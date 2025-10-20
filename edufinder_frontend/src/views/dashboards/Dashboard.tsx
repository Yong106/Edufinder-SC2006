import TopPayingClients from 'src/components/dashboard/TopPayingClients';
import Header from 'src/layouts/full/header/Header';

const Dashboard = () => {
  return (
    <>
      <Header/>
      <div className="lg:col-span-8 col-span-12 pt-8">
        <TopPayingClients />
      </div>
    </>
  );
};

export default Dashboard;
