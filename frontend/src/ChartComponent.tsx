import React, { useRef, useEffect, useState } from 'react';
import { Button, Radio } from 'antd';
import { createChart } from "lightweight-charts";
import { getStockData } from './api';

const ChartComponent = () => {
  const chartContainerRef = useRef(null);
  const [chartType, setChartType] = useState('Candlestick');
  const [timePeriod, setTimePeriod] = useState('D');
  const [data, setData] = useState([]);

  useEffect(() => {
    if (chartContainerRef.current) {
      const chart = createChart(chartContainerRef.current, {
        width: 800,
        height: 400,
        layout: {
          backgroundColor: '#ffffff',
          textColor: '#333',
        },
        grid: {
          vertLines: {
            color: '#eee',
          },
          horzLines: {
            color: '#eee',
          },
        },
        // crosshair: {
        //   mode: LightweightCharts.CrosshairMode.Normal,
        // },
        priceScale: {
          borderColor: '#ddd',
        },
        timeScale: {
          borderColor: '#ddd',
        },
      });

      let series;
      if (chartType === 'Candlestick') {
        series = chart.addCandlestickSeries();
      } else {
        series = chart.addAreaSeries({
          topColor: 'rgba(59, 150, 255, 0.4)',
          bottomColor: 'rgba(59, 150, 255, 0)',
          lineColor: 'rgba(59, 150, 255, 1)',
        });
      }

      // const filteredData = filterDataByTimePeriod(data, timePeriod);
      getData(series);

      return () => {
        chart.remove();
      };
    }
  }, [data, chartType, timePeriod]);


  const getData = async (series) => {
    const stockData = await getStockData("000001");
    const updatedData = stockData.map(item => {
      const time = new Date(item.date);
      return {
        ...item,
        time: time.getTime() / 1000,
      };
    });
    series.setData(updatedData);
  };

  const filterDataByTimePeriod = (data, period) => {
    // 这里可以根据实际需求对数据进行过滤
    // 目前仅返回原始数据
    return data;
  };

  return (
    <div>
      <div ref={chartContainerRef} />
      <Radio.Group defaultValue="Candlestick" buttonStyle="solid" onChange={(e) => setChartType(e.target.value)}>
        <Radio.Button value="Candlestick">K线图</Radio.Button>
        <Radio.Button value="Area">面积图</Radio.Button>
      </Radio.Group>
      <Radio.Group defaultValue="D" buttonStyle="solid" onChange={(e) => setTimePeriod(e.target.value)} style={{ marginLeft: 16 }}>
        <Radio.Button value="D">日线</Radio.Button>
        <Radio.Button value="W">周线</Radio.Button>
        <Radio.Button value="M">月线</Radio.Button>
      </Radio.Group>
    </div>
  );
};

export default ChartComponent;
