import { useMemo } from 'react';
import { School } from 'src/types/school/school.ts';

const useSchoolFilterOptions = (schools: School[]) => {
  return useMemo(() => {
    const locations = Array.from(new Set(schools.map(s => s.location ?? '').filter(Boolean))).sort();

    const CCAs = Array.from(
      new Set(
        schools.flatMap(s => s.ccas?.map(c => c.name) ?? [])
      )
    ).sort();

    const Subjects = Array.from(
      new Set(
        schools.flatMap(s => s.subjects ?? [])
      )
    ).sort();

    const NatureCodes = Array.from(
      new Set(schools.map(s => String(s.natureCode ?? '')).filter(Boolean))
    ).sort();

    const SchoolTypes = Array.from(
      new Set(schools.map(s => String(s.type ?? '')).filter(Boolean))
    ).sort();

    const SessionCodes = Array.from(
      new Set(schools.map(s => String(s.sessionCode ?? '')).filter(Boolean))
    ).sort();

    console.log('Schools length:', schools.length);
    console.log('Sample school:', schools[0]);

    console.log('Raw natureCodes:', schools.map(s => s.natureCode));
    console.log('Processed NatureCodes:', NatureCodes);

    console.log('Raw schoolTypes:', schools.map(s => s.type));
    console.log('Processed SchoolTypes:', SchoolTypes);

    console.log('Raw sessionCodes:', schools.map(s => s.sessionCode));
    console.log('Processed SessionCodes:', SessionCodes);

    return { locations, CCAs, Subjects, NatureCodes, SchoolTypes, SessionCodes };
  }, [schools]);
};

export default useSchoolFilterOptions;