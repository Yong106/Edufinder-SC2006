import CONSTANTS from 'src/constants.ts';

const fetchSavedSchoolIds = async () => {
  const res = await fetch(CONSTANTS.backendEndpoint + '/users/saved-schools', {
    method: 'GET',
    credentials: 'include',
  });

  if (!res.ok) throw new Error('Failed to fetch saved schools');

  const data = await res.json();
  return data.savedSchoolIds;
}

export default fetchSavedSchoolIds;