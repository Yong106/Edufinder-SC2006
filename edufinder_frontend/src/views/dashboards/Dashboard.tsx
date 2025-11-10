import TopSchools from 'src/components/dashboard/TopSchools.tsx';
import Header from 'src/layouts/full/header/Header';
import { useSchoolContext } from 'src/context/SchoolProvider.tsx';
import { useEffect, useState } from 'react';
import { School } from 'src/types/school/school.ts';

const Dashboard = () => {

  const { schoolMap, isLoading } = useSchoolContext();
  const [ searchValue, setSearchValue ] = useState('');
  const [ filteredSchools, setFilteredSchools] = useState<School[]>([]);

  if (isLoading) return <div>Loading...</div>;

  useEffect(() => {
    console.log("School Map loaded");
  }, [schoolMap])

  useEffect(() => {
    const schools = Array.from(schoolMap.values());

    setFilteredSchools(
      schools.filter((school) =>
        school.name.toLowerCase().includes(searchValue.toLowerCase())
      )
    );



  }, [searchValue, schoolMap]); // ✅ depends on updated map

  return (
    <>
      <Header searchValue={searchValue} setSearchValue={setSearchValue} />
      <div className="lg:col-span-8 col-span-12 pt-8">
        <TopSchools schools={filteredSchools} />
      </div>
    </>
  );
};

export default Dashboard;
