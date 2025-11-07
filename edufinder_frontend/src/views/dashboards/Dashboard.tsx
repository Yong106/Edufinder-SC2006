import TopSchools from 'src/components/dashboard/TopSchools.tsx';
import Header from 'src/layouts/full/header/Header';
import { useSchoolContext } from 'src/context/SchoolProvider.tsx';
import { useEffect } from 'react';

const Dashboard = () => {

  const { schoolMap, isLoading } = useSchoolContext();

  if (isLoading) return <div>Loading...</div>;

  useEffect(() => {
    console.log("School Map loaded");
  }, [schoolMap])

  const schools = Array.from(schoolMap.values());

  return (
    <>
      <Header/>
      <div className="lg:col-span-8 col-span-12 pt-8">
        <TopSchools schools={schools} />
      </div>
    </>
  );
};

export default Dashboard;
