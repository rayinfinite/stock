import { useEffect } from "react";
import { useViewport } from "./ViewportContext";

import { init, dispose, Chart, Nullable } from "klinecharts";
import { getStockData } from "./api";
import ChartComponent from "./ChartComponent";

const App = () => {
  const viewport = useViewport();
  if (!viewport) return null;

  useEffect(() => {
    const chart: Nullable<Chart> = init("chart");
    getData(chart);
    return () => {
      dispose("chart");
    };
  }, []);

  const getData = async (chart: Nullable<Chart>) => {
    const stockData = await getStockData("000001");
    chart?.applyNewData(stockData);
  };
  return (
    <div style={{ padding: viewport.width > 660 ? "0px 24px" : "0" }}>
      <h1>Stock Analyse System</h1>
      <div id="chart" style={{ height: 600 }} />
      <ChartComponent/>
    </div>
  );
};

export default App;
