package com.bytedance.fast_tickets.service;

import com.bytedance.fast_tickets.dao.InventoryDao;
import com.bytedance.fast_tickets.entity.DecStock;
import com.bytedance.fast_tickets.entity.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
public class InventoryService extends BaseService<InventoryDao, Inventory>{
    @Autowired
    LogsService logsService;

    @Transactional
    public int buyTicket(String goodsId, List<String> logsIdList) {
        System.out.println();
        try{
            // 1. 更新用户抢购成功状态
            logsService.updateList(logsIdList, 1);
            // 2. 库存减少
            this.decStock(goodsId, logsIdList);
        } catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            // 3. 更新用户抢购失败记录
            logsService.updateList(logsIdList, 0);
        }
        return 1;
    }

    private void decStock(String goodsId, List<String> logsIdList){
        dao.decStock(new DecStock(goodsId, logsIdList.size()));
    }
}
