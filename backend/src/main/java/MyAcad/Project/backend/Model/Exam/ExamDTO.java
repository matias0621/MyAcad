package MyAcad.Project.backend.Model.Exam;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ExamDTO {
    @Min(1)
    @Max(100)
    private int score;

    @NotNull
    private int subjectId;
}
