package MyAcad.Project.backend.Model.Programs;

import MyAcad.Project.backend.Enum.ProgramType;
import MyAcad.Project.backend.Enum.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ProgramsDTO {
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50)
    private String name;
    private String description;

    private Integer durationMonths;
    private Double monthlyFee;
    private Double annualFee;
    private Boolean active;

    @Enumerated(EnumType.STRING)
    ProgramType programType;

    public ProgramsDTO(CareerDTO dto) {

    }
    public ProgramsDTO(Long id, String name, String description, Integer durationMonths, Double monthlyFee, Double annualFee, Boolean active) {
    }

}