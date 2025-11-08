export default interface Student {
    id: number;
    name: string;
    lastName: string;
    dni: number;
    email: string;
    legajo: string;
    password: string;
}

export interface RegistrationStudentOrTeacher {
    legajo: string,
    subjectsId: number
}