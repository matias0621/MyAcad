import Subjects from "../Subjects/Subjects";

export interface ExamFinal {
    id: number,
    score: number,
    subject: Subjects
}

export interface PostExamFinal {
    score: number,
    subjectId: number
}