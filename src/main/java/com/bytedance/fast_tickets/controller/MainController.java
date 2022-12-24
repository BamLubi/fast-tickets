package com.bytedance.fast_tickets.controller;

import com.bytedance.fast_tickets.config.JmsConfig;
import com.bytedance.fast_tickets.entity.Inventory;
import com.bytedance.fast_tickets.entity.Logs;
import com.bytedance.fast_tickets.service.InventoryService;
import com.bytedance.fast_tickets.service.LogsService;
import com.bytedance.fast_tickets.utils.Result;
import com.bytedance.fast_tickets.utils.TicketNoLeftException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@RestController
@Slf4j
public class MainController implements InitializingBean {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private LogsService logsService;
    @Autowired
    private RedisScript<Long> redisScript;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    private Map<String, Boolean> isEmpty = new HashMap<>();

    @PostMapping("/buy")
    public WebAsyncTask<Result<String>> buyTicketAsync(@RequestBody Logs logs) {
        String goodsId = logs.getGoodsId();
        final Boolean[] isRollRedis = {false};
        WebAsyncTask<Result<String>> webAsyncTask = new WebAsyncTask<>(3000, new Callable<Result<String>>() {
            @Override
            public Result<String> call() {
                try {
                    // 1. 检查内存标记
                    if (isEmpty.containsKey(goodsId)) {
                        throw new TicketNoLeftException();
                    }
                    // 2. 使用Redis脚本实现redis预扣减
                    Long stock = (Long) redisTemplate.execute(redisScript, Collections.singletonList("goods:" + goodsId));
                    if (stock <= 0) {
                        isEmpty.put(goodsId, true);
                        throw new TicketNoLeftException();
                    }
                    isRollRedis[0] = true;
                    // 3. Redis预减库存没问题, 将状态信息写入数据库, 将请求写入消息队列
                    // 3.1 将抢票状态写入数据库
                    logs.setStatus(2);
                    logsService.save(logs);
                    // 3.2 将请求写入消息队列
                    rocketMQTemplate.convertAndSend(JmsConfig.TOPIC, logs.getId() + ":" + goodsId);
                } catch (TicketNoLeftException e){
                    logs.setStatus(0);
                    logsService.save(logs);
                    return new Result<String>(true, Result.StatusCode.OK, "NO_LEFT", logs.getId());
                } catch (Exception e) {
                    logs.setStatus(0);
                    logsService.save(logs);
                    // 回滚redis
                    if (isRollRedis[0]){
                        redisTemplate.opsForValue().increment("goods:" + goodsId);
                    }
                    return new Result<String>(true, Result.StatusCode.ERROR, "FAIL", logs.getId());
                }
                return new Result<String>(true, Result.StatusCode.OK, "PENDING", logs.getId());
            }
        });
        webAsyncTask.onTimeout(new Callable<Result<String>>() {
            @Override
            public Result<String> call() throws Exception {
                logs.setStatus(0);
                logsService.save(logs);
                // 回滚redis
                if (isRollRedis[0]){
                    redisTemplate.opsForValue().increment("goods:" + goodsId);
                }
                return new Result<String>(true, Result.StatusCode.ERROR, "FAIL", logs.getId());
            }
        });
        return webAsyncTask;
    }


    /**
     * 系统初始化
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 加载商品库存
        System.out.println("Init Redis...");
        List<Inventory> list = inventoryService.findAll();
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(goods -> {
                System.out.println("REDIS: SET " + "goods:" + goods.getId() + " = " + goods.getStock());
                redisTemplate.opsForValue().set("goods:" + goods.getId(), goods.getStock() + "");
            });
        }
        System.out.println("Init Redis Success!");
        System.out.println("Test Redis...");
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(goods -> {
                System.out.println("REDIS: GET " + "goods:" + goods.getId() + " = "
                        + redisTemplate.opsForValue().get("goods:" + goods.getId()));
            });
        }
        System.out.println("Test Redis Success!");
    }
}
