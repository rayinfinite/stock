package com.github.rayinfinite.stock.service.stock;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StockFactory implements InitializingBean {
    private final ApplicationContext applicationContext;

    private static final Map<String, StockService> strategyMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        Map<String, StockService> beansOfType = applicationContext.getBeansOfType(StockService.class);
        beansOfType.forEach((k, v) -> strategyMap.put(v.key(), v));
    }

    public static StockService getStockStrategy(String type){
        return strategyMap.get(type);
    }
}
