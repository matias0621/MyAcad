package MyAcad.Project.backend.Repository;

import MyAcad.Project.backend.Util.SystemSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemSettingRepository extends JpaRepository<SystemSetting, String> {
}
