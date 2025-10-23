import TopSchools from 'src/components/dashboard/TopSchools.tsx';

const SavedSchools = () => {
  return (
    <>
      <h1 className="font-semibold text-4xl pb-5">Saved Schools</h1>
      <div className="lg:col-span-8 col-span-12 pt-8">
        <TopSchools />
      </div>
    </>
  );
};

export default SavedSchools;