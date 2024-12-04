import { useState } from "react";
import { useViewport } from "./ViewportContext";

import MarketDepth from "./component/MarketDepth";
import StockInfo from "./component/StockInfo";
import TickTrade from "./component/TickTrade";
import KLineChart from "./component/KLineChart";

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
          <TickTrade stockCode={stockCode} />
          <MarketDepth stockCode={stockCode} />
        </div>
      )}
    </div>
  );
};

export default App;
