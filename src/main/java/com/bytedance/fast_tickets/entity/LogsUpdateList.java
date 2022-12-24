package com.bytedance.fast_tickets.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class LogsUpdateList {
    private List<String> idList;
    private int status;
    private Date updateTime;

    public LogsUpdateList(){
        this.updateTime = new Date();
    }

    public LogsUpdateList(List<String> idList, int status){
        this.setIdList(idList);
        this.setStatus(status);
        this.updateTime = new Date();
    }
}
