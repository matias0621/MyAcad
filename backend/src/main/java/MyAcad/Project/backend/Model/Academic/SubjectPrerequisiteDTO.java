package MyAcad.Project.backend.Model.Academic;


import MyAcad.Project.backend.Enum.AcademicStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectPrerequisiteDTO {
    
    private Long subjectId;
    private Long prerequisiteId;
    private AcademicStatus requiredStatus;
}
