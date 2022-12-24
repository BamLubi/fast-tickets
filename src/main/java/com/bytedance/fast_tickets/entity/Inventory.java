package com.bytedance.fast_tickets.entity;

import lombok.Data;

@Data
public class Inventory extends BaseEntity{
    private String name;
    private int stock;
}
