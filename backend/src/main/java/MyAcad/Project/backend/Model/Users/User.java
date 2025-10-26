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
    @Column(unique = true)
    protected String legajo;

    protected String name, lastName, email, password;

    protected int dni;

    @Enumerated(EnumType.STRING)
    protected Role role;

    public User(UserDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.lastName = dto.getLastName();
        this.dni = dto.getDni();
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.role = dto.getRole();
    }
}
