package MyAcad.Project.backend.Util;

import MyAcad.Project.backend.Repository.SystemSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SystemSettingsInitializer implements ApplicationRunner {
    private final SystemSettingRepository systemSettingRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createIfNotExists(
                SystemSettingKeys.COURSE_EVALUATIONS_ENABLED,
                "false"
        );
    }

    private void createIfNotExists(String key, String value) {
        if (!systemSettingRepository.existsById(key)) {
            SystemSetting systemSetting = new SystemSetting();
            systemSetting.setKey(key);
            systemSetting.setValue(value);
            systemSettingRepository.save(systemSetting);
        }
    }
}
