package MyAcad.Project.backend.Controller;

import MyAcad.Project.backend.Enum.Role;
import MyAcad.Project.backend.Exception.EmailAlreadyExistsException;
import MyAcad.Project.backend.Exception.LegajoAlreadyExistsException;
import MyAcad.Project.backend.Model.Users.Manager;
import MyAcad.Project.backend.Model.Users.ManagerDTO;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Service.ManagerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/managers")
@AllArgsConstructor
public class ManagerController {
    private ManagerService services;

    //GET
    //Listado
    @GetMapping()
    public List<Manager> listManagers() {
        return services.list();
    }

    //Paginación
    @GetMapping("/paginated")
    public Page<Manager> listManagerPaginated(@RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "10") int size) {
        return services.listManagersPaginated(page, size);
    }

    //Obtener por legajo
    @GetMapping("/legajo/{legajo}")
    public List<Manager> getByLegajoContaining(@PathVariable(name = "legajo", required = false) String legajo) {
        if (legajo == null || legajo.isEmpty()) {
            return listManagers();
        } else {
            return services.getByLegajoContaining(legajo);
        }
    }

    //Obtener por nombre
    @GetMapping("/name/{name}")
    public List<Manager> getByName(@PathVariable(name = "name", required = false) String name) {
        if (name == null || name.isEmpty()) {
            return listManagers();
        } else {
            return services.getByFullName(name);
        }
    }

    //Obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        Optional<Manager> student = services.getById(id);
        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> addManager(@RequestBody ManagerDTO dto) {
        try {
            Manager student = new Manager(dto);
            student.setRole(Role.STUDENT);
            services.add(student);
            return ResponseEntity.ok(student);
        }catch (EmailAlreadyExistsException | LegajoAlreadyExistsException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManager(@PathVariable(name = "id") Long id){
        return services.delete(id);
    }

    //PUT
    @PutMapping
    public ResponseEntity<?> updateManager(@RequestBody Manager updatedUser) {
        try {
            services.modify(updatedUser.getId(), updatedUser);
            return ResponseEntity.ok(updatedUser);
        }catch (EmailAlreadyExistsException | LegajoAlreadyExistsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
