package MyAcad.Project.backend.Controller;

import MyAcad.Project.backend.Enum.Role;
import MyAcad.Project.backend.Exception.EmailAlreadyExistsException;
import MyAcad.Project.backend.Exception.UsernameAlreadyExistsException;
import MyAcad.Project.backend.Model.Users.Manager;
import MyAcad.Project.backend.Model.Users.ManagerDTO;
import MyAcad.Project.backend.Service.ManagerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/manager")
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
    public Page<Manager> listManagerPaginated(@RequestParam(name = "page") int page,
                                              @RequestParam(name = "size") int size) {
        return services.listManagersPaginated(page, size);
    }

    //Obtener por usuario
    @GetMapping("/username/{username}")
    public List<Manager> getByUsernameIgnoringCase(@PathVariable(name = "username", required = false) String username) {
        if (username == null || username.isEmpty()) {
            return listManagers();
        } else {
            return services.getByUsernameIgnoringCase(username);
        }
    }

    //Obtener por id
    @GetMapping("/id/{id}")
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
        }catch (EmailAlreadyExistsException | UsernameAlreadyExistsException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }

    //DELETE
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteManager(@PathVariable(name = "id") Long id){
        return services.delete(id);
    }

    //PUT
    @PutMapping
    public ResponseEntity<?> updateManager(@RequestBody Manager updatedUser) {
        try {
            services.modify(updatedUser.getId(), updatedUser);
            return ResponseEntity.ok(updatedUser);
        }catch (EmailAlreadyExistsException | UsernameAlreadyExistsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
