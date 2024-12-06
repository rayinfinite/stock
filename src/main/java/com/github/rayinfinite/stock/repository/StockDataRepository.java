package com.github.rayinfinite.stock.repository;

import com.github.rayinfinite.stock.entity.table.StockData;
import com.github.rayinfinite.stock.entity.table.StockDataPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockDataRepository extends JpaRepository<StockData, StockDataPrimaryKey> {
    boolean existsByIdStockCodeAndIdPeriod(String stockCode, int period);

    @Query("SELECT s FROM StockData s WHERE s.id.stockCode = :stockCode AND s.id.period = :period ORDER BY s.id.timestamp DESC")
    StockData findTopByStockCodeAndPeriodOrderByTimestampDesc(@Param("stockCode") String stockCode, @Param("period") int period);

    List<StockData> findByIdStockCodeAndIdPeriod(String stockCode, int period);
}
