package MyAcad.Project.backend.Model.Subjects;

import MyAcad.Project.backend.Enum.AcademicStatus;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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

    @NotNull
    private AcademicStatus academicStatus;

    private List<SubjectsEntity> prerequisites;

    public boolean getSubjectActive() {
        return subjectActive;
    }
}
