package com.bytedance.fast_tickets.entity;

import lombok.Data;

@Data
public class Logs extends BaseEntity{
    private String userId;
    private String goodsId;
    private int status; // 状态: 0:失败,1:成功,2:排队中

    public Logs(){}

    public Logs(String userId, String goodsId){
        this.setUserId(userId);
        this.setGoodsId(goodsId);
    }

    public Logs(String userId, String goodsId, int status){
        this.setUserId(userId);
        this.setGoodsId(goodsId);
        this.setStatus(status);
    }
}
