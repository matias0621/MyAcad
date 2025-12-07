import Program from "../Program/Program";

export default interface Teacher {
    id: number;
    name: string;
    lastName: string;
    dni: number;
    email: string;
    legajo: string;
    password: string;
    active: boolean
    programs:Program[]
}