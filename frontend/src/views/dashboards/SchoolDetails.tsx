import SchoolInfoCards from 'src/components/dashboard/SchoolInfoCards.tsx';
import { useParams } from 'react-router';
import { useSchoolContext } from 'src/context/SchoolProvider.tsx';

const SchoolDetails = () => {

  const { id } = useParams<{ id: string }>();
  const { schoolMap, isLoading } = useSchoolContext();

  const school = id && schoolMap ? schoolMap.get(Number(id)) : null;

  if (isLoading) return <div>Loading...</div>;

  if (!school) return <div>404 Not Found. Please check your url and try again.</div>;

  return (
    <>
      <h1 className="font-semibold text-4xl pb-5">{school.name}</h1>
      <div className="lg:col-span-8 col-span-12 pt-8">
        <SchoolInfoCards school={school} />
      </div>
    </>
  );
};

export default SchoolDetails;