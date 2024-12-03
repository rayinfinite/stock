package com.github.rayinfinite.stock.entity;

import lombok.Data;

import java.util.List;

@Data
public class MarketDepth {
    long timestamp;
    String stockCode;
    String price;
    List<String> buyPrices;
    List<String> buyVolumes;
    List<String> sellPrices;
    List<String> sellVolumes;
    String buyPct;
    String sellPct;
}
