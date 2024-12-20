import React, {useEffect, useState} from "react";
import {getStockInfo, StockInfoType} from "../api";
import {Col, Row} from "antd";

interface MarketDepthProps {
  stockCode: string;
}

const StockInfo: React.FC<MarketDepthProps> = ({ stockCode }) => {
  const [data, setData] = useState<StockInfoType>();

  useEffect(() => {
    setData(undefined);
    const fetchStockInfo = async () => {
      const data = await getStockInfo(stockCode);
      if (data.issueDate && data.issueDate != "0") {
        setData(data);
      }
    };
    fetchStockInfo();

    const intervalId = setInterval(fetchStockInfo, 3000);
    return () => clearInterval(intervalId);
  }, [stockCode]);

  if (!data) {
    return <div>加载中...</div>;
  }

  return (
    <div>
      <h1>
        {data.name} ({data.stockCode})
      </h1>
      <div
        style={{
          display: "flex",
          alignItems: "center",
          color: Number(data.change) == 0 ? "black" : Number(data.change) > 0 ? "red" : "green",
          fontSize: "20px",
        }}
      >
        <p style={{ margin: "0 0 10px", fontSize: "40px", fontWeight: "bold" }}>{data.current}</p>
        <p style={{ margin: "0 10px" }}>{data.change}</p>
        <p>{data.percent}%</p>
      </div>
      <Row style={{ marginBottom: "20px" }}>
        <Col span={6}>开盘价: {data.open}</Col>
        <Col span={6}>最高价: {data.high}</Col>
        <Col span={6}>最低价: {data.low}</Col>

        <Col span={6}>发行日期: {new Date(parseInt(data.issueDate)).toLocaleDateString("en-CA")}</Col>
        <Col span={6}>货币: {data.currency}</Col>
        <Col span={6}>交易所: {data.stockExchange}</Col>
        <Col span={6}>均价: {data.avgPrice}</Col>

        {!Number.isNaN(data?.floatMarketCapital) && (
          <Col span={6}>流通市值: {formatNumber(data.floatMarketCapital)}</Col>
        )}
        <Col span={6}>总市值: {formatNumber(data.marketCapital)}</Col>
        {!Number.isNaN(data?.floatShares) && <Col span={6}>流通股本: {formatNumber(data.floatShares)}</Col>}
        <Col span={6}>总股本: {formatNumber(data.totalShares)}</Col>
        <Col span={6}>成交金额: {formatNumber(data.amount)}</Col>
        <Col span={6}>成交量: {formatNumber(data.volume)}</Col>
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
