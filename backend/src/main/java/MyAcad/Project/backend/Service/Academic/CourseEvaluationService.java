package MyAcad.Project.backend.Service.Academic;

import MyAcad.Project.backend.Mapper.CourseEvaluationMapper;
import MyAcad.Project.backend.Mapper.TeacherMapper;
import MyAcad.Project.backend.Model.Academic.CourseEvaluationDTO;
import MyAcad.Project.backend.Model.Academic.CourseEvaluationEntity;
import MyAcad.Project.backend.Model.Academic.CourseEvaluationResponse;
import MyAcad.Project.backend.Model.Academic.SubjectsEntity;
import MyAcad.Project.backend.Model.Users.Teacher;
import MyAcad.Project.backend.Repository.Academic.CourseEvaluationRepository;
import MyAcad.Project.backend.Repository.Academic.SubjectsRepository;
import MyAcad.Project.backend.Repository.Users.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CourseEvaluationService {
    private final CourseEvaluationRepository courseEvaluationRepository;
    private final SubjectService subjectService;
    private final SubjectsRepository subjectsRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    public void createCourseEvaluation(CourseEvaluationDTO courseEvaluationDTO) {
        SubjectsEntity subjects = subjectsRepository.findById(courseEvaluationDTO.getSubjectId()).orElseThrow(() -> new RuntimeException("Subject not found"));
        Teacher teacher = teacherRepository.findById(courseEvaluationDTO.getTeacherId()).orElseThrow(() -> new RuntimeException("Teacher not found"));

        CourseEvaluationEntity courseEvaluation = CourseEvaluationEntity.builder()
                .createdAt(LocalDateTime.now())
                .subject(subjects)
                .teacher(teacher)
                .feedback(courseEvaluationDTO.getFeedback())
                .build();

        courseEvaluationRepository.save(courseEvaluation);
    }

    public List<CourseEvaluationResponse> getAllCourseEvaluations() {
        return courseEvaluationRepository.findAll().stream().map(this::mapCourseEvaluation).toList();
    }

    public List<CourseEvaluationResponse> getAllCourseEvaluationsBySubjectId(Long subjectId) {
        return courseEvaluationRepository.findBySubject_Id(subjectId).stream().map(this::mapCourseEvaluation).toList();
    }

    public List<CourseEvaluationResponse> getAllCourseEvaluationsByTeacherId(Long teacherId) {
        return courseEvaluationRepository.findByTeacher_Id(teacherId).stream().map(this::mapCourseEvaluation).toList();
    }

    public Page<CourseEvaluationResponse> getAllCourseEvaluationsPaginated(int page ,int size){
        Page<CourseEvaluationEntity> pageEvaluation = courseEvaluationRepository.findAll(PageRequest.of(page, size));
        return pageEvaluation.map(this::mapCourseEvaluation);
    }

    public Page<CourseEvaluationResponse> getAllCourseEvaluationsPaginatedBySubjectId(Long subjectId, int page , int size){
        Page<CourseEvaluationEntity> pageEvaluation = courseEvaluationRepository.findBySubject_Id(subjectId, PageRequest.of(page, size));
        return pageEvaluation.map(this::mapCourseEvaluation);
    }

    public Page<CourseEvaluationResponse> getAllCourseEvaluationsPaginatedByTeacherId(Long teacherId, int page, int size){
        Page<CourseEvaluationEntity> pageEvaluation = courseEvaluationRepository.findByTeacher_Id(teacherId, PageRequest.of(page, size));
        return pageEvaluation.map(this::mapCourseEvaluation);
    }

    public Page<CourseEvaluationResponse> getAllCourseEvaluationsPaginatedByDate(LocalDateTime date , int page, int size){
        Page<CourseEvaluationEntity> pageEvaluation = courseEvaluationRepository.findByCreatedAt(date, PageRequest.of(page, size));
        return pageEvaluation.map(this::mapCourseEvaluation);
    }

    public CourseEvaluationResponse getCourseEvaluationById(Long courseEvaluationId) {
        return mapCourseEvaluation(courseEvaluationRepository.findById(courseEvaluationId).orElseThrow(() -> new RuntimeException("Course Evaluation Not Found")));
    }


    private CourseEvaluationResponse mapCourseEvaluation(CourseEvaluationEntity e) {
        CourseEvaluationResponse res = new CourseEvaluationResponse();
        res.setId(e.getId());
        res.setFeedback(e.getFeedback());
        res.setCreatedAt(e.getCreatedAt());

        // 👇 USO DEL MAPEADO MANUAL DESDE EL SERVICE
        res.setSubject(
                subjectService.mapSubjectManually(e.getSubject())
        );

        res.setTeacher(
                teacherMapper.toResponse(e.getTeacher())
        );

        return res;
    }

}
