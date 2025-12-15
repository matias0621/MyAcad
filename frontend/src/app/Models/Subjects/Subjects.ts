import { SubjectPrerequisite } from "../SubjectPrerequisite/SubjectPrerequisite";


export default interface Subjects {
  id: number;
  name: string;
  description: string;
  semesters: number;
  academicStatus: string;
  prerequisites?: SubjectPrerequisite[];
  program : string;
  subjectActive: boolean
}