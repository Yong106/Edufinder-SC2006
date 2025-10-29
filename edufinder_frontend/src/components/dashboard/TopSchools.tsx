import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeadCell,
  TableRow,
  Button,
} from 'flowbite-react';
import CardBox from 'src/components/shared/CardBox';
import { Icon } from '@iconify/react';
import { School } from 'src/types/school/school.ts';



const TopSchools = ({schools}: {schools: School[]}) => {

  return (
    <>
      <CardBox>
        <div className="overflow-x-auto overflow-y-hidden">
          <Table className="">
            <TableHead className="border-0">
              <TableHeadCell className="text-20 font-semibold  py-3 whitespace-nowrap">
                School Name
              </TableHeadCell>
              <TableHeadCell className="text-20 font-semibold  py-3 whitespace-nowrap">
                Location
              </TableHeadCell>
              <TableHeadCell className="text-20 font-semibold py-3 whitespace-nowrap">
                2024 Cut-Off Point
              </TableHeadCell>
              <TableHeadCell className="text-20 font-semibold whitespace-nowrap">
              </TableHeadCell>
            </TableHead>
            <TableBody className="">
              {schools.map((item, index) => (
                <TableRow key={index}>
                  <TableCell className="whitespace-nowrap">
                    <div className="flex gap-4 items-center">
                      <div className="w-full md:pe-0 pe-10">
                        <p className="text-ld text-sm  font-medium">{item.name}</p>
                      </div>
                    </div>
                  </TableCell>
                  <TableCell className="whitespace-nowrap ">
                    <p className="text-sm text-ld font-medium flex items-center gap-2">
                      {item.address}
                      <Button
                        size="sm"
                        color="light"
                        //onClick={() => handleSave(item.id)}
                        aria-label={`Save ${item.name}`}
                        className="!p-1 !h-auto !w-auto !border-0 !bg-transparent shadow-none"
                      >
                        <Icon icon="solar:map-point-wave-bold" width={16} height={16} />
                      </Button>
                    </p>
                  </TableCell>
                  <TableCell className="whitespace-nowrap">
                    <p className="text-ld text-sm  font-medium">{item.minCutOffPoint} - {item.maxCutOffPoint}</p>
                  </TableCell>
                  <TableCell className="whitespace-nowrap ">
                    <Button
                      size="sm"
                      color="light"
                      //onClick={() => handleSave(item.id)}
                      aria-label={`Save ${item.name}`}
                      className="!p-1 !h-auto !w-auto !border-0 !bg-transparent shadow-none"
                    > 
                      <Icon icon="solar:bookmark-bold" width={16} height={16} />
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      </CardBox>
    </>
  );
};

export default TopSchools;
