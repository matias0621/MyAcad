package MyAcad.Project.backend.Controller.Academic;

import MyAcad.Project.backend.Exception.CommissionAlreadyExistsException;
import MyAcad.Project.backend.Model.Academic.Commission;
import MyAcad.Project.backend.Model.Academic.CommissionDTO;
import MyAcad.Project.backend.Service.Academic.CommissionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/commissions")
@AllArgsConstructor
public class CommissionController {
    private final CommissionService services;


    @GetMapping("/active")
    public List<Commission> listActiveCommissions() {
        return services.listActive();
    }

    @GetMapping()
    public List<Commission> listCommissions() {
        return services.list();
    }
    //Paginacion
    @GetMapping("/paginated")
    public Page<Commission> listCommissionPaginated(@RequestParam(name = "page") int page,
                                                    @RequestParam(name = "size") int size) {
        return services.listCommissionPaginated(page, size);
    }

    @GetMapping("/program/{program}")
    public List<Commission> getByProgram(@PathVariable String program) {
        return services.findByProgram(program);
    }

    //Obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        Optional<Commission> c = services.getById(id);
        if (c.isPresent()) {
            return ResponseEntity.ok(c.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> add(@RequestBody CommissionDTO dto) {
        try {
            Commission c = new Commission(dto);
            services.add(c);
            return ResponseEntity.ok(c);
        }catch (CommissionAlreadyExistsException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommission(@PathVariable(name = "id") Long id){
        return services.delete(id);
    }

    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCommission(@PathVariable Long id, @RequestBody CommissionDTO dto){
        try {
            Commission c = new Commission(dto);
            services.modify(id, c);
            return ResponseEntity.ok(c);
        }catch (CommissionAlreadyExistsException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }

}

