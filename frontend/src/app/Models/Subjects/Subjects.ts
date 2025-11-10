

export default interface Subjects {
  id: number;
  name: string;
  description: string;
  semesters: number;
  academicStatus: string;
  prerequisites?: Subjects[];
  program : string;
  subjectActive: boolean
}

