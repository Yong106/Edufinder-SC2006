import { School } from 'src/types/school/school.ts';
import SchoolInfoCards from 'src/components/dashboard/SchoolInfoCards.tsx';

const SchoolDetails = ({school}: {school: School | null}) => {

  if (school === null) {
    //TODO: API call to backend
  }

  return (
    <>
      {school === null ? (

      )}
      <h1 className="font-semibold text-4xl pb-5">school.name</h1>
      <div className="lg:col-span-8 col-span-12 pt-8">
        <SchoolInfoCards school={school} />
      </div>
    </>
  );
};

export default SchoolDetails;