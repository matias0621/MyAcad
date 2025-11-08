package MyAcad.Project.backend.Model.Academic;

import MyAcad.Project.backend.Enum.AcademicStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectsResponse {
    private Long id;
    private String name;
    private String description;
    private int semesters;
    private boolean subjectActive;
    private AcademicStatus academicStatus;
    private List<SubjectsResponse> prerequisites;
}
