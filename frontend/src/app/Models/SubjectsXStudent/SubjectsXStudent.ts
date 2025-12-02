import Subjects from "../Subjects/Subjects"
import Student from "../Users/Student"

export interface PostSubjectsXStudent {
    subjectsId: number,
    studentId: number,
    academicStatus: string
}

export interface SubjectsXStudent {
    id: number,
    subjects:Subjects,
    student:Student,
    academicStatus: string
}