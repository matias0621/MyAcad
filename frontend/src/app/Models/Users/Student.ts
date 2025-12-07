import Program from "../Program/Program";

export default interface Student {
    id: number;
    name: string;
    lastName: string;
    dni: number;
    email: string;
    legajo: string;
    password: string;
    active: boolean
    programs: Program[]
}

export interface RegistrationStudentOrTeacher {
    legajo: string,
    subjectsId: number
}