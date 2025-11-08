package MyAcad.Project.backend.Controller.Academic;

import MyAcad.Project.backend.Configuration.UserDetailsImpl;
import MyAcad.Project.backend.Exception.CommissionAlreadyExistsException;
import MyAcad.Project.backend.Model.Academic.Commission;
import MyAcad.Project.backend.Model.Academic.CommissionDTO;
import MyAcad.Project.backend.Model.Academic.CommissionResponse;
import MyAcad.Project.backend.Model.Programs.Course;
import MyAcad.Project.backend.Model.Programs.CourseDTO;
import MyAcad.Project.backend.Model.RegistrationStudent.RegistrationRequest;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.Teacher;
import MyAcad.Project.backend.Service.Academic.CommissionService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public List<CommissionResponse> listActiveCommissions() {
        return services.listActive();
    }

    @GetMapping()
    public List<CommissionResponse> listCommissions() {
        return services.list();
    }



    //Paginacion
    @GetMapping("/paginated")
    public Page<Commission> listCommissionPaginated(@RequestParam(name = "page") int page,
                                                    @RequestParam(name = "size") int size) {
        return services.listCommissionPaginated(page, size);
    }

    @GetMapping("/program/{program}")
    public List<CommissionResponse> getByProgram(@PathVariable String program) {
        return services.findByProgram(program);
    }

    @GetMapping("/program/info-student/{program}")
    public List<CommissionResponse> getByProgramInfoStudent(@PathVariable String program) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return services.listForStudentInfo(program, userDetails.getId());
    }

    //Obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        Optional<CommissionResponse> c = services.getById(id);
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

    // Baja definitiva
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> definitiveDeleteCommission(@PathVariable Long id) {
        return services.definitiveDeleteCommission(id);
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

    @PutMapping(value = "/add-subject/{id}")
    public ResponseEntity<?> addSubjectsToSubjects(@PathVariable Long id, @RequestBody Long subjects){
        services.addSubjectToCommission(id, subjects);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/delete-subject/{id}")
    public ResponseEntity<?> deleteSubjectsToSubjects(@PathVariable Long id, @RequestBody Long subjectsId){
        services.deleteSubjectsFromCommission(id, subjectsId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/register-student-by-manager/{id}")
    public ResponseEntity<?> registerStudent(@PathVariable Long id, @RequestBody RegistrationRequest request){
        services.registerStudentbyManager(request.getLegajo(), id, request.getSubjectsId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/register-teacher-by-manager/{id}")
    public ResponseEntity<?> registerTeacher(@PathVariable Long id, @RequestBody RegistrationRequest request){
        services.registerTeacherToProgram(request.getLegajo(), id, request.getSubjectsId());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @PutMapping("register-commision-to-student/{id}")
    public ResponseEntity<?> registerCommissionToStudent(@PathVariable Long id, @RequestBody Long subjectsId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
        services.registerStudentByToken(user.getId(), id, subjectsId);
        return ResponseEntity.ok().build();
    }

}

