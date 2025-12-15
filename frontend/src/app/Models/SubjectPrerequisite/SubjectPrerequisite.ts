import Subjects from "../Subjects/Subjects";

export interface SubjectPrerequisite {
    subject:Subjects,
    prerequisite:Subjects,
    requiredStatus: string
}

export interface PostSubjectPrerequisite {
    subjectId: number,
    prerequisiteId: number,
    requiredStatus: string
}