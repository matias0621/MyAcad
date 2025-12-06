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
    @Column(name = "user_id")
    protected Long id;
    @Column(unique = true)
    protected String legajo;

    protected String name, lastName, email, password;

    @Column(unique = true)
    protected int dni;

    @Enumerated(EnumType.STRING)
    protected Role role;

    protected boolean active;

    public User(UserDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.lastName = dto.getLastName();
        this.dni = dto.getDni();
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.role = dto.getRole();
        this.active = dto.isActive();
    }


    public Object getFullName() {
        return this.name + " " + this.lastName;
    }
}
