package MyAcad.Project.backend.Model.Users;

import MyAcad.Project.backend.Enum.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
@ToString
public abstract class User {
    @Id
    @GeneratedValue
    protected Long id;
    protected String name, lastName, email, username, password;

    @Enumerated(EnumType.STRING)
    protected Role role;

    public User(UserDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.lastName = dto.getLastName();
        this.email = dto.getEmail();
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.role = dto.getRole();
    }
}
