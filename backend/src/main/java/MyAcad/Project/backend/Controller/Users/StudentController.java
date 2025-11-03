package MyAcad.Project.backend.Controller.Users;

import MyAcad.Project.backend.Enum.Role;
import MyAcad.Project.backend.Exception.DniAlreadyExistsException;
import MyAcad.Project.backend.Exception.EmailAlreadyExistsException;
import MyAcad.Project.backend.Exception.LegajoAlreadyExistsException;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.StudentDTO;
import MyAcad.Project.backend.Service.Users.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
@AllArgsConstructor
public class StudentController {
    private final StudentService services;

    //GET
    //Listado
    @GetMapping()
    public List<Student> listStudents() {
        return services.list();
    }

    //Paginación
    @GetMapping("/paginated")
    public Page<Student> listStudentPaginated(@RequestParam(name = "page") int page,
                                              @RequestParam(name = "size") int size) {
        return services.listStudentsPaginated(page, size);
    }

    //Obtener por legajo
    @GetMapping("/legajo/{legajo}")
    public List<Student> getByLegajoContaining(@PathVariable(name = "legajo", required = false) String legajo) {
        if (legajo == null || legajo.isEmpty()) {
            return listStudents();
        } else {
            return services.getByLegajoContaining(legajo);
        }
    }

    //Obtener por nombre
    @GetMapping("/name/{name}")
    public List<Student> getByName(@PathVariable(name = "name", required = false) String name) {
        if (name == null || name.isEmpty()) {
            return listStudents();
        } else {
            return services.getByFullName(name);
        }
    }

    //Obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        Optional<Student> student = services.getById(id);
        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody StudentDTO dto) {
        try {
            Student student = new Student(dto);
            //Por defecto se le asigna el dni como contraseña a un usuario nuevo, luego lo cambia el mismo en su cuenta
            student.setPassword(String.valueOf(dto.getDni()));
            student.setRole(Role.STUDENT);
            services.add(student);
            return ResponseEntity.ok(student);
        }catch (EmailAlreadyExistsException | LegajoAlreadyExistsException | DniAlreadyExistsException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable(name = "id") Long id){
        return services.delete(id);
    }

    //PUT
    @PutMapping
    public ResponseEntity<?> updateStudent(@RequestBody Student updatedUser) {
        try {
            services.modify(updatedUser.getId(), updatedUser);
            return ResponseEntity.ok(updatedUser);
        }catch (EmailAlreadyExistsException | LegajoAlreadyExistsException | DniAlreadyExistsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
