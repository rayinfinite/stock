import {createContext, ReactNode, useContext, useLayoutEffect, useState,} from "react";

interface ViewportContextType {
  width: number;
  height: number;
}

const ViewportContext = createContext<ViewportContextType | undefined>(
  undefined
);

export const ViewportProvider = ({ children }: { children: ReactNode }) => {
  const [width, setWidth] = useState(window.innerWidth);
  const [height, setHeight] = useState(window.innerHeight);

  const handleWindowResize = () => {
    setWidth(window.innerWidth);
    setHeight(window.innerHeight);
  };

  useLayoutEffect(() => {
    window.addEventListener("resize", handleWindowResize);
    return () => window.removeEventListener("resize", handleWindowResize);
  }, []);

  return (
    <ViewportContext.Provider value={{ width, height }}>
      {children}
    </ViewportContext.Provider>
  );
};

export const useViewport = () => {
  return useContext(ViewportContext);
};
