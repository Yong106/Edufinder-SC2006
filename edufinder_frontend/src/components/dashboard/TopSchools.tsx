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
import { Link } from 'react-router';
import { useEffect, useState } from 'react';
import fetchSavedSchoolIds from 'src/utils/fetchSavedSchoolIds.ts';
import CONSTANTS from 'src/constants.ts';
import toast from 'react-hot-toast';



const TopSchools = ({
    schools,
    onToggleSave
  }: {
  schools: School[];
  onToggleSave?: (schoolId: number, isSaved: boolean) => void;
}) => {

  const [savedSchoolIds, setSavedSchoolIds] = useState<number[]>([]);

  console.log(schools);

  useEffect(() => {
    const fetchSavedSchools = async () => {
      try {
        const res = await fetchSavedSchoolIds();
        setSavedSchoolIds(res);
      } catch (err) {
        console.error('Error fetching saved schools', err);
        // toast.error('Failed to load saved schools');
      }
    };

    fetchSavedSchools();
  }, []);

  const handleToggleSave = async (schoolId: number) => {
    const isSaved = savedSchoolIds.includes(schoolId);

    try {
      const res = await fetch(CONSTANTS.backendEndpoint + '/users/saved-schools', {
        method: isSaved ? 'DELETE' : 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ schoolId }),
      })

      console.log(res);

      if (!res.ok) throw new Error('Failed to toggle save');

      setSavedSchoolIds((prev) =>
        isSaved ? prev.filter((id) => id !== schoolId) : [...prev, schoolId]
      );

      if (onToggleSave) {
        onToggleSave(schoolId, !isSaved);
      }

      toast.success(
        isSaved ? 'Saved removed from saved list' : 'School saved!',
      );
    } catch (err) {
      console.error('Error saving school', err);
      toast.error("Failed to toggle school save status.");
    }
  }

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
              {
                schools.length > 0 ? (
                  schools.map((item) => (
                    <TableRow key={item.id}>
                      <TableCell className="whitespace-nowrap">
                        <Link to={`/school/${item.id}`}>
                          <div className="flex gap-4 items-center">
                            <div className="w-full md:pe-0 pe-10">
                              <p className="text-ld text-sm  font-medium">{item.name}</p>
                            </div>
                          </div>
                        </Link>
                      </TableCell>
                      <TableCell className="whitespace-nowrap ">
                        <p className="text-sm text-ld font-medium flex items-center gap-2">
                          {item.address}
                        </p>
                      </TableCell>
                      <TableCell className="whitespace-nowrap">
                        <p className="text-ld text-sm  font-medium">{item.minCutOffPoint} - {item.maxCutOffPoint}</p>
                      </TableCell>
                      <TableCell className="whitespace-nowrap ">
                        <Button
                          size="sm"
                          color="light"
                          onClick={() => handleToggleSave(item.id)}
                          aria-label={`Save ${item.name}`}
                          className="!p-1 !h-auto !w-auto !border-0 !bg-transparent shadow-none"
                        >
                          <Icon icon={ savedSchoolIds.includes(item.id) ? "solar:bookmark-outline" : "solar:bookmark-bold" } width={16} height={16} />
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))
                ) : (
                  <TableRow>
                    <TableCell colSpan={4} className="text-center py-4 text-gray-500">
                      No schools found.
                    </TableCell>
                  </TableRow>
                )
              }
            </TableBody>
          </Table>
        </div>
      </CardBox>
    </>
  );
};

export default TopSchools;
