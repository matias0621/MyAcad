package MyAcad.Project.backend.Model.Users;

import MyAcad.Project.backend.Enum.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ManagerDTO extends UserDTO{

    public ManagerDTO(Long id, String name, String lastName, String email, String password) {
        super(id, name, lastName, email, password);
        this.legajo = String.valueOf((id + 800000));
        this.role = Role.MANAGER;
    }
}
