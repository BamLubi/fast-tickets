package com.bytedance.fast_tickets;

import com.bytedance.fast_tickets.controller.InventoryController;
import com.bytedance.fast_tickets.entity.Inventory;
import com.bytedance.fast_tickets.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class InventoryTests {
    @Autowired
    InventoryController inventoryController;
    @Autowired
    InventoryService inventoryService;

    @Test
    void getAllInventory() {
        List<Inventory> L = inventoryController.findAll();
        System.out.println(L.toString());
    }

    @Test
    void addInventory(){
        Inventory inventory = new Inventory();
        inventory.setName("Ticket 5");
        inventory.setStock(1024);
        inventoryController.save(inventory);
    }

}
