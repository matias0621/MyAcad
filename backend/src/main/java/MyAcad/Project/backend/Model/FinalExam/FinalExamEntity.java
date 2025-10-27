package MyAcad.Project.backend.Model.FinalExam;

import MyAcad.Project.backend.Model.Subjects.SubjectsEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    private LocalDate examDate;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private SubjectsEntity subject;

}
