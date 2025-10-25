import Subjects from "../Subjects/Subjects";

export default interface Exam {
    id: number,
    score: number,
    subject: Subjects
}