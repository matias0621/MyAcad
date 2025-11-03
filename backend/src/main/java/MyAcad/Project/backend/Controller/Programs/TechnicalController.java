package MyAcad.Project.backend.Controller.Programs;

import MyAcad.Project.backend.Exception.LegajoAlreadyExistsException;
import MyAcad.Project.backend.Model.Programs.Technical;
import MyAcad.Project.backend.Model.Programs.TechnicalDTO;
import MyAcad.Project.backend.Service.Programs.TechnicalService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/technicals")
@AllArgsConstructor
public class TechnicalController {
    private final TechnicalService services;

    @GetMapping()
    public List<Technical> listTechnicals() {
        return services.list();
    }

    //Paginacion
    @GetMapping("/paginated")
    public Page<Technical> listTechnicalPaginated(@RequestParam(name = "page") int page,
                                            @RequestParam(name = "size") int size) {
        return services.listTechnicalPaginated(page, size);
    }

    //Obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        Optional<Technical> technical = services.getById(id);
        if (technical.isPresent()) {
            return ResponseEntity.ok(technical.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> addTechnical(@RequestBody TechnicalDTO dto) {
        try {
            Technical technical = new Technical(dto);
            services.add(technical);
            return ResponseEntity.ok(technical);
        }catch (LegajoAlreadyExistsException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnical(@PathVariable(name = "id") Long id){
        return services.delete(id);
    }

    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTechnical(@PathVariable Long id, @RequestBody TechnicalDTO dto){
        try {
            Technical technical = new Technical(dto);
            services.modify(id, technical);
            return ResponseEntity.ok(technical);
        }catch (LegajoAlreadyExistsException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }
}
