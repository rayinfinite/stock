package com.github.rayinfinite.stock.service;

import com.github.rayinfinite.stock.entity.MarketDepth;
import com.github.rayinfinite.stock.entity.StockData;
import com.github.rayinfinite.stock.service.stock.StockFactory;
import com.github.rayinfinite.stock.service.stock.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppService {

    public List<StockData> getStockData(String stockCode,int period) {
        StockService stockService = StockFactory.getStockStrategy("xueqiu");
        return stockService.getStockData(stockCode,period);
    }

    public MarketDepth getMarketDepth(String stockCode) {
        StockService stockService = StockFactory.getStockStrategy("xueqiu");
        return stockService.getMarketDepth(stockCode);
    }
}
