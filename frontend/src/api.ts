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
