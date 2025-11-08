package MyAcad.Project.backend.Service.Programs;

import MyAcad.Project.backend.Exception.CourseAlreadyExistsException;
import MyAcad.Project.backend.Mapper.CourseMapper;
import MyAcad.Project.backend.Model.Programs.Course;
import MyAcad.Project.backend.Model.Programs.CourseResponse;
import MyAcad.Project.backend.Repository.Programs.CourseRepository;
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
    private final CourseMapper courseMapper;

    public void add(Course c){
        if(repository.findCourseByName(c.getName()).isPresent()){
            throw new RuntimeException("Course name already exists");
        }
        repository.save(c);
    }

    public List<CourseResponse> getByNameIgnoringCase(String name) {
        return courseMapper.toResponseList(repository.findByNameContainingIgnoreCase(name));
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

    public List<CourseResponse> list() {
        return courseMapper.toResponseList(repository.findAll());
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

    public Optional<CourseResponse> getById(Long id){

        return repository.findById(id).map(courseMapper::toResponse);
    }

    public Page<Course> listCoursesPaginated(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }

}
