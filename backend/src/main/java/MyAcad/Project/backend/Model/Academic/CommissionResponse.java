package MyAcad.Project.backend.Model.Academic;

import MyAcad.Project.backend.Model.Users.Student;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommissionResponse {
    private Long id;
    private int number;
    private String program;
    private int capacity;
    private boolean active;
    private List<SubjectsResponse> subjects;
    private List<Student> students;
}
