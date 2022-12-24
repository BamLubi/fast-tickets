package com.bytedance.fast_tickets.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
public class BaseEntity implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    protected String id;

    protected Date createTime;
    protected Date updateTime;

    public void preInsert() {
        this.id = UUID.randomUUID().toString().replace("-", "");
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    public void preUpdate() {
        this.updateTime = new Date();
    }
}
