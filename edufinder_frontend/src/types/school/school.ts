export type Level =
  | 'PRIMARY'
  | 'SECONDARY (S1-S5)'
  | 'SECONDARY (S1-S4)'
  | 'JUNIOR COLLEGE'
  | 'MIXED LEVEL (S1-JC2)';

export type NatureCode = 'CO-ED SCHOOL' | "BOYS' SCHOOL" | "GIRLS' SCHOOL";

export type SchoolType =
  | 'GOVERNMENT SCHOOL'
  | 'GOVERNMENT-AIDED SCH'
  | 'INDEPENDENT SCHOOL'
  | 'SPECIALISED SCHOOL';

export type CcaType =
  | 'PHYSICAL SPORTS'
  | 'CLUBS AND SOCIETIES'
  | 'VISUAL AND PERFORMING ARTS'
  | 'UNIFORMED GROUPS'
  | 'OTHERS';

export interface Cca {
  type: CcaType;
  name: string;
}

export type SessionCode = 'FULL DAY' | 'SINGLE SESSION';

export interface School {
  id: number;
  name: string;
  location: string;

  ccas: Cca[];      // Co-curricular activities
  subjects: string[];
  moeProgrammes: string[]; // Signature MOE programmes e.g. bicultural studies

  level: Level;
  natureCode: NatureCode;
  type: SchoolType;
  sessionCode: SessionCode;

  address: string;
  postalCode: string;

  nearbyMrtStation: string;
  nearbyBusStation: string;

  website: string;
  email: string;
  phoneNumber: string;
  faxNumber: string;

  sapInd: boolean;
  autonomousInd: boolean;
  giftedInd: boolean;
  ipInd: boolean;

  motherTongue1: string | null;
  motherTongue2: string | null;
  motherTongue3: string | null;

  minCutOffPoint: number | null;
  maxCutOffPoint: number | null;
}