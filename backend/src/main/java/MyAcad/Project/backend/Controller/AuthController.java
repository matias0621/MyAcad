package MyAcad.Project.backend.Controller;

import MyAcad.Project.backend.Configuration.LoginRequest;
import MyAcad.Project.backend.Configuration.LoginResponse;
import MyAcad.Project.backend.Configuration.UserDetailsImpl;
import MyAcad.Project.backend.Model.ChangePassword.ChangePasswordRequest;
import MyAcad.Project.backend.Service.JwtService;
import MyAcad.Project.backend.Service.Users.UserLookupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController //
@RequestMapping("/auth") //
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserLookupService service;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLegajo(),
                        request.getPassword()
                )
        );

        UserDetailsImpl user = (UserDetailsImpl) service.loadUserByUsername(request.getLegajo());

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request,
                                            Principal principal) {

        String legajo = principal.getName();

        service.changePassword(legajo, request.getCurrentPassword(), request.getNewPassword());

        return ResponseEntity.ok("Contraseña actualizada correctamente");
    }
}
