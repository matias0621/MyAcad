package MyAcad.Project.backend.Model.Users;


import MyAcad.Project.backend.Enum.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class StudentDTO extends UserDTO{

    public StudentDTO(Long id, String name, String lastName, String email, String password) {
        super(id, name, lastName, email, password);
        this.legajo = String.valueOf((id + 100000));
        this.role = Role.STUDENT;
    }
}
