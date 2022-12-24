package com.bytedance.fast_tickets.controller;

import com.bytedance.fast_tickets.entity.Inventory;
import com.bytedance.fast_tickets.service.InventoryService;
import com.bytedance.fast_tickets.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@Slf4j
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/all")
    public List<Inventory> findAll() {return inventoryService.findAll();}

    @PostMapping("/save")
    public Result<Void> save(@RequestBody Inventory inventory) {
        inventoryService.save(inventory);
        return Result.SUCCESS_RESULT;
    }
}
