import {useEffect, useState} from "react";
import {getMarketDepthData} from "../api";

interface MarketDepthProps {
  stockCode: string;
}

const MarketDepth: React.FC<MarketDepthProps> = ({ stockCode }) => {
  const [data, setData] = useState<any>(null);
  useEffect(() => {
    setData(undefined);
    const fetchData = async () => {
      const data = await getMarketDepthData(stockCode);
      if (data.timestamp && data.timestamp !== "0") {
        setData(data);
      }
    };
    fetchData();

    const intervalId = setInterval(fetchData, 3000);
    return () => clearInterval(intervalId);
  }, [stockCode]);

  if (!data) {
    return null;
  }
  if (data.buyVolumes[0] == "0") {
    return null;
  }

  const cumulativeBuyVolumes = data.buyVolumes.reduce((acc: number[], volume: number, index: number) => {
    acc.push((acc[index - 1] || 0) + volume);
    return acc;
  }, []);

  const cumulativeSellVolumes = data.sellVolumes.reduce((acc: number[], volume: number, index: number) => {
    acc.push((acc[index - 1] || 0) + volume);
    return acc;
  }, []);

  const maxVolume = Math.max(
    cumulativeBuyVolumes[cumulativeBuyVolumes.length - 1],
    cumulativeSellVolumes[cumulativeSellVolumes.length - 1]
  );
  return (
    <div style={{ minWidth: "200px" }}>
      <h2>五档盘口</h2>
      <p>现价: {data.price}</p>
      <div>
        {data.sellPrices
          .slice()
          .reverse()
          .map((price: string, index: number) => (
            <div key={index} style={{ display: "flex", justifyContent: "space-between", maxWidth: 200 }}>
              <span>
                Sell {data.sellPrices.length - index}: {round2decimal(price)}
              </span>
              <span>{data.sellVolumes[data.sellVolumes.length - 1 - index]}</span>
              <div
                style={{
                  position: "absolute",
                  width: `${(cumulativeSellVolumes[data.sellVolumes.length - 1 - index] / maxVolume) * 200}px`,
                  height: "18px",
                  backgroundColor: "rgba(255, 0, 0, 0.3)",
                  zIndex: -1,
                }}
              />
            </div>
          ))}
      </div>
      <div>
        {data.buyPrices.map((price: string, index: number) => (
          <div key={index} style={{ display: "flex", justifyContent: "space-between", maxWidth: 200 }}>
            <span>
              Buy {index + 1}: {round2decimal(price)}
            </span>
            <span>{data.buyVolumes[index]}</span>
            <div
              style={{
                position: "absolute",
                width: `${(cumulativeBuyVolumes[index] / maxVolume) * 200}px`,
                height: "18px",
                backgroundColor: "rgba(0, 255, 0, 0.3)",
                zIndex: -1,
              }}
            />
          </div>
        ))}
      </div>

      <div>
        <p>买方占比: {data.buyPct}%</p>
        <p>卖方占比: {data.sellPct}%</p>
      </div>
    </div>
  );
};

export default MarketDepth;

const round2decimal = (num: string): string => {
  const roundedNum = Math.round(Number(num) * 100) / 100;
  return roundedNum.toFixed(2);
};
