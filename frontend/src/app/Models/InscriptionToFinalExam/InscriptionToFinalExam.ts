import Subjects from "../Subjects/Subjects";
import Student from "../Users/Student";

export interface PostInscriptionToFinalExam {
    inscriptionDate: string,
    finalExamDate: string,
    subjectsId: number
}

export interface InscriptionToFinalExam {
    id: number,
    inscriptionDate: string,
    finalExamDate: string,
    subjects: Subjects
    students: Student[] 
}