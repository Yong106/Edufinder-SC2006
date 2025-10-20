import ProductCards from 'src/components/dashboard/ProductCards';
import ProductSales from 'src/components/dashboard/ProductSales';
import RecentTransactionCard from 'src/components/dashboard/RecentTransactions';
import SalesProfit from 'src/components/dashboard/SalesProfit';
import TopPayingClients from 'src/components/dashboard/TopPayingClients';
import TrafficDistribution from 'src/components/dashboard/TrafficDistribution';
import Header from 'src/layouts/full/header/Header';

const SavedSchools = () => {
  return (
    <>
      <h1 className="font-semibold text-4xl pb-5">Saved Schools</h1>
      <div className="lg:col-span-8 col-span-12 pt-8">
        <TopPayingClients />
      </div>
    </>
  );
};

export default SavedSchools;