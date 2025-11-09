import { useEffect, useState } from 'react';
import CONSTANTS from 'src/constants.ts';
import toast from 'react-hot-toast';
import TopSchools from 'src/components/dashboard/TopSchools.tsx';
import { useSchoolContext} from 'src/context/SchoolProvider.tsx';
import fetchSavedSchoolIds from 'src/utils/fetchSavedSchoolIds.ts';

const SavedSchools = () => {

  const { schoolMap, isLoading } = useSchoolContext();
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

    fetchSavedSchools();
  }, []);

  return (
    <>
      <h1 className="font-semibold text-4xl pb-5 text-primary">Saved Schools</h1>
      <div className="lg:col-span-8 col-span-12 pt-8">

        {
          savedSchoolLoading || isLoading  ? (
            <p>Loading...</p>
          ) : (
            <TopSchools schools={savedSchoolIds.map(id => schoolMap[id]).filter(Boolean)} />
          )
        }
      </div>
    </>
  );
};

export default SavedSchools;