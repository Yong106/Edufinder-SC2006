import { useEffect, useState } from 'react';
import { geocodePostalCode } from 'src/utils/geocode.ts';
import { TransportationModes } from 'src/types/direction/transportationModes.ts';
import { Button } from 'flowbite-react';
import {
  DirectionsRenderer,
  GoogleMap,
  Marker
} from '@react-google-maps/api';
import { useAuth } from 'src/context/AuthProvider.tsx';

const LocationMapWithDirections = ({ postalCode }: { postalCode: number }) => {
  const [schoolCoords, setSchoolCoords] = useState<{ lat: number; lng: number } | null>(null);
  const [userCoords, setUserCoords] = useState<{ lat: number; lng: number } | null>(null);
  const [transportationMode, setTransportationMode] = useState<TransportationModes | null>(null);
  const [travelTimes, setTravelTimes] = useState<Record<google.maps.TravelMode, string>>({
    DRIVING: '',
    WALKING: '',
    TRANSIT: '',
    BICYCLING: '',
  });
  const [routes, setRoutes] = useState<google.maps.DirectionsRoute[]>([]);
  const [selectedRouteIndex, setSelectedRouteIndex] = useState(0);
  const [directionsResult, setDirectionsResult] = useState<google.maps.DirectionsResult | null>(null);
  const { isLoggedIn } = useAuth();

  const handleSchoolGeocode = async () => {
    const geocodeResults = await geocodePostalCode(postalCode.toString());
    if (geocodeResults) setSchoolCoords(geocodeResults);
    else setSchoolCoords(null);
  };

  const handleUserGeocode = async () => {
    const userPostalCode = localStorage.getItem('postalCode');
    if (!userPostalCode) return setUserCoords(null);

    const geocodeResults = await geocodePostalCode(userPostalCode.toString());
    if (geocodeResults) setUserCoords(geocodeResults);
    else setUserCoords(null);
  };

  const handleTransportationModeChange = (mode: google.maps.TravelMode) => {
    setTransportationMode(mode);
    setSelectedRouteIndex(0);
  };

  const handleDirectionsCallback = (
    res: google.maps.DirectionsResult | null,
    status: google.maps.DirectionsStatus
  ) => {
    if (status === 'OK' && res?.routes.length) {
      setRoutes(res.routes);
      setDirectionsResult(res);
      setSelectedRouteIndex((prevIndex) =>
        prevIndex < res.routes.length ? prevIndex : 0
      );
    } else {
      console.error('Directions request failed:', res);
    }
  };

  const fetchTravelTime = (mode: google.maps.TravelMode) => {
    if (!userCoords || !schoolCoords) return;

    const service = new window.google.maps.DirectionsService();
    service.route(
      {
        origin: userCoords,
        destination: schoolCoords,
        travelMode: mode,
      },
      (result, status) => {
        if (status === 'OK' && result?.routes[0]?.legs[0]?.duration?.text) {
          setTravelTimes((prev) => ({
            ...prev,
            [mode]: result.routes[0].legs[0].duration!.text,
          }));
        } else {
          console.error(`Failed to fetch time for ${mode}:`, status);
        }
      }
    );
  };

  useEffect(() => {
    handleSchoolGeocode();
    handleUserGeocode();
  }, [postalCode]);

  useEffect(() => {
    if (userCoords && schoolCoords) {
      fetchTravelTime(google.maps.TravelMode.DRIVING);
      fetchTravelTime(google.maps.TravelMode.WALKING);
      fetchTravelTime(google.maps.TravelMode.TRANSIT);
      fetchTravelTime(google.maps.TravelMode.BICYCLING);
    }
  }, [userCoords, schoolCoords]);

  useEffect(() => {
    if (!userCoords || !schoolCoords || !transportationMode) return;

    const service = new google.maps.DirectionsService();
    service.route(
      {
        origin: userCoords,
        destination: schoolCoords,
        travelMode: transportationMode,
        provideRouteAlternatives: true,
      },
      (result, status) => {
        if (status === 'OK' && result.routes.length > 0) {
          setRoutes(result.routes);
          setSelectedRouteIndex(0);
          setDirectionsResult(result); // only if needed
        } else {
          console.error('Directions request failed:', status, result);
        }
      }
    );
  }, [userCoords, schoolCoords, transportationMode]);

  return (
    <div style={{ height: '600px', width: '100%' }}>
      {schoolCoords ? (
        <div>
          <GoogleMap
            mapContainerStyle={{ width: '100%', height: '500px' }}
            center={schoolCoords}
            zoom={12}
          >

            {routes.length > 0 && (
              <DirectionsRenderer
                directions={{
                  ...directionsResult,
                  routes: [routes[selectedRouteIndex]],
                }}
                options={{ preserveViewport: true }}
              />
            )}

            {schoolCoords && <Marker position={schoolCoords} />}
            {userCoords && <Marker position={userCoords} />}
          </GoogleMap>

          <div className="flex gap-2 mt-3">
            { !isLoggedIn || !localStorage.getItem('postalCode') || localStorage.getItem('postalCode').length === 0 ? <p>Login and update your postal code to calculate travel times.</p> : null }
            <Button className={ isLoggedIn && localStorage.getItem("postalCode") ? "bg-blue-700" : "bg-gray-400 hover:bg-gray-400 cursor-not-allowed"} onClick={() => handleTransportationModeChange(google.maps.TravelMode.DRIVING)}>Drive: {travelTimes.DRIVING}</Button>
            <Button className={ isLoggedIn && localStorage.getItem("postalCode") ? "bg-blue-700" : "bg-gray-400 hover:bg-gray-400 cursor-not-allowed"} onClick={() => handleTransportationModeChange(google.maps.TravelMode.BICYCLING)}>Cycle: {travelTimes.BICYCLING}</Button>
            <Button className={ isLoggedIn && localStorage.getItem("postalCode") ? "bg-blue-700" : "bg-gray-400 hover:bg-gray-400 cursor-not-allowed"} onClick={() => handleTransportationModeChange(google.maps.TravelMode.TRANSIT)}>Public Transport: {travelTimes.TRANSIT}</Button>
            <Button className={ isLoggedIn && localStorage.getItem("postalCode") ? "bg-blue-700" : "bg-gray-400 hover:bg-gray-400 cursor-not-allowed"} onClick={() => handleTransportationModeChange(google.maps.TravelMode.WALKING)}>Walk: {travelTimes.WALKING}</Button>
          </div>
          {routes.length > 1 && (
            <div className="flex gap-2 mt-3">
              {routes.map((route, idx) => {
                const duration = route.legs[0]?.duration?.text ?? 'N/A';
                return (
                  <Button
                    key={idx}
                    color={selectedRouteIndex === idx ? 'blue' : 'gray'}
                    onClick={() => setSelectedRouteIndex(idx)}
                  >
                    Route {idx + 1}: {duration}
                  </Button>
                );
              })}
            </div>
          )}
        </div>
      ) : (
        <p>Invalid school postal code.</p>
      )}
    </div>
  );
};

export default LocationMapWithDirections;