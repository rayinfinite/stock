// 引入js
import { KLineChartPro, DefaultDatafeed } from "@klinecharts/pro";
// 引入样式
import "@klinecharts/pro/dist/klinecharts-pro.css";
import { useEffect } from "react";

const ChartComponent = () => {
  useEffect(() => {
    // 创建实例
    const chart = new KLineChartPro({
      container: document.getElementById("klinechartpro"),
      // 初始化标的信息
      symbol: {
        exchange: "XNYS",
        market: "stocks",
        name: "Alibaba Group Holding Limited American Depositary Shares, each represents eight Ordinary Shares",
        shortName: "BABA",
        ticker: "BABA",
        priceCurrency: "usd",
        type: "ADRC",
      },
      // 初始化周期
      period: { multiplier: 15, timespan: "minute", text: "15m" },
      // 这里使用默认的数据接入，如果实际使用中也使用默认数据，需要去 https://polygon.io/ 申请 API key
      datafeed: new DefaultDatafeed(`IR3qS2VjZ7kIDgnlqKxSmCRHqyBaMh9q`),
    });
  }, []);

  return <div id="klinechartpro" />;
};

export default ChartComponent;
