package com.github.rayinfinite.stock.service.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rayinfinite.stock.entity.*;
import com.github.rayinfinite.stock.utils.HttpUtils;

import java.util.List;
import java.util.Map;

public interface StockService {
    ObjectMapper objectMapper = new ObjectMapper();

    default String getStockData(String stockCode, StockUrlProperties properties) {
        String url = properties.getUrl().replace("{id}", stockCode);
        if (properties.getHeader().equals("url")) {
            String formattedUrl = properties.getUrl().replace("{token}", properties.getToken());
            return HttpUtils.get(formattedUrl);
        } else {
            return HttpUtils.get(url, Map.of(properties.getHeader(), properties.getToken()));
        }
    }

    List<StockData> getStockData(String stockCode, int period);

    String key();

    default MarketDepth getMarketDepth(String stockCode) {
        throw new UnsupportedOperationException("Market depth is not supported by " + key() + ".");
    }

    default StockInfo getStockInfo(String stockCode) {
        throw new UnsupportedOperationException("Stock info is not supported by " + key() + ".");
    }

    default List<TickTrade> getTickTrade(String stockCode) {
        throw new UnsupportedOperationException("Tick Trade is not supported by " + key() + ".");
    }
}
