package MyAcad.Project.backend.Service;

import MyAcad.Project.backend.Configuration.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    // Genera un token para un usuario autenticado
    public String generateToken(UserDetails userDetails) {
        UserDetailsImpl user = (UserDetailsImpl) userDetails;
        return Jwts.builder()
                //user.getUsername devuelve el legajo, con el que se logea.
                .setSubject(user.getUsername())
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("lastname", user.getLastname())
                .claim("role", "ROLE_" + user.getRole())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String legajo = extractLegajo(token);
        return legajo.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String extractLegajo(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token){
        return extractAllClaims(token).get("role", String.class);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    // Extrae todos los datos (claims) del token
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("Invalid token: " + e.getMessage());
            return null;
        }
    }

    //Convierte la clave secreta en un objeto Key para usar con la librería jjwt
    private Key getKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
