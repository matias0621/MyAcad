package MyAcad.Project.backend.Model.Academic;

import MyAcad.Project.backend.Enum.AcademicStatus;
import MyAcad.Project.backend.Model.Users.Student;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubjectsXStudentEntity {

    @Id
    @Column(name = "subjetXStudent_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private SubjectsEntity subjects;

    @OneToOne
    private Student student;

    @Enumerated(EnumType.STRING)
    private AcademicStatus stateStudent;
}
