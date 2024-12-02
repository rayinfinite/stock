package com.github.rayinfinite.stock.service.stock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.rayinfinite.stock.entity.StockData;
import com.github.rayinfinite.stock.entity.StockUrlProperties;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
public class Xueqiu implements StockService {
    private static final String URL = "https://stock.xueqiu.com/v5/stock/chart/kline.json?" +
            "symbol={id}&begin={begin}&period=day&type=before&count=-{count}";
    private static final String HEADER = "url";
    private final StockUrlProperties properties;

    @Override
    public List<StockData> getStockData(String stockCode) throws IOException, InterruptedException {
        String url = URL.replace("{begin}", String.valueOf(System.currentTimeMillis()))
                .replace("{count}", "500");
        properties.setUrl(url);
        properties.setHeader(HEADER);
        return getStockData(stockCode, properties);
    }

    @Override
    public List<StockData> parseJson(String response) {
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode columnsNode = rootNode.path("data").path("columns");
        if (!columnsNode.isArray()) {
            throw new IllegalArgumentException("The 'columns' node is not an array.");
        }
        List<StockData> result = new ArrayList<>();
        for (Iterator<JsonNode> it = columnsNode.elements(); it.hasNext(); ) {
            JsonNode rowNode = it.next();
            if (!rowNode.isArray()) {
                throw new IllegalArgumentException("Each element in 'columns' should be an array.");
            }

            List<String> row = new ArrayList<>();
            for (JsonNode cellNode : rowNode) {
                row.add(cellNode.asText());
            }
// "timestamp","volume","open","high","low","close","chg","percent","turnoverrate","amount","volume_post","amount_post"
            StockData stockData = new StockData(row.get(0), row.get(2), row.get(5), row.get(3), row.get(4),
                    row.get(1), row.get(8));
            result.add(stockData);
        }
        return result;
    }

    @Override
    public String key() {
        return "xueqiu";
    }
}
