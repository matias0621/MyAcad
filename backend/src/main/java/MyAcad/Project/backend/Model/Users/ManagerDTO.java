package MyAcad.Project.backend.Model.Users;

import MyAcad.Project.backend.Enum.Role;

public class ManagerDTO extends UserDTO{

    public ManagerDTO(Long id, String name, String lastName, String email, String username, String password, String legajo) {
        super(id, name, lastName, email, username, password);
        this.role = Role.MANAGER;
    }
}
