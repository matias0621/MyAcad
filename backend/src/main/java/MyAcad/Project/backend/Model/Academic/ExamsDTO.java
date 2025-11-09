package MyAcad.Project.backend.Model.Academic;

import MyAcad.Project.backend.Enum.ExamType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamsDTO {
    @Min(1)
    @Max(100)
    private int score;

    @NotNull
    private Long subjectId;

    @NotNull
    private String legajoStudent;

    @NotNull
    private ExamType examType;
}
