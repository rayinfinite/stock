package com.github.rayinfinite.stock.service;

import com.github.rayinfinite.stock.entity.MarketDepth;
import com.github.rayinfinite.stock.entity.StockData;
import com.github.rayinfinite.stock.entity.StockInfo;
import com.github.rayinfinite.stock.entity.TickTrade;
import com.github.rayinfinite.stock.service.stock.StockFactory;
import com.github.rayinfinite.stock.service.stock.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppService {
    @Value("${stock.platform}")
    private String platform;

    private StockService getStockService() {
        return StockFactory.getStockStrategy(platform);
    }

    @Cacheable("stockData")
    public List<StockData> getStockData(String stockCode, int period) {
        return getStockService().getStockData(stockCode, period);
    }

    @Cacheable("marketDepth")
    public MarketDepth getMarketDepth(String stockCode) {
        return getStockService().getMarketDepth(stockCode);
    }

    @Cacheable("stockInfo")
    public StockInfo getStockInfo(String stockCode) {
        return getStockService().getStockInfo(stockCode);
    }

    @Cacheable("tickTrade")
    public List<TickTrade> getTickTrade(String stockCode) {
        return getStockService().getTickTrade(stockCode);
    }
}
