package com.github.rayinfinite.stock.service.stock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.rayinfinite.stock.entity.MairuiInputData;
import com.github.rayinfinite.stock.entity.StockData;
import com.github.rayinfinite.stock.entity.StockUrlProperties;
import com.github.rayinfinite.stock.entity.exception.WebCrawlerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Mairui implements StockService {
    private static final String URL = "https://api.mairui.club/hszbl/fsjy/{id}/dn/{token}";
    private static final String HEADER = "url";
    private final StockUrlProperties properties;

    @Override
    public List<StockData> getStockData(String stockCode, int period) {
        properties.setUrl(URL);
        properties.setHeader(HEADER);
        stockCode = stockCode.toLowerCase();
        if (stockCode.startsWith("sh") || stockCode.startsWith("sz")) {
            stockCode = stockCode.substring(2);
        }
        String response = getStockData(stockCode, properties);
        return parseJson(response);
    }

    public List<StockData> parseJson(String response) {
        List<MairuiInputData> mairuiInputDataList;
        try {
            mairuiInputDataList = objectMapper.readValue(response, new TypeReference<>() {
            });
            return mairuiInputDataList.stream().map(StockData::new).toList();
        } catch (JsonProcessingException e) {
            throw new WebCrawlerException(e);
        }
    }

    @Override
    public String key() {
        return "mairui";
    }
}
