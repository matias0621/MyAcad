package MyAcad.Project.backend.Model.Academic;

import MyAcad.Project.backend.Enum.AcademicStatus;
import MyAcad.Project.backend.Model.Users.Student;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectsXStudentResponse {

    private Long id;

    private SubjectsResponse subjects;
    private Student student;
    private CommissionResponse commission;
    private AcademicStatus stateStudent;

}
