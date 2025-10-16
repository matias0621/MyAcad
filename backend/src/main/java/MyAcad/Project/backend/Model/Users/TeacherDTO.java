package MyAcad.Project.backend.Model.Users;

import MyAcad.Project.backend.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeacherDTO extends UserDTO{
    public TeacherDTO(Long id, String name, String lastName, String email, String username, String password) {
        super(id, name, lastName, email, username, password);
        this.role = Role.TEACHER;
    }
}
