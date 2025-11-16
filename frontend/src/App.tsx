import { RouterProvider } from "react-router";
import { ThemeModeScript, ThemeProvider } from 'flowbite-react';
import customTheme from './utils/theme/custom-theme';
import router from "./routes/Router";
import { SchoolProvider } from "./context/SchoolProvider";
import { APIProvider } from '@vis.gl/react-google-maps';
import { Toaster } from 'react-hot-toast';
import { AuthProvider } from 'src/context/AuthProvider.tsx';

function App() {

  return (
    <>
      <ThemeModeScript />
      <ThemeProvider theme={customTheme}>
        <SchoolProvider>
          <APIProvider apiKey={import.meta.env.VITE_GOOGLE_MAPS_API_KEY} onLoad={() => console.log('Maps API has loaded.')}>
            <AuthProvider>
                <Toaster position="top-center"/>
                <RouterProvider router={router} />
            </AuthProvider>
          </APIProvider>
        </SchoolProvider>
      </ThemeProvider>
    </>
  );
}

export default App;
