package MyAcad.Project.backend.Exception;

public class CourseAlreadyExistsException extends RuntimeException {
    public CourseAlreadyExistsException() {
        super("Ese nombre de carrera ya existe: ");
    }
}

