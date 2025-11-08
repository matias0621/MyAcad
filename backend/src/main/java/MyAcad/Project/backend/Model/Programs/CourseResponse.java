package MyAcad.Project.backend.Model.Programs;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CourseResponse {
    private Long id;
    private String name;
    private String description;
    private Integer durationMonths;
    private Double monthlyFee;
    private Double annualFee;
    private Boolean active;
}