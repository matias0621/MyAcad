package MyAcad.Project.backend.Exception;

public class DniAlreadyExistsException extends RuntimeException {
    public DniAlreadyExistsException() {
        super("Este dni ya está registrado.");
    }
}
