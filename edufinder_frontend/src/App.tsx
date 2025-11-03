import { RouterProvider } from "react-router";
import { ThemeModeScript, ThemeProvider } from 'flowbite-react';
import customTheme from './utils/theme/custom-theme';
import router from "./routes/Router";
import { SchoolProvider } from "./context/SchoolProvider";
import { APIProvider } from '@vis.gl/react-google-maps';

function App() {

  return (
    <>
      <ThemeModeScript />
      <ThemeProvider theme={customTheme}>
        <SchoolProvider>
          <APIProvider apiKey={import.meta.env.VITE_GOOGLE_MAPS_API_KEY} onLoad={() => console.log('Maps API has loaded.')}>
            <RouterProvider router={router} />
          </APIProvider>
        </SchoolProvider>
      </ThemeProvider>
    </>
  );
}

export default App;
