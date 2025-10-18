package MyAcad.Project.backend.Model.Courses;

import MyAcad.Project.backend.Model.Careers.CareerDTO;
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
public class Course {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private Integer durationMonths;
    private Double monthlyFee;
    private Double annualFee;
    private Boolean active;

    public Course(CourseDTO dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.durationMonths = dto.getDurationMonths();
        this.monthlyFee = dto.getMonthlyFee();
        this.annualFee = dto.getAnnualFee();
        this.active = dto.getActive();
    }
}
