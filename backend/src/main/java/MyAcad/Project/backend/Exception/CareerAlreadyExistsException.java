package MyAcad.Project.backend.Exception;

public class CareerAlreadyExistsException extends RuntimeException {
    public CareerAlreadyExistsException() {
        super("Ese nombre de carrera ya existe: ");
    }
}
