import React, { useEffect, useState } from "react";
import { getStockInfo, StockInfoType } from "./api";
import { Col, Row } from "antd";

interface MarketDepthProps {
  stockCode: string;
}

const StockInfo: React.FC<MarketDepthProps> = ({ stockCode }) => {
  const [stockInfo, setStockInfo] = useState<StockInfoType>();

  useEffect(() => {
    const fetchStockInfo = async () => {
      const data = await getStockInfo(stockCode);
      if (data.issueDate && data.issueDate != "0") {
        setStockInfo(data);
      }
    };
    fetchStockInfo();

    const intervalId = setInterval(fetchStockInfo, 5000);
    return () => clearInterval(intervalId);
  }, [stockCode]);

  if (!stockInfo) {
    return <div>加载中...</div>;
  }

  return (
    <div>
      <h1>
        {stockInfo.name} ({stockInfo.stockCode})
      </h1>
      <div
        style={{
          display: "flex",
          alignItems: "center",
          color: Number(stockInfo.change) > 0 ? "red" : "green",
          fontSize: "20px",
        }}
      >
        <p style={{ margin: "0 0 10px", fontSize: "40px", fontWeight: "bold" }}>{stockInfo.current}</p>
        <p style={{ margin: "0 10px" }}>{stockInfo.change}</p>
        <p>{stockInfo.percent}%</p>
      </div>
      <Row style={{ marginBottom: "20px" }}>
        <Col span={6}>开盘价: {stockInfo.open}</Col>
        <Col span={6}>最高价: {stockInfo.high}</Col>
        <Col span={6}>最低价: {stockInfo.low}</Col>

        <Col span={6}>发行日期: {new Date(parseInt(stockInfo.issueDate)).toLocaleDateString("en-CA")}</Col>
        <Col span={6}>货币: {stockInfo.currency}</Col>
        <Col span={6}>交易所: {stockInfo.stockExchange}</Col>
        <Col span={6}>均价: {stockInfo.avgPrice}</Col>

        {!Number.isNaN(stockInfo?.floatMarketCapital) && (
          <Col span={6}>流通市值: {formatNumber(stockInfo.floatMarketCapital)}</Col>
        )}
        <Col span={6}>总市值: {formatNumber(stockInfo.marketCapital)}</Col>
        {!Number.isNaN(stockInfo?.floatShares) && <Col span={6}>流通股本: {formatNumber(stockInfo.floatShares)}</Col>}
        <Col span={6}>总股本: {formatNumber(stockInfo.totalShares)}</Col>
        <Col span={6}>成交金额: {formatNumber(stockInfo.amount)}</Col>
        <Col span={6}>成交量: {formatNumber(stockInfo.volume)}</Col>
      </Row>
    </div>
  );
};

export default StockInfo;

const formatNumber = (num: number): string => {
  if (num >= 1e12) {
    return (num / 1e12).toFixed(2) + " 万亿";
  } else if (num >= 1e8) {
    return (num / 1e8).toFixed(2) + " 亿";
  } else if (num >= 1e4) {
    return (num / 1e4).toFixed(2) + " 万";
  } else {
    return num.toString();
  }
};
