import Subjects from "../Subjects/Subjects";
import Teacher from "../Users/Teachers";


export interface CourseEvaluation {
    id: number,
    feedback: string,
    createAt: string,
    subject: Subjects,
    teacher: Teacher
}

export interface PostCourseEvaluation {
    feedback: string,
    subjectId: number,
    teacherId: number
}