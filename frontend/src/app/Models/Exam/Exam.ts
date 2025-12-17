import Subjects from "../Subjects/Subjects"
import Student from "../Users/Student"


export interface Exams {
    id: number,
    score: number,
    examType: string,
    subject: Subjects,
    student: Student,
    date?: string
}

export interface ExamsPost {
    score: number,
    examType: string,
    subjectId: number,
    legajoStudent: string,
    date?: string
}
