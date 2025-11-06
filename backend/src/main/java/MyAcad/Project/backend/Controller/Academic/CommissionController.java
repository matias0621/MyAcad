package MyAcad.Project.backend.Controller.Academic;

import MyAcad.Project.backend.Configuration.UserDetailsImpl;
import MyAcad.Project.backend.Exception.CommissionAlreadyExistsException;
import MyAcad.Project.backend.Model.Academic.Commission;
import MyAcad.Project.backend.Model.Academic.CommissionDTO;
import MyAcad.Project.backend.Model.Programs.Course;
import MyAcad.Project.backend.Model.Programs.CourseDTO;
import MyAcad.Project.backend.Model.RegistrationStudent.RegistrationRequest;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.Teacher;
import MyAcad.Project.backend.Service.Academic.CommissionService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/commissions")
@RequiredArgsConstructor
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

    @PutMapping("/add-subject/{id}")
    public ResponseEntity<?> addSubjectsToSubjects(@PathVariable Long idCommission, @RequestBody List<Long> subjects){
        services.addSubjectsToCommission(idCommission, subjects);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/delete-subject/{id}")
    public ResponseEntity<?> deleteSubjectsToSubjects(@PathVariable Long idCommission, @RequestBody Long subjectsId){
        services.deleteSubjectsFromCommission(idCommission, subjectsId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("register-student-by-manager/{id}")
    public ResponseEntity<?> registerStudent(@PathVariable Long id, @RequestBody RegistrationRequest request){
        services.registerStudentbyManager(request.getStudentLegajo(), id, request.getSubjectsId());
        return ResponseEntity.ok().build();
    }

}

