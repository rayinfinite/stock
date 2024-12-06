package com.github.rayinfinite.stock.service;

import com.github.rayinfinite.stock.entity.MarketDepth;
import com.github.rayinfinite.stock.entity.StockInfo;
import com.github.rayinfinite.stock.entity.TickTrade;
import com.github.rayinfinite.stock.entity.table.StockData;
import com.github.rayinfinite.stock.repository.StockDataRepository;
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
    private final StockDataRepository repository;

    private StockService getStockService() {
        return StockFactory.getStockStrategy(platform);
    }

    @Cacheable("stockData")
    public List<StockData> getStockData(String stockCode, int period) {
        List<StockData> stockDataList = getStockService().getStockData(stockCode, period);
        for (StockData data : stockDataList) {
            data.getId().setStockCode(stockCode);
            data.getId().setPeriod(period);
        }
        Thread.startVirtualThread(() -> {
            boolean exists = repository.existsByIdStockCodeAndIdPeriod(stockCode, period);
            List<StockData> newList = stockDataList.subList(0, Math.max(0, stockDataList.size() - 1));
            if(!exists) {
                repository.saveAll(newList);
                //TODO: 提取更早的数据
            }else{
                StockData lastData = repository.findTopByStockCodeAndPeriodOrderByTimestampDesc(stockCode, period);
                List<StockData> filterList = newList.stream().filter(stockData -> stockData.getId().getTimestamp() > lastData.getId().getTimestamp()).toList();
                repository.saveAll(filterList);
            }
        });
        return stockDataList;
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
