package MyAcad.Project.backend.Model.Academic;


import MyAcad.Project.backend.Model.Users.Student;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id")
    private Long id;

    private int score;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private SubjectsEntity subject;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Student student;

}
