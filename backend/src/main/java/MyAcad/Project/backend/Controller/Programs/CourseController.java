package MyAcad.Project.backend.Controller.Programs;

import MyAcad.Project.backend.Enum.ProgramType;
import MyAcad.Project.backend.Exception.LegajoAlreadyExistsException;
import MyAcad.Project.backend.Model.Programs.*;
import MyAcad.Project.backend.Service.Programs.ProgramService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
@AllArgsConstructor
public class CourseController {
    private final ProgramService programService;

    //Paginacion
    @GetMapping("/paginated")
    public Page<ProgramResponse> listCoursesPaginated(@RequestParam(name = "page") int page,
                                                      @RequestParam(name = "size") int size) {
        return programService.listProgramPaginatedByProgramType(page, size, ProgramType.COURSE);
    }

    //Obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        Optional<ProgramResponse> course = Optional.ofNullable(programService.findProgramById(id));
        if (course.isPresent()) {
            return ResponseEntity.ok(course.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> add(@RequestBody ProgramsDTO dto) {
        try {
            programService.createCourse(dto);
            return ResponseEntity.ok().build();
        }catch (LegajoAlreadyExistsException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable(name = "id") Long id){
        programService.logicDeleteProgram(id);
        return ResponseEntity.ok().build();
    }

    // Baja definitiva
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> definitiveDeleteCourse(@PathVariable Long id) {
        programService.deleteProgram(id);
        return ResponseEntity.ok().build();
    }
    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody ProgramsDTO dto){
        try {
            programService.updateProgram(id, dto);
            return ResponseEntity.ok().build();
        }catch (LegajoAlreadyExistsException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }

    @GetMapping()
    public List<ProgramResponse> listCourses() {
        return programService.findByProgramType(ProgramType.COURSE);
    }

}
