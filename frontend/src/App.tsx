import { useEffect, useRef, useState } from "react";
import { useViewport } from "./ViewportContext";

import { init, dispose, Chart, Nullable } from "klinecharts";
import { getStockData } from "./api";
import { Input, Radio, RadioChangeEvent } from "antd";
import ChartComponent from "./ChartComponent";

const App = () => {
  const viewport = useViewport();
  if (!viewport) return null;
  const [period, setPeriod] = useState("0");
  const [stockCode, setStockCode] = useState("SZ000001");
  const chartRef = useRef<Nullable<Chart>>(null);

  useEffect(() => {
    initChart();
    getData(stockCode, period);
    return () => {
      dispose("chart");
    };
  }, []);

  const periodOptions = [
    { label: "Day", value: "0" },
    { label: "Week", value: "1" },
    { label: "Month", value: "2" },
    { label: "Quarter", value: "3" },
    { label: "Year", value: "4" },
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
    chartRef.current?.applyNewData(stockData);
  };

  return (
    <div style={{ padding: viewport.width > 660 ? "0px 24px" : "0" }}>
      <h1>Stock Analyse System</h1>
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

export default App;
