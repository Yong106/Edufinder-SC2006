import { MapCameraChangedEvent, Map, AdvancedMarker, Pin } from '@vis.gl/react-google-maps';
import { useEffect, useState } from 'react';

const LocationMap = ({postalCode}: {postalCode: number} ) => {

  const [coords, setCoords] = useState<{lat: number, lng: number} | null>(null);

  useEffect(() => {
    const geocode = async () => {
      const geocoder = new google.maps.Geocoder();

      const results = await geocoder.geocode(
        {
          address: postalCode.toString(),
          componentRestrictions: {country: 'SG'},
        },
      );

      if (!results.results[0]) {
        console.log("Geocoding failed: Location not found based on postal code");
      }

      const location = results.results[0].geometry.location;
      setCoords({
        lat: location.lat(),
        lng: location.lng(),
      })

    }

    geocode();
  }, [postalCode]);

  return (
    <div style={{ height: '500px', width: '100%' }}>
      {
        coords ? (
          <Map
            defaultZoom={11.5}
            defaultCenter={{ lat: 1.3521, lng: 103.8198 }}
            mapId={"e25348296667f63cc0a0e9f5"}
            onCameraChanged={(ev: MapCameraChangedEvent) =>
              console.log('camera changed:', ev.detail.center, 'zoom:', ev.detail.zoom)
            }
          >
            <AdvancedMarker position={coords}>
              <Pin background={'#FBBC04'} glyphColor={'#000'} borderColor={'#000'} />
            </AdvancedMarker>
          </Map>
        ) : (
          <p>Geocoding...</p>
        )
      }
    </div>
  )
}

export default LocationMap;