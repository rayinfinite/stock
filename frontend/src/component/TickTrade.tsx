import {useEffect, useState} from "react";
import {getTickTradeData} from "../api";

interface MarketDepthProps {
  stockCode: string;
}

const TickTrade: React.FC<MarketDepthProps> = ({ stockCode }) => {
  const [data, setData] = useState<any>(null);
  useEffect(() => {
    setData(undefined);
    const fetchData = async () => {
      const data = await getTickTradeData(stockCode);
      if (data[0] && data[0].timestamp != "0") {
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

  return (
    <div>
      <h2>逐笔交易历史</h2>
      <div>
        {data.map((price, index) => (
          <div key={index} style={{ display: "flex", justifyContent: "space-between", maxWidth: 200 }}>
            <span>
              {new Date(parseInt(price.timestamp)).toLocaleTimeString(undefined, {
                hour: "2-digit",
                minute: "2-digit",
                second: "2-digit",
                hour12: false, // 确保使用24小时制
              })}
            </span>
            
            <span>{price.volume}</span>
            <span>{parseFloat(price.current).toFixed(2)}</span>
          </div>
        ))}
      </div>
    </div>
  );
};

export default TickTrade;
