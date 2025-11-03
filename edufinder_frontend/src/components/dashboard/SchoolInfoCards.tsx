import CardBox from 'src/components/shared/CardBox.tsx';
import { Table, TableBody, TableCell, TableRow } from 'flowbite-react';
import { Cca, CcaType, School } from 'src/types/school/school.ts';

const SchoolInfoCards = ({school}: {school: School}) => {

  const tags = [
    school.sapInd && "SAP",
    school.autonomousInd && "Autonomous",
    school.giftedInd && "Gifted",
    school.ipInd && "IP"
  ].filter(Boolean).join(" / ");

  const motherTongues = [
    school.motherTongue1,
    school.motherTongue2,
    school.motherTongue3
  ].filter(Boolean).join(", ");

  const groupCcas = (ccas: Cca[]) => {
    return ccas.reduce<Record<CcaType, string[]>>((acc, cca) => {
      if (!acc[cca.type]) acc[cca.type] = [];
      acc[cca.type].push(cca.name);
      return acc;
    }, {
      'PHYSICAL SPORTS': [],
      'CLUBS AND SOCIETIES': [],
      'VISUAL AND PERFORMING ARTS': [],
      'UNIFORMED GROUPS': [],
      'OTHERS': [],
    });
  };

  const ccaLabels: Record<CcaType, string> = {
    'PHYSICAL SPORTS': 'Sports',
    'CLUBS AND SOCIETIES': 'Clubs & Societies',
    'VISUAL AND PERFORMING ARTS': 'Performing Arts',
    'UNIFORMED GROUPS': 'Uniformed Groups',
    'OTHERS': 'Others'
  };

  const groupedCcas = groupCcas(school.ccas);

  return (
    <div>
      {/* General Information */}
      <CardBox>
        <h5>General Information</h5>
        <div className="overflow-x-auto overflow-y-hidden">
          <Table className="">
            <TableBody className="border-0">
              <TableRow>
                <TableCell className="text-20 font-semibold  py-3 whitespace-nowrap">Location</TableCell>
                <TableCell className="whitespace-nowrap">
                  <div className="flex gap-4 items-center">
                    <div className="w-full md:pe-0 pe-10">
                      <p className="text-ld text-sm  font-medium">{school.address}</p>
                    </div>
                  </div>
                </TableCell>
              </TableRow>
              <TableRow>
                <TableCell className="text-20 font-semibold  py-3 whitespace-nowrap">Website</TableCell>
                <TableCell className="whitespace-nowrap">
                  <div className="flex gap-4 items-center">
                    <div className="w-full md:pe-0 pe-10">
                      <p className="text-ld text-sm  font-medium">{school.website}</p>
                    </div>
                  </div>
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </div>
      </CardBox>
      {/* School Details */}
      <CardBox>
        <h5>Details</h5>
        <div className="overflow-x-auto overflow-y-hidden">
          <Table className="">
            <TableBody className="border-0">
              {/* School Type */}
              <TableRow>
                <TableCell className="text-20 font-semibold  py-3 whitespace-nowrap">Type</TableCell>
                <TableCell className="whitespace-nowrap">
                  <div className="flex gap-4 items-center">
                    <div className="w-full md:pe-0 pe-10">
                      <p className="text-ld text-sm  font-medium">{school.type}</p>
                    </div>
                  </div>
                </TableCell>
              </TableRow>
              {/* School Nature */}
              <TableRow>
                <TableCell className="text-20 font-semibold  py-3 whitespace-nowrap">Nature</TableCell>
                <TableCell className="whitespace-nowrap">
                  <div className="flex gap-4 items-center">
                    <div className="w-full md:pe-0 pe-10">
                      <p className="text-ld text-sm  font-medium">{school.natureCode}</p>
                    </div>
                  </div>
                </TableCell>
              </TableRow>
              {/*} SAP / Autonomous / Gifted / Independent */}
              <TableRow>
                <TableCell className="text-20 font-semibold  py-3 whitespace-nowrap">Tags</TableCell>
                <TableCell className="whitespace-nowrap">
                  <div className="flex gap-4 items-center">
                    <div className="w-full md:pe-0 pe-10">
                      <p className="text-ld text-sm  font-medium">{tags}</p>
                    </div>
                  </div>
                </TableCell>
              </TableRow>
              {/* Mother Tongues Offered */}
              <TableRow>
                <TableCell className="text-20 font-semibold  py-3 whitespace-nowrap">Mother Tongues</TableCell>
                <TableCell className="whitespace-nowrap">
                  <div className="flex gap-4 items-center">
                    <div className="w-full md:pe-0 pe-10">
                      <p className="text-ld text-sm  font-medium">{motherTongues}</p>
                    </div>
                  </div>
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </div>
      </CardBox>
      {/* Programmes */}
      <CardBox>
        <h5>General Information</h5>
        <div className="overflow-x-auto overflow-y-hidden">
          <Table className="">
            <TableBody className="border-0">
              <TableRow>
                <TableCell className="text-20 font-semibold  py-3 whitespace-nowrap">CCAs</TableCell>
                <TableCell className="whitespace-nowrap">
                  <div className="flex gap-4 items-center">
                    <div className="w-full md:pe-0 pe-10">
                      <p className="text-ld text-sm  font-medium">{school.address}</p>
                      <ul>
                        {
                          Object.entries(groupedCcas).map(([type, items]) =>
                              items.length > 0 ? (
                                <li key={type}>
                                  <strong>{ccaLabels[type as CcaType]}:</strong>{" "}
                                  <em>{items.join(", ")}</em>
                                </li>
                              ) : null
                            )
                        }
                      </ul>
                    </div>
                  </div>
                </TableCell>
              </TableRow>
              <TableRow>
                <TableCell className="text-20 font-semibold  py-3 whitespace-nowrap">Special Programmes</TableCell>
                <TableCell className="whitespace-nowrap">
                  <div className="flex gap-4 items-center">
                    <div className="w-full md:pe-0 pe-10">
                      <p className="text-ld text-sm  font-medium">{school.programmes.join(", ")}</p>
                    </div>
                  </div>
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </div>
      </CardBox>
    </div>
  )
}

export default SchoolInfoCards;