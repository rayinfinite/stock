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
          timestamp: Number(item.timestamp),
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
  const params = new URLSearchParams({ stockCode}).toString();
  return await fetch(`${ROOT_PATH}/marketDepth?${params}`)
    .then((response) => response.json())
    .then((data) => {
      const stockData:MarketDepthType = data.data;
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