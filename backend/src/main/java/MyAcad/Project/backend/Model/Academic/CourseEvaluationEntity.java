package MyAcad.Project.backend.Model.Academic;

import MyAcad.Project.backend.Model.Users.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseEvaluationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String feedback;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    private SubjectsEntity subject;

    @ManyToOne
    private Teacher teacher;

}
