package MyAcad.Project.backend.Model.FinalExam;

import MyAcad.Project.backend.Model.Subjects.SubjectsEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FinalExamDTO {
    @Min(1)
    @Max(100)
    private int score;

    @NotNull
    private int subjectId;
}
