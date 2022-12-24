package com.bytedance.fast_tickets.mq;

import com.bytedance.fast_tickets.config.JmsConfig;
import com.bytedance.fast_tickets.service.InventoryService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Consumer implements InitializingBean {

    @Autowired
    private InventoryService inventoryService;

    @Override
    public void afterPropertiesSet() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(JmsConfig.CONSUMER_GROUP);
        //设置consumer所订阅的Topic和Tag，*代表全部的Tag
        consumer.subscribe(JmsConfig.TOPIC, "*");
        consumer.setNamesrvAddr(JmsConfig.NAME_SERVER);
        // 设置消息拉取间隔 ms
        consumer.setPullInterval(1000);
        // 设置最小消费线程数
        consumer.setConsumeThreadMin(1);
        // 最大消费线程池数
        consumer.setConsumeThreadMax(10);
        // 设置消费者单次批量消费的消息数目上限，默认1
        consumer.setConsumeMessageBatchMaxSize(32);
        // 设置每个队列每次拉取的最大消费数，默认32
        consumer.setPullBatchSize(32);
        //集群订阅
        consumer.setMessageModel(MessageModel.CLUSTERING);
        //这里设置的是一个consumer的消费策略
        //CONSUME_FROM_LAST_OFFSET 默认策略，从该队列最尾开始消费，即跳过历史消息
        //CONSUME_FROM_FIRST_OFFSET 从队列最开始开始消费，即历史消息（还储存在broker的）全部消费一遍
        //CONSUME_FROM_TIMESTAMP 从某个时间点开始消费，和setConsumeTimestamp()配合使用，默认是半个小时以前
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        //设置一个Listener，主要进行消息的逻辑处理
        consumer.registerMessageListener((MessageListenerConcurrently) (message, context) -> {
            List<String> logsIdList = new ArrayList<>();
            String goodsId = "";
            Map<String, List<String>> hash = new HashMap<>();
            for (MessageExt messageExt : message) {
                // 获取到订单id和商品id
                String[] msg = new String(messageExt.getBody()).split(":");
//                logsIdList.add(msg[0]);
//                goodsId = msg[1];
                if (hash.containsKey(msg[1])){
                    hash.get(msg[1]).add(msg[0]);
                } else {
                    List<String> _list = new ArrayList<>();
                    _list.add(msg[0]);
                    hash.put(msg[1], _list);
                }
            }
            // 更新数据库
            hash.forEach((key, value) -> inventoryService.buyTicket(key, value));
            //返回消费状态
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        //调用start()方法启动consumer
        consumer.start();
    }
}
