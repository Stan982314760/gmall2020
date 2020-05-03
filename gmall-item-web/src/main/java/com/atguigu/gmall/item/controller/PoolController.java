package com.atguigu.gmall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@RestController
public class PoolController {

    @Qualifier("mainThreadPool")
    @Autowired
    ThreadPoolExecutor mainThreadPool;

    @GetMapping("/monitor/mainpool/status")
    public Map<String,Object> poolStatus() {
        Map<String,Object> map = new HashMap<>();

        int activeCount = mainThreadPool.getActiveCount();
        int corePoolSize = mainThreadPool.getCorePoolSize();
        long completedTaskCount = mainThreadPool.getCompletedTaskCount();

        map.put("activeCount", activeCount);
        map.put("corePoolSize", corePoolSize);
        map.put("completedTaskCount", completedTaskCount);

        return map;
    }

    @GetMapping("/monitor/mainpool/shutdown")
    public String shutdown() {
        mainThreadPool.shutdown();
        if (mainThreadPool.isShutdown()) {
            return "success";
        }

        return "fail";
    }


}
