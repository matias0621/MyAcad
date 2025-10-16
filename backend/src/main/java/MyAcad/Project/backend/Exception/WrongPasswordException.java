package MyAcad.Project.backend.Exception;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("Contraseña incorrecta.");
    }
}
