package MyAcad.Project.backend.Model.MateriaXAlumno;

import MyAcad.Project.backend.Enum.AcademicStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectsXStudentDTO {

    private Long subjectsId;

    private Long studentId;

    private AcademicStatus academicStatus;
}
