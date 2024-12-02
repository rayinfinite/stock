export const ROOT_PATH = "http://localhost:9000";

export async function getStockData(stockCode: string) {
  const params = new URLSearchParams({ stockCode }).toString();
  return await fetch(`${ROOT_PATH}?${params}`)
    .then((response) => response.json())
    .then((data) => {
      return data.data;
    })
    .catch((error) => {
      console.error("Error:", error);
      return [];
    });
}
