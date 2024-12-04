package com.github.rayinfinite.stock.entity;

import lombok.Data;

@Data
public class StockInfo {
    String stockCode;
    long issueDate; // stock start date
    String currency;
    String name;
    String stockExchange;

    String percent;
    String change;
    String avgPrice;
    String current;
    String open;
    String high;
    String low;

    String floatMarketCapital;
    String marketCapital;
    String floatShares;
    String totalShares;
    String amount;
    String volume;
}
