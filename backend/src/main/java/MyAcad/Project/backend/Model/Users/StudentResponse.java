package MyAcad.Project.backend.Model.Users;

import MyAcad.Project.backend.Enum.Role;
import MyAcad.Project.backend.Model.Programs.ProgramResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {
    private Long id;
    private String name;
    private String lastName;
    private String legajo;
    private String email;
    private int dni;
    private Role role;
    private boolean isActive;

    private List<ProgramResponse> programs;
}
