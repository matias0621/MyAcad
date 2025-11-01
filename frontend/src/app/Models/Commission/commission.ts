import { Subjects } from "../../pages/subjects/subjects"
import Student from "../Users/Student"

export default interface Commission {
    id: number
    number: number
    students: Student[]
    subjects: Subjects[]
    program : String
    capacity: number
    active: boolean
}
