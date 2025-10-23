import TopSchools from 'src/components/dashboard/TopSchools.tsx';
import Header from 'src/layouts/full/header/Header';

const Dashboard = () => {
  return (
    <>
      <Header/>
      <div className="lg:col-span-8 col-span-12 pt-8">
        <TopSchools />
      </div>
    </>
  );
};

export default Dashboard;
