package com.github.rayinfinite.stock.service.stock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.rayinfinite.stock.entity.*;
import com.github.rayinfinite.stock.entity.exception.WebCrawlerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Xueqiu implements StockService {
    private static final String BASE_URL = "https://stock.xueqiu.com/v5/stock";
    private static final String STOCK_URL = BASE_URL + "/chart/kline.json?symbol={id}&begin={begin}&period" +
            "={period}&type=before&count=-{count}";
    private static final String MARKET_DEPTH_URL = BASE_URL + "/realtime/pankou.json?symbol={id}";
    private static final String STOCK_INFO_URL = BASE_URL + "/quote.json?symbol={id}&extend=detail";
    private static final String TICK_TRADE_URL = BASE_URL + "/history/trade.json?symbol={id}";
    private static final String HEADER = "cookie";
    private static final List<String> periodList = List.of("day", "week", "month", "quarter", "year");
    private static final List<String> negativePeriodList = List.of("1m");
    private final StockUrlProperties properties;

    @Override
    public List<StockData> getStockData(String stockCode, int period) {
        String periodIndex = period >= 0 ? periodList.get(period) : negativePeriodList.get(-period - 1);
        String url = STOCK_URL.replace("{begin}", String.valueOf(System.currentTimeMillis()))
                .replace("{count}", "500")
                .replace("{period}", periodIndex);
        properties.setUrl(url);
        properties.setHeader(HEADER);
        String response = getStockData(stockCode.toUpperCase(), properties);
        return parseJson(response);
    }

    public List<StockData> parseJson(String response) {
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new WebCrawlerException(e);
        }
        JsonNode itemNode = rootNode.path("data").path("item");
        if (!itemNode.isArray()) {
            throw new IllegalArgumentException("The 'item' node is not an array.");
        }
        List<StockData> result = new ArrayList<>();
        for (Iterator<JsonNode> it = itemNode.elements(); it.hasNext(); ) {
            JsonNode rowNode = it.next();
            if (!rowNode.isArray()) {
                throw new IllegalArgumentException("Each element in 'item' should be an array.");
            }

            List<String> row = new ArrayList<>();
            for (JsonNode cellNode : rowNode) {
                row.add(cellNode.asText());
            }
// "timestamp","volume","open","high","low","close","chg","percent","turnoverrate","amount","volume_post","amount_post"
            long timestamp = Long.parseLong(row.get(0));
            StockData stockData = new StockData(timestamp, row.get(2), row.get(5), row.get(3), row.get(4), row.get(1),
                    row.get(8));
            result.add(stockData);
        }
        return result;
    }

    @Override
    public MarketDepth getMarketDepth(String stockCode) {
        properties.setUrl(MARKET_DEPTH_URL);
        properties.setHeader(HEADER);
        String response = getStockData(stockCode.toUpperCase(), properties);
        return parseMarketDepthJson(response);
    }

    public MarketDepth parseMarketDepthJson(String response) {
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new WebCrawlerException(e);
        }
        JsonNode itemNode = rootNode.path("data");
        MarketDepth marketDepth = new MarketDepth();
        marketDepth.setStockCode(itemNode.path("symbol").asText());
        marketDepth.setPrice(itemNode.path("current").asText());
        marketDepth.setTimestamp(itemNode.path("timestamp").asLong());
        marketDepth.setBuyPct(itemNode.path("buypct").asText());
        marketDepth.setSellPct(itemNode.path("sellpct").asText());
        List<String> buyPrices = new ArrayList<>();
        List<String> buyVolumes = new ArrayList<>();
        List<String> sellPrices = new ArrayList<>();
        List<String> sellVolumes = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            buyPrices.add(itemNode.path("bp" + i).asText());
            buyVolumes.add("" + itemNode.path("bc" + i).asInt() / 100);// 100股为一手
            sellPrices.add(itemNode.path("sp" + i).asText());
            sellVolumes.add("" + itemNode.path("sc" + i).asInt() / 100);
        }
        marketDepth.setBuyPrices(buyPrices);
        marketDepth.setBuyVolumes(buyVolumes);
        marketDepth.setSellPrices(sellPrices);
        marketDepth.setSellVolumes(sellVolumes);
        return marketDepth;
    }

    @Override
    public StockInfo getStockInfo(String stockCode) {
        properties.setUrl(STOCK_INFO_URL);
        properties.setHeader(HEADER);
        String response = getStockData(stockCode.toUpperCase(), properties);
        return parseStockInfoJson(response);
    }

    public StockInfo parseStockInfoJson(String response) {
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new WebCrawlerException(e);
        }
        JsonNode itemNode = rootNode.path("data").path("quote");
        StockInfo stockInfo = new StockInfo();
        stockInfo.setStockCode(itemNode.path("symbol").asText());
        stockInfo.setIssueDate(itemNode.path("issue_date").asLong());
        stockInfo.setCurrency(itemNode.path("currency").asText());
        stockInfo.setName(itemNode.path("name").asText());
        stockInfo.setStockExchange(itemNode.path("exchange").asText());

        stockInfo.setPercent(itemNode.path("percent").asText());
        stockInfo.setChange(itemNode.path("chg").asText());
        stockInfo.setAvgPrice(itemNode.path("avg_price").asText());
        stockInfo.setCurrent(itemNode.path("current").asText());
        stockInfo.setOpen(itemNode.path("open").asText());
        stockInfo.setHigh(itemNode.path("high").asText());
        stockInfo.setLow(itemNode.path("low").asText());

        stockInfo.setFloatMarketCapital(itemNode.path("float_market_capital").asText());
        stockInfo.setMarketCapital(itemNode.path("market_capital").asText());
        stockInfo.setFloatShares(itemNode.path("float_shares").asText());
        stockInfo.setTotalShares(itemNode.path("total_shares").asText());
        stockInfo.setAmount(itemNode.path("amount").asText());
        stockInfo.setVolume(itemNode.path("volume").asText());
        return stockInfo;
    }

    @Override
    public List<TickTrade> getTickTrade(String stockCode) {
        properties.setUrl(TICK_TRADE_URL);
        properties.setHeader(HEADER);
        String response = getStockData(stockCode.toUpperCase(), properties);
        return parseTickTradeJson(response);
    }

    public List<TickTrade> parseTickTradeJson(String response) {
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new WebCrawlerException(e);
        }
        JsonNode itemNode = rootNode.path("data").path("items");
        if (!itemNode.isArray()) {
            throw new IllegalArgumentException("The 'items' node is not an array.");
        }
        List<TickTrade> result = new ArrayList<>();
        for (Iterator<JsonNode> it = itemNode.elements(); it.hasNext(); ) {
            JsonNode rowNode = it.next();
            TickTrade tickTrade = new TickTrade();
            tickTrade.setTimestamp(rowNode.path("timestamp").asLong());
            tickTrade.setCurrent(rowNode.path("current").asText());
            tickTrade.setVolume(rowNode.path("trade_volume").asLong() / 100);// 100股为一手
            result.add(tickTrade);
        }
        return result;
    }

    @Override
    public String key() {
        return "xueqiu";
    }
}
