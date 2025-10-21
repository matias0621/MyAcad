package MyAcad.Project.backend.Model.Programs;

import MyAcad.Project.backend.Enum.ProgramType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TechnicalDTO extends ProgramsDTO{

    public TechnicalDTO(Long id, String name, String description, Integer durationMonths, Double monthlyFee, Double annualFee, Boolean active) {
        super(id, name, description, durationMonths, monthlyFee, annualFee, active);
        this.programType = ProgramType.TECHNICAL;
    }

}
