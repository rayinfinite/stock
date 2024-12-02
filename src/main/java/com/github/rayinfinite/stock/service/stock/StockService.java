package com.github.rayinfinite.stock.service.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rayinfinite.stock.entity.StockData;
import com.github.rayinfinite.stock.entity.StockUrlProperties;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public interface StockService {
    HttpClient client = HttpClient.newHttpClient();
    ObjectMapper objectMapper = new ObjectMapper();

    default List<StockData> getStockData(String stockCode, StockUrlProperties properties) throws IOException,
            InterruptedException {
        String url = properties.getUrl().replace("{id}", stockCode);
        HttpRequest request;
        if (properties.getHeader().equals("url")) {
            String formattedUrl = properties.getUrl().replace("{token}", properties.getToken());
            request = HttpRequest.newBuilder().uri(URI.create(formattedUrl)).build();
        } else {
            request = HttpRequest.newBuilder().uri(URI.create(url)).header(properties.getHeader(),
                    properties.getToken()).build();
        }
        String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        return parseJson(response);
    }

    List<StockData> getStockData(String stockCode) throws IOException, InterruptedException;

    List<StockData> parseJson(String response);

    String key();
}
