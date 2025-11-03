package MyAcad.Project.backend.Model.Academic;

import MyAcad.Project.backend.Enum.AcademicStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectsXStudentDTO {

    private Long subjectsId;

    private Long studentId;

    private AcademicStatus academicStatus;
}
