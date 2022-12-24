package com.bytedance.fast_tickets.dao;

import com.bytedance.fast_tickets.entity.Logs;
import com.bytedance.fast_tickets.entity.LogsUpdateList;

import java.util.List;
import java.util.Map;

public interface LogsDao extends BaseDao<Logs> {
    List<Map<String, Object>> count();
    void updateList(LogsUpdateList logsUpdateList);
}
