package MyAcad.Project.backend.Model.Programs;

import MyAcad.Project.backend.Enum.ProgramType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CourseDTO extends ProgramsDTO{

    public CourseDTO(Long id, String name, String description, Integer durationMonths, Double monthlyFee, Double annualFee, Boolean active) {
        super(id, name, description, durationMonths, monthlyFee, annualFee, active);
        this.programType = ProgramType.COURSE;
    }
}
