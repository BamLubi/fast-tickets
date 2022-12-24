package com.bytedance.fast_tickets.controller;

import com.bytedance.fast_tickets.entity.Logs;
import com.bytedance.fast_tickets.service.LogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/logs")
@Slf4j
public class LogsController {
    @Autowired
    private LogsService logsService;

    @GetMapping("/count")
    public List<Map<String, Object>> count() {return logsService.count();}

    @PostMapping("/find")
    public Logs findById(@RequestBody Logs logs) {return logsService.findById(logs.getId());}
}
