import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import TopSchools from 'src/components/dashboard/TopSchools.tsx';
import { useSchoolContext} from 'src/context/SchoolProvider.tsx';
import fetchSavedSchoolIds from 'src/utils/fetchSavedSchoolIds.ts';
import { useAuth } from 'src/context/AuthProvider.tsx';
import { School } from 'src/types/school/school.ts';

const SavedSchools = () => {

  const { schoolMap, isLoading } = useSchoolContext();
  const { isLoggedIn } = useAuth();
  const [savedSchoolIds, setSavedSchoolIds] = useState<number[]>([]);
  const [savedSchools, setSavedSchools] = useState<School[]>([]);
  const [savedSchoolLoading, setSavedSchoolLoading] = useState(true);

  useEffect(() => {
    const fetchSavedSchools = async () => {
      try {
        const res = await fetchSavedSchoolIds();
        setSavedSchoolIds(res);
        const schools = res
          .map((id) => schoolMap.get(id))
          .filter((s): s is School => Boolean(s));
        setSavedSchools(schools);
      } catch (err) {
        console.error('Error fetching saved schools', err);
        toast.error('Failed to load saved schools');
      } finally {
        setSavedSchoolLoading(false);
      }
    };

    if (isLoggedIn) fetchSavedSchools();
  }, [isLoggedIn, schoolMap]);

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
              <TopSchools
                schools={savedSchools}
                onToggleSave={(schoolId, nowSaved) => {
                  if (!nowSaved) {
                    setSavedSchools((prev) =>
                      prev.filter((s) => s.id !== schoolId)
                    );
                    setSavedSchoolIds((prev) =>
                      prev.filter((id) => id !== schoolId)
                    );
                  }
                }}
              />
            )
          )
        }
      </div>
    </>
  );
};

export default SavedSchools;