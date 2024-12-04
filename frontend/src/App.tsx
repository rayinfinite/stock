import { useEffect, useRef, useState } from "react";
import { useViewport } from "./ViewportContext";

import { init, dispose, Chart, Nullable } from "klinecharts";
import { getStockData } from "./api";
import { Input, Radio, RadioChangeEvent } from "antd";
import MarketDepth from "./MarketDepth";
import StockInfo from "./StockInfo";
import TickTrade from "./TickTrade";
import KLineChart from "./KLineChart";

const App = () => {
  const viewport = useViewport();
  if (!viewport) return null;
  const [stockCode, setStockCode] = useState<string>("SZ000001");

  return (
    <div style={{ padding: viewport.width > 660 ? "0px 24px" : "0" }}>
      <h1>Stock Analyse System</h1>
      {stockCode && <StockInfo stockCode={stockCode} />}
      <KLineChart stockCode={stockCode} setStockCode={setStockCode} />
      {stockCode && (
        <div style={{ display: "flex", justifyContent: "space-between" }}>
          <MarketDepth stockCode={stockCode} />
          <TickTrade stockCode={stockCode} />
        </div>
      )}
    </div>
  );
};

export default App;
