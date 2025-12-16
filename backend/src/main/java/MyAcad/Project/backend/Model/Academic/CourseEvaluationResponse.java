package MyAcad.Project.backend.Model.Academic;

import MyAcad.Project.backend.Model.Users.Teacher;
import MyAcad.Project.backend.Model.Users.TeacherResponse;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseEvaluationResponse {
    private Long id;
    private String feedback;
    private LocalDateTime createdAt;
    private SubjectsResponse subject;
    private TeacherResponse teacher;
}
