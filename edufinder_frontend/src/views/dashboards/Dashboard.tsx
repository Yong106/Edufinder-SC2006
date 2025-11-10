import TopSchools from 'src/components/dashboard/TopSchools.tsx';
import Header from 'src/layouts/full/header/Header';
import { useSchoolContext } from 'src/context/SchoolProvider.tsx';
import { useEffect, useState } from 'react';
import { School } from 'src/types/school/school.ts';

const filterSchools = (
  schools: School[],
  searchValue: string,
  selectedLocations: string[],
  selectedCCAs: string[],
  selectedSubjects: string[],
  selectedNatureCodes: string[],
  selectedSchoolTypes: string[],
  selectedSessionCodes: string[],
  minCOP: number,
  maxCOP: number
): School[] => {
  return schools.filter((school) => {
    const nameMatch = school.name?.toLowerCase().includes(searchValue.toLowerCase());
    const locMatch = selectedLocations.length === 0 || selectedLocations.includes(school.location); // ✅ use school.location
    const ccaMatch = selectedCCAs.length === 0 || selectedCCAs.some(cca => school.ccas?.includes(cca));
    const subjectMatch = selectedSubjects.length === 0 || selectedSubjects.some(sub => school.subjects?.includes(sub));
    const natureMatch = selectedNatureCodes.length === 0 || selectedNatureCodes.includes(school.natureCode);
    const typeMatch = selectedSchoolTypes.length === 0 || selectedSchoolTypes.includes(school.schoolType);
    const sessionMatch = selectedSessionCodes.length === 0 || selectedSessionCodes.includes(school.sessionCode);
    const copMatch = !school.minCutOffPoint || !school.maxCutOffPoint ||
      (school.minCutOffPoint >= minCOP && school.maxCutOffPoint <= maxCOP);

    return nameMatch && locMatch && ccaMatch && subjectMatch && natureMatch && typeMatch && sessionMatch && copMatch;
  });
};

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
    if (schoolMap.size > 0) {
      const allSchools = Array.from(schoolMap.values());
      setSchools(allSchools);

      const min = parseInt(minCOP, 10) || 4;
      const max = parseInt(maxCOP, 10) || 32;

      const filtered = filterSchools(
        allSchools,
        searchValue,
        selectedLocations,
        selectedCCAs,
        selectedSubjects,
        selectedNatureCodes,
        selectedSchoolTypes,
        selectedSessionCodes,
        min,
        max
      );

      setFilteredSchools(filtered);
    }
  }, [
    schoolMap,
    searchValue,
    selectedLocations,
    selectedCCAs,
    selectedSubjects,
    selectedNatureCodes,
    selectedSchoolTypes,
    selectedSessionCodes,
    minCOP,
    maxCOP
  ]);

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
