package MyAcad.Project.backend.Controller;


import MyAcad.Project.backend.Service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
public class SystemSettingController {
    private final SystemSettingService systemSettingService;

    @GetMapping("/course-evaluations")
    public boolean isEnabled() {
        return systemSettingService.isCourseEvaluationEnabled();
    }

    @PutMapping("/course-evaluations")
    public void setEnabled(@RequestParam boolean enabled) {
        systemSettingService.setCourseEvaluationEnabled(enabled);
    }
}
