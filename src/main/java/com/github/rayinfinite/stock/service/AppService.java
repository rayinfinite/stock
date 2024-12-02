package com.github.rayinfinite.stock.service;

import com.github.rayinfinite.stock.entity.StockData;
import com.github.rayinfinite.stock.service.stock.StockFactory;
import com.github.rayinfinite.stock.service.stock.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppService {

    public List<StockData> getStockData(String stockCode) throws IOException, InterruptedException {
        StockService stockService = StockFactory.getStockStrategy("xueqiu");
        return stockService.getStockData(stockCode);
    }
}
