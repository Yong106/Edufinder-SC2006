import TopSchools from 'src/components/dashboard/TopSchools.tsx';
import Header from 'src/layouts/full/header/Header';
import schoolData from '../../test.json';
import { School } from 'src/types/school/school.ts';

const Dashboard = () => {

  const schools: School[] = schoolData;

  return (
    <>
      <Header/>
      <div className="lg:col-span-8 col-span-12 pt-8">
        <TopSchools schools={schools} />
      </div>
    </>
  );
};

export default Dashboard;
