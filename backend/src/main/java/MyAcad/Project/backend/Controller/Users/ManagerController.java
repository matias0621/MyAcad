package MyAcad.Project.backend.Controller.Users;

import MyAcad.Project.backend.Enum.Role;
import MyAcad.Project.backend.Exception.DniAlreadyExistsException;
import MyAcad.Project.backend.Exception.EmailAlreadyExistsException;
import MyAcad.Project.backend.Exception.LegajoAlreadyExistsException;
import MyAcad.Project.backend.Model.Users.Manager;
import MyAcad.Project.backend.Model.Users.ManagerDTO;
import MyAcad.Project.backend.Service.Users.ManagerService;
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
    private final ManagerService services;


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
        Optional<Manager> manager = services.getById(id);
        if (manager.isPresent()) {
            return ResponseEntity.ok(manager.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> addManager(@RequestBody ManagerDTO dto) {
        try {
            Manager manager = new Manager(dto);
            //Por defecto se le asigna el dni como contraseña a un usuario nuevo, luego lo cambia el mismo en su cuenta
            manager.setPassword(String.valueOf(dto.getDni()));
            manager.setRole(Role.STUDENT);
            services.add(manager);
            return ResponseEntity.ok(manager);
        }catch (EmailAlreadyExistsException | LegajoAlreadyExistsException | DniAlreadyExistsException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManager(@PathVariable(name = "id") Long id){
        return services.delete(id);
    }

    // Baja definitiva
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> definitiveDeleteManager(@PathVariable Long id) {
        return services.definitiveDeleteManager(id);
    }
    //PUT
    @PutMapping
    public ResponseEntity<?> updateManager(@RequestBody Manager updatedUser) {
        try {
            services.modify(updatedUser.getId(), updatedUser);
            return ResponseEntity.ok(updatedUser);
        }catch (EmailAlreadyExistsException | LegajoAlreadyExistsException | DniAlreadyExistsException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
