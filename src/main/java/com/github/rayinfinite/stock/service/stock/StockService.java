package com.github.rayinfinite.stock.service.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rayinfinite.stock.entity.MarketDepth;
import com.github.rayinfinite.stock.entity.StockData;
import com.github.rayinfinite.stock.entity.StockUrlProperties;
import com.github.rayinfinite.stock.entity.exception.WebCrawlerException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public interface StockService {
    HttpClient client = HttpClient.newHttpClient();
    ObjectMapper objectMapper = new ObjectMapper();

    default String getStockData(String stockCode, StockUrlProperties properties) {
        String url = properties.getUrl().replace("{id}", stockCode);
        HttpRequest request;
        System.out.println(url);
        if (properties.getHeader().equals("url")) {
            String formattedUrl = properties.getUrl().replace("{token}", properties.getToken());
            request = HttpRequest.newBuilder().uri(URI.create(formattedUrl)).build();
        } else {
            request = HttpRequest.newBuilder().uri(URI.create(url)).header(properties.getHeader(),
                    properties.getToken()).build();
        }
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        }catch (IOException e) {
            throw new WebCrawlerException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new WebCrawlerException(e);
        }
    }

    List<StockData> getStockData(String stockCode, int period);

    List<StockData> parseJson(String response);

    String key();

    MarketDepth getMarketDepth(String stockCode);
}
