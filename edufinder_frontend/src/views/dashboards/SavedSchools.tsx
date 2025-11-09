import { useEffect } from 'react';
import CONSTANTS from 'src/constants.ts';

const SavedSchools = () => {

  useEffect(() => {
    fetch(CONSTANTS.backendEndpoint + '/users/saved-schools', {
      method: 'GET',
      credentials: 'include',

    })
      .then(res => {
        console.log('Raw response:', res);
        return res.json();
      })
      .then(data => {
        console.log('Parsed data:', data);
        // Do something with the data here
      })
      .catch(err => {
        console.error('Fetch error:', err);
      });
  }, []);

  return (
    <>
      <h1 className="font-semibold text-4xl pb-5 text-primary">Saved Schools</h1>
      <div className="lg:col-span-8 col-span-12 pt-8">


      </div>
    </>
  );
};

export default SavedSchools;