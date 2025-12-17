package MyAcad.Project.backend.Model.Inscriptions.InscriptionToFinalExam;

import MyAcad.Project.backend.Model.Academic.SubjectsEntity;
import MyAcad.Project.backend.Model.Academic.SubjectsResponse;
import MyAcad.Project.backend.Model.Programs.Program;
import MyAcad.Project.backend.Model.Programs.ProgramResponse;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.Teacher;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscriptionToFinalExamResponse {
    private Long id;
    private LocalDateTime inscriptionDate;
    private LocalDateTime finalExamDate;
    private List<Student> students;
    private Teacher teacher;
    private SubjectsResponse subjects;
    private ProgramResponse program;
}
