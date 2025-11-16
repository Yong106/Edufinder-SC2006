import {ReactNode} from "react";
import CardBox from 'src/components/shared/CardBox.tsx';
import { Table, TableBody, TableCell, TableRow } from 'flowbite-react';
import { Cca, CcaType, School } from 'src/types/school/school.ts';
import CommentSection from 'src/views/comments/CommentSection.tsx';
import LocationMapWithDirections from 'src/views/dashboards/LocationMapWithDirections.tsx';

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
  ].filter(mt => !!mt && mt.toUpperCase() !== "NA")
    .join(", ");

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
        <SchoolInfoSection title="General Information" >
            <SchoolInfoRow title="Level">
                {school.level}
            </SchoolInfoRow>
            <SchoolInfoRow title="Type">
                {school.type}
            </SchoolInfoRow>
            <SchoolInfoRow title="Nature Code">
                {school.natureCode}
            </SchoolInfoRow>
            <SchoolInfoRow title="Session Code">
                {school.sessionCode}
            </SchoolInfoRow>
            <SchoolInfoRow title="Cut-off Points">
                {
                    school.minCutOffPoint && school.maxCutOffPoint
                    ? `${school.minCutOffPoint} – ${school.maxCutOffPoint}`
                    : "-"
                }
            </SchoolInfoRow>
            <SchoolInfoRow title="Mother Tongues">
                { motherTongues }
            </SchoolInfoRow>
            <SchoolInfoRow title="Tags">
                { tags.length == 0 ? "-" : tags }
            </SchoolInfoRow>
        </SchoolInfoSection>

        <SchoolInfoSection title={"Location"}>
            <SchoolInfoRow title={"Address"}>
                { school.address }
            </SchoolInfoRow>
            <SchoolInfoRow title={"Location"}>
                { school.location }
            </SchoolInfoRow>
            <SchoolInfoRow title={"Postal Code"}>
                { school.postalCode }
            </SchoolInfoRow>
            <SchoolInfoRow title={"Nearby MRT Station"}>
                { school.nearbyMrtStation }
            </SchoolInfoRow>
            <SchoolInfoRow title={"Nearby Bus Station"}>
                { school.nearbyBusStation }
            </SchoolInfoRow>
        </SchoolInfoSection>

        <SchoolInfoSection title={"Contact Info"}>
            <SchoolInfoRow title={"Website"}>
                <SchoolInfoLink href={school.website}>
                    { school.website }
                </SchoolInfoLink>
            </SchoolInfoRow>
            <SchoolInfoRow title={"Email"}>
                <SchoolInfoLink href={`mailto:${school.email}`}>
                    { school.email }
                </SchoolInfoLink>
            </SchoolInfoRow>
            <SchoolInfoRow title={"Phone Number"}>
                <SchoolInfoLink href={`tel:${school.phoneNumber}`}>
                    { school.phoneNumber }
                </SchoolInfoLink>
            </SchoolInfoRow>
            <SchoolInfoRow title={"Fax Number"}>
                { school.faxNumber }
            </SchoolInfoRow>
        </SchoolInfoSection>

        <SchoolInfoSection title="Subjects">
            <SchoolInfoRow title={"Subjects"}>
                <SchoolInfoMultipleEntry items={school.subjects} />
            </SchoolInfoRow>
        </SchoolInfoSection>

        <SchoolInfoSection title="CCAs">
            <SchoolInfoRow title={"CCAs"}>
                <ul>
                    {Object.entries(groupedCcas).map(([type, items]) =>
                        items.length > 0 ? (
                            <div key={type} className="mb-2">
                                <strong>{ccaLabels[type as CcaType]}:</strong>{' '}
                                <SchoolInfoMultipleEntry items={items} />
                            </div>
                        ) : null
                    )}
                </ul>
            </SchoolInfoRow>
        </SchoolInfoSection>

        <SchoolInfoSection title="MOE Programmes">
            <SchoolInfoRow title={"MOE Programmes"}>
                <SchoolInfoMultipleEntry items={school.programmes} />
            </SchoolInfoRow>
        </SchoolInfoSection>

        <LocationMapWithDirections postalCode = {Number(school.postalCode)}/>
        <CommentSection schoolId={school.id.toString()} />
    </div>
  )
}

const SchoolInfoSection = (
    { title, children } : { title: string; children: ReactNode }
) => {
    return (
        <CardBox className="m-2">
            <h5>{ title }</h5>
            <div className="overflow-x-auto overflow-y-hidden">
                <Table className="table-fixed w-full">
                    <TableBody className="border-0">
                        { children }
                    </TableBody>
                </Table>
            </div>
        </CardBox>
    );
}

const SchoolInfoRow = (
    { title, children } : { title: string; children: ReactNode }
) => {
    return (
        <TableRow>
            <TableCell className="w-48 text-20 font-semibold  py-3 whitespace-nowrap">{title}</TableCell>
            <TableCell className="whitespace-nowrap text-sm font-medium">
                { children }
            </TableCell>
        </TableRow>
    );
}

const SchoolInfoLink = (
    { href, children } : { href: string; children: ReactNode }
) => {
    return (
        <a className={"hover:underline"} href={href} target={"_blank"} rel={"noopener noreferrer"}>
            { children }
        </a>
    );
}

const SchoolInfoMultipleEntry = (
    { items } : { items: Array<string>}
) => {
    if(items.length == 0) return "-"

    return (
        <div className="mt-1 flex flex-wrap gap-1">
            {items.map((item) => (
                <span
                    key={item}
                    className="inline-block px-2 py-1 text-xs font-medium bg-gray-100 rounded"
                >
                  {item}
                </span>
            ))}
        </div>
    );
}

export default SchoolInfoCards;