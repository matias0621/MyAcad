package MyAcad.Project.backend.Model.Academic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectsDTO {

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;
    @NotBlank
    @Size(min = 3, max = 500)
    private String description;
    @NotNull
    private int semesters;

    @NotNull
    private Boolean subjectActive;

    private List<SubjectsEntity> prerequisites;

    public boolean getSubjectActive() {
        return subjectActive;
    }
}
