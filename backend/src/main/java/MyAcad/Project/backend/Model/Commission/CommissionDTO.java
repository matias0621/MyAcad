package MyAcad.Project.backend.Model.Commission;

import MyAcad.Project.backend.Enum.ProgramType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class CommissionDTO {

    private Long id;
    private int number;

    // IDs de las materias que pertenecen a la comisión
    private Set<Long> subjectIds;

    // IDs de los estudiantes
    private List<Long> studentIds;

    // ID del profesor principal
    private Long teacherId;

    // IDs de los profesores colaboradores
    private List<Long> collaboratorIds;

    private int capacity;
    private boolean active;
    private ProgramType programType;


}
