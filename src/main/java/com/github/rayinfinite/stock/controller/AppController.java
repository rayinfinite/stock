package com.github.rayinfinite.stock.controller;

import com.github.rayinfinite.stock.entity.Response;
import com.github.rayinfinite.stock.entity.StockData;
import com.github.rayinfinite.stock.service.AppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class AppController {
    private final AppService service;

    @GetMapping()
    public Response getStockData(String stockCode, @RequestParam(required = false) Integer period) {
        if (period == null) {
            period = 0;
        }
        List<StockData> data = service.getStockData(stockCode, period);
        return new Response(data);
    }

    @GetMapping("/marketDepth")
    public Response getMarketDepth(String stockCode) {
        return new Response(service.getMarketDepth(stockCode));
    }

    @GetMapping("/stockInfo")
    public Response getStockInfo(String stockCode) {
        return new Response(service.getStockInfo(stockCode));
    }

    @GetMapping("/tickTrade")
    public Response getTickTrade(String stockCode) {
        return new Response(service.getTickTrade(stockCode));
    }
}
