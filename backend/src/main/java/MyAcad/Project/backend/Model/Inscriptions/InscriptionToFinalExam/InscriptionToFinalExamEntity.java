package MyAcad.Project.backend.Model.Inscriptions.InscriptionToFinalExam;

import MyAcad.Project.backend.Model.Academic.SubjectsEntity;
import MyAcad.Project.backend.Model.Programs.Program;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscriptionToFinalExamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inscription_to_final_exam_id")
    private Long id;

    private LocalDateTime inscriptionDate;

    private LocalDateTime finalExamDate;

    @ManyToOne
    private SubjectsEntity subjects;

    @ManyToMany
    @JoinTable(
            name = "student_x_inscription",
            joinColumns = @JoinColumn(name = "inscription_to_final_exam_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Student> students;

    @ManyToOne
    @JoinTable(
            name = "teacher_x_inscription",
            joinColumns = @JoinColumn(name = "inscription_to_final_exam_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Teacher teacher;

    @ManyToOne
    private Program program;


}
