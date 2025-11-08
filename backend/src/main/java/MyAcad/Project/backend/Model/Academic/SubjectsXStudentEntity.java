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

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private SubjectsEntity subjects;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Enumerated(EnumType.STRING)
    @Column(name = "state_student", nullable = false)
    private AcademicStatus stateStudent;
}
