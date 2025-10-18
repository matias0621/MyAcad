package MyAcad.Project.backend.Exception;

public class NameMateriaAlreadyExistsException extends RuntimeException {
    public NameMateriaAlreadyExistsException() {
        super("Esta materia ya existe en esta carrera");
    }
}
