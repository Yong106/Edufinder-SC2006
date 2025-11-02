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

  useEffect(() => {
    fetch(CONSTANTS.backendEndpoint + '/schools/')
    .then(res => res.json())
    .then((data: School[]) => {
      const map = new Map<number, School>();
      data.forEach(school => {map.set(school.id, school);});
      setSchoolMap(map);
    })
    .catch(err => console.error("Failed to load schools: " + err))
    .finally(() => setIsLoading(false));
  }, []);

  return (
    <SchoolContext.Provider value={{ schoolMap, isLoading}}>
      {children}
    </SchoolContext.Provider>
  )
}