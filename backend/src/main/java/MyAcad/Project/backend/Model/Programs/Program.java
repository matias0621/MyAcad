package MyAcad.Project.backend.Model.Programs;

import MyAcad.Project.backend.Enum.ProgramType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
@ToString

public abstract class Program {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private Integer durationMonths;
    private Double monthlyFee;
    private Double annualFee;
    private Boolean active;

    @Enumerated(EnumType.STRING)
    protected ProgramType programType;

    public Program(ProgramsDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.durationMonths = dto.getDurationMonths();
        this.monthlyFee = dto.getMonthlyFee();
        this.annualFee = dto.getAnnualFee();
        this.active = dto.getActive();
        this.programType = dto.getProgramType();
    }

}
