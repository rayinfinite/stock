package com.github.rayinfinite.stock.entity;

import lombok.Data;
// tick for 3 seconds
@Data
public class TickTrade {
    long timestamp;
    String current;
    long volume;
}
