import Subjects from "../Subjects/Subjects";

export interface Exam {
    id: number,
    score: number,
    subject: Subjects
}

export interface PostExam{
    score: number,
    subjectId: number
}