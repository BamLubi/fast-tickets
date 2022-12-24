package com.bytedance.fast_tickets.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DecStock {
    private String id;
    private int num;
    private Date updateTime;

    public DecStock(){
        this.updateTime = new Date();
    }

    public DecStock(String id, int num){
        this.setId(id);
        this.setNum(num);
        this.updateTime = new Date();
    }
}
