package com.github.rayinfinite.stock.entity.table;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class StockDataPrimaryKey implements Serializable {
    private long timestamp;
    private String stockCode;
    private int period;
}
