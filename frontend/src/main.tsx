import {StrictMode} from "react";
import {createRoot} from "react-dom/client";
import App from "./App.tsx";
import {ViewportProvider} from "./ViewportContext.tsx";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <ViewportProvider>
      <App />
    </ViewportProvider>
  </StrictMode>
);
