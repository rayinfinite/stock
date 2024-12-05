package com.github.rayinfinite.stock.entity.table;

import com.github.rayinfinite.stock.entity.MairuiInputData;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// https://klinecharts.com/guide/data-source
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class StockData {
    @EmbeddedId
    private StockDataPrimaryKey id;
    private String open;
    private String close;
    private String high;
    private String low;
    private String volume;
    private String turnover;

    public StockData(MairuiInputData input) {
        this.id = new StockDataPrimaryKey();
        this.id.setTimestamp(Long.parseLong(input.getD()));

        this.open = input.getO();
        this.high = input.getH();
        this.low = input.getL();
        this.close = input.getC();
        this.volume = input.getV();
        this.turnover = input.getE();
    }
}
