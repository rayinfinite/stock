package com.github.rayinfinite.stock.service.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rayinfinite.stock.entity.MarketDepth;
import com.github.rayinfinite.stock.entity.StockData;
import com.github.rayinfinite.stock.entity.StockUrlProperties;
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

    List<StockData> parseJson(String response);

    String key();

    MarketDepth getMarketDepth(String stockCode);
}
