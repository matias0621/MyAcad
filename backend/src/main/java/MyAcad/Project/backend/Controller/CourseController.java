package MyAcad.Project.backend.Controller;

import MyAcad.Project.backend.Exception.UsernameAlreadyExistsException;
import MyAcad.Project.backend.Model.Programs.Course;
import MyAcad.Project.backend.Model.Programs.CourseDTO;
import MyAcad.Project.backend.Service.CourseService;
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
    private CourseService services;

    //Paginacion
    @GetMapping("/paginated")
    public Page<Course> listCoursesPaginated(@RequestParam(name = "page") int page,
                                             @RequestParam(name = "size") int size) {
        return services.listCoursesPaginated(page, size);
    }

    //Obtener por id
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        Optional<Course> course = services.getById(id);
        if (course.isPresent()) {
            return ResponseEntity.ok(course.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<?> add(@RequestBody CourseDTO dto) {
        try {
            Course course = new Course(dto);
            services.add(course);
            return ResponseEntity.ok(course);
        }catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable(name = "id") Long id){
        return services.delete(id);
    }

    //PUT
    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody CourseDTO dto){
        try {
            Course course = new Course(dto);
            services.modify(id, course);
            return ResponseEntity.ok(course);
        }catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.badRequest().body((e.getMessage()));
        }
    }

    @GetMapping()
    public List<Course> listCourses() {
        return services.list();
    }

}
