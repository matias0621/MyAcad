package MyAcad.Project.backend.Controller.Users;

import MyAcad.Project.backend.Enum.Role;
import MyAcad.Project.backend.Exception.DniAlreadyExistsException;
import MyAcad.Project.backend.Exception.EmailAlreadyExistsException;
import MyAcad.Project.backend.Exception.LegajoAlreadyExistsException;
import MyAcad.Project.backend.Model.Users.*;
import MyAcad.Project.backend.Service.Users.TeacherService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teachers")
@AllArgsConstructor
public class TeacherController {
    private final TeacherService services;

    //GET
    //Listado
    @GetMapping()
    public List<TeacherResponse> listTeachers() {
        System.out.println("Listado de profesores" +  services.list().toString());
        return services.list();
    }

    //Paginación
    @GetMapping("/paginated")
    public Page<TeacherResponse> listTeacherPaginated(@RequestParam(name = "page") int page,
                                              @RequestParam(name = "size") int size) {
        return services.listTeachersPaginated(page, size);
    }

    //Obtener por comisión
    @GetMapping("/commission/{commissionId}")
    public List<TeacherResponse> getByCommission(@PathVariable Long commissionId) {
        return services.getByCommission(commissionId);
    }


    //Obtener por legajo
    @GetMapping("/legajo/{legajo}")
    public List<TeacherResponse> getByLegajoContaining(@PathVariable(name = "legajo", required = false) String legajo) {
        if (legajo == null || legajo.isEmpty()) {
            return listTeachers();
        } else {
            return services.getByLegajoContaining(legajo);
        }
    }

    //Obtener por nombre
    @GetMapping("/name/{name}")
    public List<TeacherResponse> getByName(@PathVariable(name = "name", required = false) String name) {
        if (name == null || name.isEmpty()) {
            return listTeachers();
        } else {
            return services.getByFullName(name);
        }
    }

    //Obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        Optional<TeacherResponse> teacher = services.getById(id);
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
            teacher.setRole(Role.TEACHER);
            services.add(teacher);
            return ResponseEntity.ok(teacher);
        }catch (EmailAlreadyExistsException | LegajoAlreadyExistsException | DniAlreadyExistsException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }

    @PostMapping("/upload-by-csv")
    public ResponseEntity<?> uploadTeacherCSV(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo esta vacio");
        }

        try {
            List<TeacherCsvDto> teacherCsvDtos = services.parseCsv(file);
            services.saveTeacherByCsv(teacherCsvDtos);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el CSV: " + e.getMessage());
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable(name = "id") Long id){
        return services.delete(id);
    }

    // Baja definitiva
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> definitiveDeleteTeacher(@PathVariable Long id) {
        return services.definitiveDeleteTeacher(id);
    }
    //PUT
    @PutMapping
    public ResponseEntity<?> updateTeacher(@RequestBody Teacher updatedUser) {
        try {
            services.modify(updatedUser.getId(), updatedUser);
            return ResponseEntity.ok(updatedUser);
        }catch (EmailAlreadyExistsException | LegajoAlreadyExistsException | DniAlreadyExistsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
