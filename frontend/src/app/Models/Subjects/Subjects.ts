

export default interface Subjects {
  id: number;
  name: string;
  description: string;
  semesters: number;
  prerequisites?: Subjects[];
}

