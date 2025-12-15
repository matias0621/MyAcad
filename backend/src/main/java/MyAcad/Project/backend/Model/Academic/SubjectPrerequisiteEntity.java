package MyAcad.Project.backend.Model.Academic;

import MyAcad.Project.backend.Enum.AcademicStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "subject_prerequisite")
public class SubjectPrerequisiteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private SubjectsEntity subject;

    @ManyToOne
    @JoinColumn(name = "prerequisite_id", nullable = false)
    private SubjectsEntity prerequisite;

    @Enumerated(EnumType.STRING)
    @Column(name = "required_status", nullable = false)
    private AcademicStatus requiredStatus;

}
