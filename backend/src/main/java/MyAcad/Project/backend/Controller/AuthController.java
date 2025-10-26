package MyAcad.Project.backend.Controller;

import MyAcad.Project.backend.Configuration.LoginRequest;
import MyAcad.Project.backend.Configuration.LoginResponse;
import MyAcad.Project.backend.Configuration.UserDetailsImpl;
import MyAcad.Project.backend.Service.JwtService;
import MyAcad.Project.backend.Service.UserLookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
