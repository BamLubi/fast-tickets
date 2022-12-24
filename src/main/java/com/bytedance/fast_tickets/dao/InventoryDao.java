package com.bytedance.fast_tickets.dao;

import com.bytedance.fast_tickets.entity.DecStock;
import com.bytedance.fast_tickets.entity.Inventory;

public interface InventoryDao extends BaseDao<Inventory>{
    void decStock(DecStock decStock);
}
