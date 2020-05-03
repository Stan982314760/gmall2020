package com.atguigu.gmall.item.pool;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "gmall.pool.main")
@Component
@Data
public class MainPoolProperties {

    private Integer corePoolSize;
    private Integer maximumPoolSize;
    private Integer workQueueCapacity;
}
