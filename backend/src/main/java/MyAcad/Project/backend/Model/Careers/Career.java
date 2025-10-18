package MyAcad.Project.backend.Model.Careers;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Career {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private Integer durationMonths;
    private Double monthlyFee;
    private Double annualFee;
    private Boolean active;

    public Career(CareerDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.durationMonths = dto.getDurationMonths();
        this.monthlyFee = dto.getMonthlyFee();
        this.annualFee = dto.getAnnualFee();
        this.active = dto.getActive();
    }

}
