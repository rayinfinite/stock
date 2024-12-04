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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppService {
    @Value("${stock.platform}")
    private String platform;

    public List<StockData> getStockData(String stockCode,int period) {
        StockService stockService = StockFactory.getStockStrategy(platform);
        return stockService.getStockData(stockCode,period);
    }

    public MarketDepth getMarketDepth(String stockCode) {
        StockService stockService = StockFactory.getStockStrategy(platform);
        return stockService.getMarketDepth(stockCode);
    }

    public StockInfo getStockInfo(String stockCode) {
        StockService stockService = StockFactory.getStockStrategy(platform);
        return stockService.getStockInfo(stockCode);
    }

    public List<TickTrade> getTickTrade(String stockCode) {
        StockService stockService = StockFactory.getStockStrategy(platform);
        return stockService.getTickTrade(stockCode);
    }
}
