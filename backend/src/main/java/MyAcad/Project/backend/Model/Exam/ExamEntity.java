package MyAcad.Project.backend.Model.Exam;


import MyAcad.Project.backend.Model.Commission.Commission;
import MyAcad.Project.backend.Model.Subjects.SubjectsEntity;
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
    @Column(name = "commission_id")
    private Long id;

    private int score;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private SubjectsEntity subject;

}
