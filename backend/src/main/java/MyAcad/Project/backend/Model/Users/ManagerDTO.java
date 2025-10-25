package MyAcad.Project.backend.Model.Users;

import MyAcad.Project.backend.Enum.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ManagerDTO extends UserDTO{

    public ManagerDTO(String name, String lastName, String email, String password) {
        super(name, lastName, email, password);
        this.role = Role.MANAGER;
    }
}
