import TopSchools from 'src/components/dashboard/TopSchools.tsx';
import Header from 'src/layouts/full/header/Header';
import { useSchoolContext } from 'src/context/SchoolProvider.tsx';
import { useEffect, useMemo, useState } from 'react';
import { School } from 'src/types/school/school.ts';

const Dashboard = () => {

  const { schoolMap, isLoading } = useSchoolContext();
  const [ schools, setSchools ] = useState<School[]>([]);
  const [ searchValue, setSearchValue ] = useState('');
  const [ filteredSchools, setFilteredSchools] = useState<School[]>([]);

  const [isFilterOpen, setFilterOpen] = useState(false);
  const [isLocOpen, setLocOpen] = useState(false);
  const [locQuery, setLocQuery] = useState('');
  const [selectedLocations, setSelectedLocations] = useState<string[]>([]);
  const [isCCAOpen, setCCAOpen] = useState(false);
  const [ccaQuery, setCCAQuery] = useState('');
  const [selectedCCAs, setSelectedCCAs] = useState<string[]>([]);
  const [isSubjectOpen, setSubjectOpen] = useState(false);
  const [subjectQuery, setSubjectQuery] = useState('');
  const [selectedSubjects, setSelectedSubjects] = useState<string[]>([]);
  const [isNatureCodeOpen, setNatureCodeOpen] = useState(false);
  const [selectedNatureCodes, setSelectedNatureCodes] = useState<string[]>([]);
  const [isSchoolTypeOpen, setSchoolTypeOpen] = useState(false);
  const [selectedSchoolTypes, setSelectedSchoolTypes] = useState<string[]>([]);
  const [isSessionCodeOpen, setSessionCodeOpen] = useState(false);
  const [selectedSessionCodes, setSelectedSessionCodes] = useState<string[]>([]);
  const [minCOP, setMinCOP] = useState('4');
  const [maxCOP, setMaxCOP] = useState('32');

  if (isLoading) return <div>Loading...</div>;

  useEffect(() => {
    console.log("School Map loaded");
    if (schoolMap.size > 0) {
      setSchools(Array.from(schoolMap.values()));
    }
  }, [schoolMap]);

  useEffect(() => {
    for (const school of schools) {
      if (!school) {
        console.error('Null school:', school);
      } else if (school.name == null) {
        console.error('School with null name:', school);
      }
    }
    console.log("Search value:", searchValue);
    if (schools.length > 0 ) {
      console.log("xyz", schools);
      setFilteredSchools(
        schools.filter((school) => {
          const name = school?.name ?? '';
          const search = searchValue ?? '';
          return name.toLowerCase().includes(search.toLowerCase());
        })
      );
    }
  }, [searchValue, schools]);

  return (
    <>
      <Header
        searchValue={searchValue}
        setSearchValue={setSearchValue}

        selectedLocations={selectedLocations}
        setSelectedLocations={setSelectedLocations}
        locQuery={locQuery}
        setLocQuery={setLocQuery}
        isLocOpen={isLocOpen}
        setLocOpen={setLocOpen}

        selectedCCAs={selectedCCAs}
        setSelectedCCAs={setSelectedCCAs}
        ccaQuery={ccaQuery}
        setCCAQuery={setCCAQuery}
        isCCAOpen={isCCAOpen}
        setCCAOpen={setCCAOpen}

        selectedSubjects={selectedSubjects}
        setSelectedSubjects={setSelectedSubjects}
        subjectQuery={subjectQuery}
        setSubjectQuery={setSubjectQuery}
        isSubjectOpen={isSubjectOpen}
        setSubjectOpen={setSubjectOpen}

        selectedNatureCodes={selectedNatureCodes}
        setSelectedNatureCodes={setSelectedNatureCodes}
        isNatureCodeOpen={isNatureCodeOpen}
        setNatureCodeOpen={setNatureCodeOpen}

        selectedSchoolTypes={selectedSchoolTypes}
        setSelectedSchoolTypes={setSelectedSchoolTypes}
        isSchoolTypeOpen={isSchoolTypeOpen}
        setSchoolTypeOpen={setSchoolTypeOpen}

        selectedSessionCodes={selectedSessionCodes}
        setSelectedSessionCodes={setSelectedSessionCodes}
        isSessionCodeOpen={isSessionCodeOpen}
        setSessionCodeOpen={setSessionCodeOpen}

        minCOP={minCOP}
        setMinCOP={setMinCOP}
        maxCOP={maxCOP}
        setMaxCOP={setMaxCOP}

        isFilterOpen={isFilterOpen}
        setFilterOpen={setFilterOpen}

        schools={schools}

        filteredSchools={filteredSchools}
        setFilteredSchools={setFilteredSchools}
      />
      <div className="lg:col-span-8 col-span-12 pt-8">
        <TopSchools schools={filteredSchools} />
      </div>
    </>
  );
};

export default Dashboard;
