import { useEffect, useState } from 'react';
import CONSTANTS from 'src/constants.ts';
import toast from 'react-hot-toast';
import TopSchools from 'src/components/dashboard/TopSchools.tsx';
import { useSchoolContext} from 'src/context/SchoolProvider.tsx';

const SavedSchools = () => {

  const { schoolMap, isLoading } = useSchoolContext();
  const [savedSchoolIds, setSavedSchoolIds] = useState<number[]>([]);
  const [savedSchoolLoading, setSavedSchoolLoading] = useState(true);

  useEffect(() => {
    const fetchSavedSchools = async () => {
      try {
        const res = await fetch(CONSTANTS.backendEndpoint + '/users/saved-schools', {
          method: 'GET',
          credentials: 'include',
        });

        if (!res.ok) throw new Error('Failed to fetch saved schools');

        const data = await res.json();
        console.log("Saved schools: ", data);
        setSavedSchoolIds(data.savedSchoolIds);
      } catch (err) {
        console.error('Error fetching saved schools', err);
        toast.error('Failed to load saved schools');
      } finally {
        setSavedSchoolLoading(false);
      }
    };

    fetchSavedSchools();
  }, []);

  const savedSchools = savedSchoolIds
    .map(id => schoolMap[id])
    .filter(Boolean);

  return (
    <>
      <h1 className="font-semibold text-4xl pb-5 text-primary">Saved Schools</h1>
      <div className="lg:col-span-8 col-span-12 pt-8">

        {
          savedSchoolLoading || isLoading  ? (
            <p>Loading...</p>
          ) : (
            <TopSchools schools={savedSchools} />
          )
        }
      </div>
    </>
  );
};

export default SavedSchools;