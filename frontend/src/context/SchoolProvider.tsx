import { School } from 'src/types/school/school.ts';
import { createContext, ReactNode, useContext, useEffect, useState } from 'react';
import CONSTANTS from 'src/constants.ts';

interface SchoolContextType {
  schoolMap: Map<number, School> | null;
  isLoading: boolean;
}

const SchoolContext = createContext<SchoolContextType>({
  schoolMap: null,
  isLoading: true,
});

export const useSchoolContext = () => useContext(SchoolContext);

export const SchoolProvider = ({ children }: { children: ReactNode }) => {
  const [schoolMap, setSchoolMap] = useState<Map<number, School> | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  console.log("SchoolProvider mounted");

  const fetchers = async () => {
    console.log("Fetching schools...");
    const res = await fetch(CONSTANTS.backendEndpoint + '/schools', {
      method: 'GET',
    });

    const data = await res.json();
    console.log("Data:", data);

    const map = new Map<number, School>();
    data.schools.forEach((school: School) => {map.set(school.id, school);});
    console.log("School Map: ", map);
    setSchoolMap(map);
  }

  useEffect(() => {
    fetchers()
        .then(() => setIsLoading(false));
  }, []);

  return (
    <SchoolContext.Provider value={{ schoolMap, isLoading}}>
      {children}
    </SchoolContext.Provider>
  )
}