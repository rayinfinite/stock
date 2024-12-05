import React, {useEffect, useRef, useState} from "react";
import {Input, Radio, RadioChangeEvent} from "antd";
import {Chart, dispose, init, Nullable} from "klinecharts";
import {getStockData} from "../api";

interface KLineChartProps {
  stockCode: string;
  setStockCode: (value: string) => void;
}

const KLineChart: React.FC<KLineChartProps> = ({ stockCode, setStockCode }) => {
  const [period, setPeriod] = useState("0");
  const chartRef = useRef<Nullable<Chart>>(null);

  useEffect(() => {
    initChart();
    getData(stockCode, period);
    const intervalId = setInterval(() => getData(stockCode, period), 6000);
    return () => {
      dispose("chart");
      clearInterval(intervalId);
    };
  }, [stockCode, period]);

  const periodOptions = [
    { label: "Day", value: "0" },
    { label: "Week", value: "1" },
    { label: "Month", value: "2" },
    { label: "Quarter", value: "3" },
    { label: "Year", value: "4" },
    { label: "1 Minute", value: "-1" },
  ];

  const initChart = () => {
    chartRef.current = init("chart", {
      layout: [
        {
          type: "candle",
          content: ["MA", { name: "EMA", calcParams: [10, 30] }],
          options: { order: Number.MIN_SAFE_INTEGER },
        },
        { type: "indicator", content: ["VOL"], options: { order: 10 } },
        { type: "xAxis", options: { order: 9 } },
      ],
    });
  };

  const changePeriod = async ({ target: { value } }: RadioChangeEvent) => {
    setPeriod(value);
    await getData(stockCode, value);
  };
  const onSearchStock = async (stockCode: string) => {
    setStockCode(stockCode);
    await getData(stockCode, period);
  };
  const getData = async (stockCode: string, period: string) => {
    const stockData = await getStockData(stockCode, period);
    if (stockData.length === 0) return;
    chartRef.current?.applyNewData(stockData);
  };
  return (
    <div>
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
        }}
      >
        <Radio.Group
          options={periodOptions}
          optionType="button"
          buttonStyle="solid"
          onChange={changePeriod}
          value={period}
        />
        <Input.Search
          style={{ maxWidth: "400px" }}
          placeholder="input stock code"
          onSearch={onSearchStock}
          defaultValue={stockCode}
          enterButton
        />
      </div>
      <div id="chart" style={{ height: 600 }} />
    </div>
  );
};

export default KLineChart;
