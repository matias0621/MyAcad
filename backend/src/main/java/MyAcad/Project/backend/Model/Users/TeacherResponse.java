package MyAcad.Project.backend.Model.Users;

import MyAcad.Project.backend.Model.Academic.CommissionResponse;
import MyAcad.Project.backend.Model.Academic.SubjectsResponse;
import MyAcad.Project.backend.Model.Programs.ProgramResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherResponse {
    private Long id;
    private String legajo;
    private String name;
    private String password;
    private String lastName;
    private String email;
    private int dni;
    private boolean active;

    private List<ProgramResponse> programs;
    private List<SubjectsResponse> subjects;
    private List<CommissionResponse> commissions;
}