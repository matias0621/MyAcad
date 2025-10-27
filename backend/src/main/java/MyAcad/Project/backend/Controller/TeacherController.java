package MyAcad.Project.backend.Controller;

import MyAcad.Project.backend.Enum.Role;
import MyAcad.Project.backend.Exception.EmailAlreadyExistsException;
import MyAcad.Project.backend.Exception.LegajoAlreadyExistsException;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.Teacher;
import MyAcad.Project.backend.Model.Users.TeacherDTO;
import MyAcad.Project.backend.Service.TeacherService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teachers")
@AllArgsConstructor
public class TeacherController {
    private TeacherService services;

    //GET
    //Listado
    @GetMapping()
    public List<Teacher> listTeachers() {
        return services.list();
    }

    //Paginación
    @GetMapping("/paginated")
    public Page<Teacher> listTeacherPaginated(@RequestParam(name = "page") int page,
                                              @RequestParam(name = "size") int size) {
        return services.listTeachersPaginated(page, size);
    }


    //Obtener por legajo
    @GetMapping("/legajo/{legajo}")
    public List<Teacher> getByLegajoContaining(@PathVariable(name = "legajo", required = false) String legajo) {
        if (legajo == null || legajo.isEmpty()) {
            return listTeachers();
        } else {
            return services.getByLegajoContaining(legajo);
        }
    }

    //Obtener por nombre
    @GetMapping("/name/{name}")
    public List<Teacher> getByName(@PathVariable(name = "name", required = false) String name) {
        if (name == null || name.isEmpty()) {
            return listTeachers();
        } else {
            return services.getByFullName(name);
        }
    }

    //Obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        Optional<Teacher> teacher = services.getById(id);
        if (teacher.isPresent()) {
            return ResponseEntity.ok(teacher.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> addTeacher(@RequestBody TeacherDTO dto) {
        try {
            Teacher teacher = new Teacher(dto);
            //Por defecto se le asigna el dni como contraseña a un usuario nuevo, luego lo cambia el mismo en su cuenta
            teacher.setPassword(String.valueOf(dto.getDni()));
            teacher.setRole(Role.STUDENT);
            services.add(teacher);
            return ResponseEntity.ok(teacher);
        }catch (EmailAlreadyExistsException | LegajoAlreadyExistsException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable(name = "id") Long id){
        return services.delete(id);
    }

    //PUT
    @PutMapping
    public ResponseEntity<?> updateTeacher(@RequestBody Teacher updatedUser) {
        try {
            services.modify(updatedUser.getId(), updatedUser);
            return ResponseEntity.ok(updatedUser);
        }catch (EmailAlreadyExistsException | LegajoAlreadyExistsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
