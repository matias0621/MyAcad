package MyAcad.Project.backend.Controller.Academic;

import MyAcad.Project.backend.Configuration.UserDetailsImpl;
import MyAcad.Project.backend.Exception.CommissionAlreadyExistsException;
import MyAcad.Project.backend.Exception.InscriptionException;
import MyAcad.Project.backend.Model.Academic.Commission;
import MyAcad.Project.backend.Model.Academic.CommissionDTO;
import MyAcad.Project.backend.Model.Academic.CommissionResponse;
import MyAcad.Project.backend.Model.RegistrationStudent.RegisterStudentToCommissionByCsv;
import MyAcad.Project.backend.Model.RegistrationStudent.RegistrationRequest;
import MyAcad.Project.backend.Model.Users.Student;
import MyAcad.Project.backend.Model.Users.StudentCsvDto;
import MyAcad.Project.backend.Model.Users.Teacher;
import MyAcad.Project.backend.Service.Academic.CommissionService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public Page<CommissionResponse> listCommissionPaginated(@RequestParam(name = "page") int page,
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

    @GetMapping("/program/not-enrolled/{program}")
    public List<CommissionResponse> getByProgramNotEnrolled(@PathVariable String program) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return services.listSubjectsNotEnrolled(program, userDetails.getId());
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

    @PostMapping("/register-student-by-csv/{commissionId}")
    public ResponseEntity<?> uploadStudentCSV(@RequestParam("file") MultipartFile file, @PathVariable Long commissionId) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo esta vacio");
        }

        try {
            List<RegisterStudentToCommissionByCsv> list = services.parseCsv(file);
            services.registerStudentByCsv(list, commissionId);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar el CSV: " + e.getMessage());
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
        try {
            services.registerStudentbyManager(request.getLegajo(), id, request.getSubjectsId());
            return ResponseEntity.ok().build();
        }catch (InscriptionException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/register-teacher-by-manager/{id}")
    public ResponseEntity<?> registerTeacher(@PathVariable Long id, @RequestBody RegistrationRequest request){
        try {
            services.registerTeacherToProgram(request.getLegajo(), id, request.getSubjectsId());
            return ResponseEntity.ok().build();
        }catch (InscriptionException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/register-commision-to-student/{id}")
    public ResponseEntity<?> registerCommissionToStudent(@PathVariable Long id, @RequestBody Long subjectsId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
        services.registerStudentByToken(user.getId(), id, subjectsId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/unregister-student/{studentId}/commission/{commissionId}")
    public ResponseEntity<?> unregisterStudent(@PathVariable Long studentId, @PathVariable Long commissionId, @RequestBody Long subjectsId){
        services.unregister(studentId, commissionId, subjectsId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<CommissionResponse>> getByTeacherId(@PathVariable Long teacherId) {
        return ResponseEntity.ok(services.findByTeacherId(teacherId));
    }

}

