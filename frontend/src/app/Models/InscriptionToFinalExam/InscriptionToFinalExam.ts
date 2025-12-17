import Program from "../Program/Program";
import Subjects from "../Subjects/Subjects";
import Student from "../Users/Student";
import Teacher from "../Users/Teachers";

export interface PostInscriptionToFinalExam {
    inscriptionDate: string,
    finalExamDate: string,
    subjectsId: number,
    program: string,
    teacherLegajo: number
}

export interface InscriptionToFinalExam {
    id: number,
    inscriptionDate: string,
    finalExamDate: string,
    subjects: Subjects
    students: Student[]
    program: Program,
    teacher: Teacher
}