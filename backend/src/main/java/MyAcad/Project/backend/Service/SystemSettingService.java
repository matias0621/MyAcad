package MyAcad.Project.backend.Service;

import MyAcad.Project.backend.Repository.SystemSettingRepository;
import MyAcad.Project.backend.Util.SystemSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SystemSettingService {

    private final SystemSettingRepository systemSettingRepository;

    public boolean isCourseEvaluationEnabled() {
        return systemSettingRepository.findById("COURSE_EVALUATIONS_ENABLED")
                .map(s -> Boolean.parseBoolean(s.getValue()))
                .orElse(false);
    }

    public void setCourseEvaluationEnabled(boolean enabled) {
        SystemSetting systemSetting = new SystemSetting();
        systemSetting.setKey("COURSE_EVALUATIONS_ENABLED");
        systemSetting.setValue(String.valueOf(enabled));
        systemSettingRepository.save(systemSetting);
    }
}
