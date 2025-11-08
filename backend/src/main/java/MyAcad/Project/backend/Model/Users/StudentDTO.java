package MyAcad.Project.backend.Model.Users;


import MyAcad.Project.backend.Enum.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class StudentDTO extends UserDTO{

    public StudentDTO(String name, String lastName, int dni, String email, String password, boolean active) {
        super(name, lastName, dni, email, password, active);
        this.role = Role.STUDENT;
    }
}
