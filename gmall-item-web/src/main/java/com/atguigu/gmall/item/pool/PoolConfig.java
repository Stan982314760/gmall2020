package com.atguigu.gmall.item.pool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Configuration
public class PoolConfig {

    @Bean("mainThreadPool") // 核心任务线程池
    public ThreadPoolExecutor mainThreadPool(MainPoolProperties mainPoolProperties) {
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(mainPoolProperties.getWorkQueueCapacity());
        ThreadPoolExecutor mainThreadPool = new ThreadPoolExecutor(mainPoolProperties.getCorePoolSize(),
                mainPoolProperties.getMaximumPoolSize(), 5, TimeUnit.MINUTES, queue);

        return mainThreadPool;
    }

    @Bean("generalThreadPool") // 非核心任务线程池
    public ThreadPoolExecutor generalThreadPool(GeneralPoolProperties generalPoolProperties) {
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(generalPoolProperties.getWorkQueueCapacity());
        ThreadPoolExecutor generalThreadPool = new ThreadPoolExecutor(generalPoolProperties.getCorePoolSize(),
                generalPoolProperties.getMaximumPoolSize(), 3, TimeUnit.MINUTES, queue);

        return generalThreadPool;
    }
}
