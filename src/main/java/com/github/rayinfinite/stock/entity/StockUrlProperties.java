package com.github.rayinfinite.stock.entity;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "stock")
public class StockUrlProperties {
    private String url;
    private String token;
    private String header;
}
