package MyAcad.Project.backend.Model.Academic;

import MyAcad.Project.backend.Enum.ExamType;
import MyAcad.Project.backend.Model.Users.Student;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ExamsResponse {
    private Long id;
    private int score;
    private ExamType examType;
    private SubjectsResponse subject;
    private Student student;
    private LocalDateTime date;
}
