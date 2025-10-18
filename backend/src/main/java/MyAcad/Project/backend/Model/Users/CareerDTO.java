package MyAcad.Project.backend.Model.Users;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class CareerDTO {
    private Long id;
    private String name;
    private String description;
    private Integer durationMonths;
    private Double monthlyFee;
    private Double annualFee;
    private Boolean active;


}
