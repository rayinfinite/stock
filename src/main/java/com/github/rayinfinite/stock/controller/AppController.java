package com.github.rayinfinite.stock.controller;

import com.github.rayinfinite.stock.entity.Response;
import com.github.rayinfinite.stock.entity.StockData;
import com.github.rayinfinite.stock.service.AppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class AppController {
    private final AppService service;

    @GetMapping()
    public Response getStockData(String stockCode) throws IOException, InterruptedException {
        List<StockData> data = service.getStockData(stockCode);
        return new Response(data);
    }
}
