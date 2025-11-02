import { RouterProvider } from "react-router";
import { ThemeModeScript, ThemeProvider } from 'flowbite-react';
import customTheme from './utils/theme/custom-theme';
import router from "./routes/Router";
import { SchoolProvider } from "./context/SchoolProvider";

function App() {

  return (
    <>
      <ThemeModeScript />
      <ThemeProvider theme={customTheme}>
        <SchoolProvider>
          <RouterProvider router={router} />
        </SchoolProvider>
      </ThemeProvider>
    </>
  );
}

export default App;
