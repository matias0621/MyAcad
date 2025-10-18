package MyAcad.Project.backend.Service;

import MyAcad.Project.backend.Exception.CareerAlreadyExistsException;
import MyAcad.Project.backend.Exception.CourseAlreadyExistsException;
import MyAcad.Project.backend.Model.Careers.Career;
import MyAcad.Project.backend.Model.Courses.Course;
import MyAcad.Project.backend.Repository.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CourseService {
    private final CourseRepository repository;

    public void add(Course c){
        if(repository.findCourseByName(c.getName()).isPresent()){
            throw new RuntimeException("Course name already exists");
        }
        repository.save(c);
    }

    public List<Course> getByNameIgnoringCase(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    public ResponseEntity<Void> delete(Long id) {
        Optional<Course> optionalCourse = repository.findById(id);
        if (optionalCourse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Course course = optionalCourse.get();
        course.setActive(false);
        repository.save(course);
        return ResponseEntity.noContent().build();
    }

    public List<Course> list() {
        return repository.findAll();
    }
    public void modify(Long id, Course c) {
        Optional<Course> optionalOld = repository.findById(id);
        if (optionalOld.isEmpty()) {
            throw new RuntimeException("Career not found");
        }
        Course old = optionalOld.get();
        if (!old.getName().equals(c.getName())) {
            if (repository.findCourseByName(c.getName()).isPresent()) {
                throw new CourseAlreadyExistsException();
            }
            old.setName(c.getName());
        }
        old.setDescription(c.getDescription());
        old.setDurationMonths(c.getDurationMonths());
        old.setMonthlyFee(c.getMonthlyFee());
        old.setAnnualFee(c.getAnnualFee());
        old.setActive(c.getActive());

        repository.save(old);
    }

    public Optional<Course> getById(Long id){
        return repository.findById(id);
    }

    public Page<Course> listCoursesPaginated(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

}
