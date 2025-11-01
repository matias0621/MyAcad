package MyAcad.Project.backend.Model.InscriptionToFinalExam;

import MyAcad.Project.backend.Model.Subjects.SubjectsEntity;
import MyAcad.Project.backend.Model.Users.Student;
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



}
