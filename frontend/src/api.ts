export const ROOT_PATH = "http://localhost:9000";

export async function getStockData(stockCode: string, period: string) {
  const params = new URLSearchParams({ stockCode, period }).toString();
  return await fetch(`${ROOT_PATH}?${params}`)
    .then((response) => response.json())
    .then((data) => {
      const stockData = data.data;
      const formattedData = stockData.map((item) => {
        return {
          ...item,
          timestamp: Number(item.id.timestamp),
          open: Number(item.open),
          close: Number(item.close),
          high: Number(item.high),
          low: Number(item.low),
          volume: Number(item.volume),
          turnover: Number(item.turnover),
        };
      });
      return formattedData;
    })
    .catch((error) => {
      console.error("Error:", error);
      return [];
    });
}

interface MarketDepthType {
  timestamp: string;
  stockCode: string;
  price: string;
  buyPrices: string[];
  buyVolumes: string[];
  sellPrices: string[];
  sellVolumes: string[];
  buyPct: string;
  sellPct: string;
}
export async function getMarketDepthData(stockCode: string) {
  const params = new URLSearchParams({ stockCode }).toString();
  return await fetch(`${ROOT_PATH}/marketDepth?${params}`)
    .then((response) => response.json())
    .then((data) => {
      const stockData: MarketDepthType = data.data;
      const convertedStockData = {
        ...stockData,
        buyPrices: stockData.buyPrices.map((price: string) => parseFloat(price)),
        buyVolumes: stockData.buyVolumes.map((volume: string) => parseFloat(volume)),
        sellPrices: stockData.sellPrices.map((price: string) => parseFloat(price)),
        sellVolumes: stockData.sellVolumes.map((volume: string) => parseFloat(volume)),
      };
      return convertedStockData;
    })
    .catch((error) => {
      console.error("Error:", error);
      return [];
    });
}

export interface StockInfoType {
  stockCode: string;
  issueDate: string;
  currency: string;
  name: string;
  stockExchange: string;
  percent: number;
  change: number;
  avgPrice: number;
  current: number;
  open: number;
  high: number;
  low: number;
  floatMarketCapital: number;
  marketCapital: number;
  floatShares: number;
  totalShares: number;
  amount: number;
  volume: number;
}

export async function getStockInfo(stockCode: string) {
  const params = new URLSearchParams({ stockCode }).toString();
  return await fetch(`${ROOT_PATH}/stockInfo?${params}`)
    .then((response) => response.json())
    .then((data) => {
      const stockInfo = data.data;
      const convertedStockInfo: StockInfoType = {
        stockCode: stockInfo.stockCode,
        issueDate: stockInfo.issueDate,
        currency: stockInfo.currency,
        name: stockInfo.name,
        stockExchange: stockInfo.stockExchange,
        percent: parseFloat(stockInfo.percent),
        change: parseFloat(stockInfo.change),
        avgPrice: parseFloat(stockInfo.avgPrice),
        current: parseFloat(stockInfo.current),
        open: parseFloat(stockInfo.open),
        high: parseFloat(stockInfo.high),
        low: parseFloat(stockInfo.low),
        floatMarketCapital: parseFloat(stockInfo.floatMarketCapital),
        marketCapital: parseFloat(stockInfo.marketCapital),
        floatShares: parseFloat(stockInfo.floatShares),
        totalShares: parseFloat(stockInfo.totalShares),
        amount: parseFloat(stockInfo.amount),
        volume: parseFloat(stockInfo.volume),
      };
      return convertedStockInfo;
    })
    .catch((error) => {
      console.error("Error:", error);
      return [];
    });
}

export async function getTickTradeData(stockCode: string) {
  const params = new URLSearchParams({ stockCode }).toString();
  return await fetch(`${ROOT_PATH}/tickTrade?${params}`)
    .then((response) => response.json())
    .then((data) => {
      const tickData = data.data;
      return tickData.slice(-20);
    })
    .catch((error) => {
      console.error("Error:", error);
      return [];
    });
}
