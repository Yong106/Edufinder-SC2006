import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import TopSchools from 'src/components/dashboard/TopSchools.tsx';
import { useSchoolContext} from 'src/context/SchoolProvider.tsx';
import fetchSavedSchoolIds from 'src/utils/fetchSavedSchoolIds.ts';
import { useAuth } from 'src/context/AuthProvider.tsx';

const SavedSchools = () => {

  const { schoolMap, isLoading } = useSchoolContext();
  const { isLoggedIn } = useAuth();
  const [savedSchoolIds, setSavedSchoolIds] = useState<number[]>([]);
  const [savedSchoolLoading, setSavedSchoolLoading] = useState(true);

  useEffect(() => {
    const fetchSavedSchools = async () => {
      try {
        const res = await fetchSavedSchoolIds();
        setSavedSchoolIds(res);
      } catch (err) {
        console.error('Error fetching saved schools', err);
        toast.error('Failed to load saved schools');
      } finally {
        setSavedSchoolLoading(false);
      }
    };

    if (isLoggedIn) fetchSavedSchools();
  }, []);

  return (
    <>
      <h1 className="font-semibold text-4xl pb-5 text-primary">Saved Schools</h1>
      <div className="lg:col-span-8 col-span-12 pt-8">

        {
          !isLoggedIn ? (
            <p>Log in to use this functionality.</p>
            ) : (
            savedSchoolLoading || isLoading  ? (
              <p>Loading...</p>
            ) : (
              <TopSchools schools={savedSchoolIds.map(id => schoolMap.get(id)).filter(Boolean)} />
            )
          )
        }
      </div>
    </>
  );
};

export default SavedSchools;