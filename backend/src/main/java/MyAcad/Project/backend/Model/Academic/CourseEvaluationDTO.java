package MyAcad.Project.backend.Model.Academic;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseEvaluationDTO {

    private String feedback;
    private Long subjectId;
    private Long teacherId;
}
