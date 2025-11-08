import toast from 'react-hot-toast';

export const geocodePostalCode = async (postalCode: string): Promise<{ lat: number; lng: number } | null> => {


    return new Promise((resolve) => {
      const geocoder = new google.maps.Geocoder();
      geocoder.geocode(
        {
          address: postalCode.toString(),
          componentRestrictions: {country: 'SG'},
        },
        (results, status) => {
          if (status === 'OK' && results && results[0]) {
            const formattedAddress = results[0].formatted_address;
            const postalCodeMatch = formattedAddress.match(/\b\d{6}\b/);

            if (postalCodeMatch && postalCodeMatch[0] === postalCode) {
              const location = results[0].geometry.location;
              toast.dismiss();
              resolve({ lat: location.lat(), lng: location.lng() });
            } else {
              // Matched result doesn't actually contain the requested postal code
              resolve(null);
            }
          } else {
            resolve(null);
          }
        }
      );

    })

}