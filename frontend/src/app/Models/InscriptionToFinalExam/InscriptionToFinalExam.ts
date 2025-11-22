import Program from "../Program/Program";
import Subjects from "../Subjects/Subjects";
import Student from "../Users/Student";

export interface PostInscriptionToFinalExam {
    inscriptionDate: string,
    finalExamDate: string,
    subjectsId: number
    program: string
}

export interface InscriptionToFinalExam {
    id: number,
    inscriptionDate: string,
    finalExamDate: string,
    subjects: Subjects
    students: Student[]
    program: Program 
}