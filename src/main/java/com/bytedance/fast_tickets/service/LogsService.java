package com.bytedance.fast_tickets.service;

import com.bytedance.fast_tickets.dao.LogsDao;
import com.bytedance.fast_tickets.entity.Logs;
import com.bytedance.fast_tickets.entity.LogsUpdateList;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LogsService extends BaseService<LogsDao, Logs>{
    /**
     * 获取所有人的购买情况
     * @return
     */
    public List<Map<String, Object>> count(){
        return dao.count();
    }

    public void updateList(List<String> idList, int status) { dao.updateList(new LogsUpdateList(idList, status)); }
}
