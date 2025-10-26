package MyAcad.Project.backend.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String legajo;
    private String password;
}