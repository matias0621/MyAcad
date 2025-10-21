package MyAcad.Project.backend.Model.FinalExam;

import MyAcad.Project.backend.Model.Subjects.SubjectsEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinalExamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "final_exam_id")
    private int id;

    private int score;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private SubjectsEntity subject;

}
