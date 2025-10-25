package MyAcad.Project.backend.Exception;

public class LegajoAlreadyExistsException extends RuntimeException {
    public LegajoAlreadyExistsException() {
        super("Este legajo ya se encuentra en uso.");
    }
}
