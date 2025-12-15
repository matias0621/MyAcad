package MyAcad.Project.backend.Model.Academic;


import MyAcad.Project.backend.Enum.AcademicStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectPrerequisiteResponse {
    private Long id;
    // Use singular 'subject' so MapStruct maps SubjectPrerequisiteEntity.subject -> subject
    private SubjectsResponse subject;
    private SubjectsResponse prerequisite;
    // Match frontend naming
    private AcademicStatus requiredStatus;
}
