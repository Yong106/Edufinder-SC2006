import { FC } from 'react';
import { Outlet } from 'react-router';
import Sidebar from './sidebar/Sidebar';
const FullLayout: FC = () => {
  return (
    <>
        <div className="flex w-full ">
          <div className="page-wrapper flex w-full">
            <Sidebar />
            <div className="container flex flex-col w-full pt-6">
              <div className={`h-full`}>
                <div className={`w-full`}>
                  <div className="container px-0 py-6">
                    <Outlet />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
    </>
  );
};

export default FullLayout;
