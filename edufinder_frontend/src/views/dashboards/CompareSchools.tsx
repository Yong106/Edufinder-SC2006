import ProductCards from 'src/components/dashboard/ProductCards';
import ProductSales from 'src/components/dashboard/ProductSales';
import RecentTransactionCard from 'src/components/dashboard/RecentTransactions';
import SalesProfit from 'src/components/dashboard/SalesProfit';
import TopSchools from 'src/components/dashboard/TopSchools.tsx';
import TrafficDistribution from 'src/components/dashboard/TrafficDistribution';

const CompareSchools = () => {
  return (
    <>
        <h1 className="font-semibold text-4xl pb-5">Compare Schools</h1>
      <div className="lg:col-span-8 col-span-12 pt-8">
        <TopSchools />
      </div>
      <div className="grid grid-cols-12 gap-6">
        <div className="lg:col-span-8 col-span-12">
          <SalesProfit />
        </div>
        <div className="lg:col-span-4 col-span-12">
          <div className="grid grid-cols-12 ">
            <div className="col-span-12 mb-6">
              <TrafficDistribution />
            </div>
            <div className="col-span-12">
              <ProductSales />
            </div>
          </div>
        </div>
        <div className="lg:col-span-4 col-span-12">
          <RecentTransactionCard />
        </div>
        <div className="col-span-12">
          <ProductCards />
        </div>
      </div>
    </>
  );
};

export default CompareSchools;