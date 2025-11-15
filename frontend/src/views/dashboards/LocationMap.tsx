import { MapCameraChangedEvent, Map, AdvancedMarker, Pin } from '@vis.gl/react-google-maps';
import { useEffect, useState } from 'react';
import { geocodePostalCode } from 'src/utils/geocode.ts';
//import toast from 'react-hot-toast';

const LocationMap = ({postalCode}: {postalCode: number} ) => {

  const [coords, setCoords] = useState<{lat: number, lng: number} | null>(null);

  const handleGeocode = async () => {
    const geocodeResults = await geocodePostalCode(postalCode.toString());
    if (geocodeResults) setCoords((geocodeResults));
    else {
      setCoords(null);
      // toast.error("Invalid postal code");
      return;
    }
  }

  useEffect(() => {
    handleGeocode();
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
            {
              coords && (
                <AdvancedMarker position={coords}>
                  <Pin background={'#FBBC04'} glyphColor={'#000'} borderColor={'#000'} />
                </AdvancedMarker>
              )
            }

          </Map>
        ) : (
          <p>Invalid postal code.</p>
        )
      }
    </div>
  )
}

export default LocationMap;