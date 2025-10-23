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

export type SessionCode = 'FULL DAY' | 'SINGLE SESSION';

export interface School {
  id: number;
  name: string;
  location: string;

  ccas: string[];      // Co-curricular activities
  subjects: string[];

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

  minCutOffPoint: number | null;
  maxCutOffPoint: number | null;
}