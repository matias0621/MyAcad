package MyAcad.Project.backend.Model.Users;

import MyAcad.Project.backend.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TeacherDTO extends UserDTO{
    public TeacherDTO(Long id, String name, String lastName, String email, String password) {
        super(id, name, lastName, email, password);
        this.legajo = String.valueOf((id + 400000));
        System.out.println(this.legajo);
        this.role = Role.TEACHER;
    }
}
