package MyAcad.Project.backend.Model.Commission;

import MyAcad.Project.backend.Enum.ProgramType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class CommissionDTO {

    private int number;
    private Long id;
    private String subject;
    private String students;
    private String teachers;
    private int capacity;
    private boolean active;
    private ProgramType programType;

    public CommissionDTO(int number,Long id, String subject, String students, String teachers, int capacity, ProgramType programType, boolean active) {
        this.programType = programType;
    }
}
