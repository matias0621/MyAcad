package MyAcad.Project.backend.Controller.Programs;


import MyAcad.Project.backend.Exception.CareerAlreadyExistsException;
import MyAcad.Project.backend.Exception.LegajoAlreadyExistsException;
import MyAcad.Project.backend.Model.Programs.Career;
import MyAcad.Project.backend.Model.Programs.CareerDTO;
import MyAcad.Project.backend.Service.Programs.CareerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/careers")
@AllArgsConstructor
public class CareerController {
    private CareerService services;

    @GetMapping()
    public List<Career> listCareers() {
        return services.list();
    }
    //Paginacion
    @GetMapping("/paginated")
    public Page<Career> listCareerPaginated(@RequestParam(name = "page") int page,
                                             @RequestParam(name = "size") int size) {
        return services.listCareersPaginated(page, size);
    }

    //Obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        Optional<Career> career = services.getById(id);
        if (career.isPresent()) {
            return ResponseEntity.ok(career.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> addCareer(@RequestBody CareerDTO dto) {
        try {
            Career career = new Career(dto);
            services.add(career);
            return ResponseEntity.ok(career);
        }catch (CareerAlreadyExistsException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCareer(@PathVariable(name = "id") Long id){
        return services.delete(id);
    }

    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCareer(@PathVariable Long id, @RequestBody CareerDTO dto){
        try {
            Career career = new Career(dto);
            services.modify(id, career);
            return ResponseEntity.ok(career);
        }catch (LegajoAlreadyExistsException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }
}
