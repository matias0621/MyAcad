package MyAcad.Project.backend.Exception;

public class ExpiredJwtException extends RuntimeException {
    public ExpiredJwtException() {
        super("La sesión ha expirado");
    }
}
