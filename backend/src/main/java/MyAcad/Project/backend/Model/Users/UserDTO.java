package MyAcad.Project.backend.Model.Users;

import MyAcad.Project.backend.Enum.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDTO {
    protected Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50)
    protected String name;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50)
    protected String lastName;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50)
    @Email(message = "El email no es válido.")
    @Column(unique = true)
    protected String email;

    @NotBlank(message = "El legajo no puede estar vacío, máximo 999.999")
    @Size(max = 999999)
    @Column(unique = true)
    protected String legajo;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.])[A-Za-z\\d@$!%*?&.]{8,15}$",
            message = "La contraseña debe tener entre 6 y 15 caracteres, al menos una mayuscula, " +
                    "una minuscula y un caracter especial(@$!%*?&.).")
    @NotBlank(message = "El nombre no puede estar vacío")
    protected String password;

    @Enumerated(EnumType.STRING)
    protected Role role;

    public UserDTO(String name, String lastName, String email, String password) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
