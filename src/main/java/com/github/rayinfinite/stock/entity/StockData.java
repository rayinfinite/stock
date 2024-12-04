package com.github.rayinfinite.stock.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// https://klinecharts.com/guide/data-source
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockData {
    long timestamp;
    String open;
    String close;
    String high;
    String low;
    String volume;
    String turnover;

    public StockData(MairuiInputData input) {
        this.timestamp = Long.parseLong(input.d);
        this.open = input.o;
        this.high = input.h;
        this.low = input.l;
        this.close = input.c;
        this.volume = input.v;
        this.turnover = input.e;
    }
}
