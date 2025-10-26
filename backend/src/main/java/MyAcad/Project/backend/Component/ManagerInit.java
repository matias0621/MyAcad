package MyAcad.Project.backend.Component;

import MyAcad.Project.backend.Model.Users.Manager;
import MyAcad.Project.backend.Model.Users.ManagerDTO;
import MyAcad.Project.backend.Service.ManagerService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ManagerInit {
    private final ManagerService managerService;

    @PostConstruct
    public void createSuperUser() {
        if(managerService.list().isEmpty()){
            ManagerDTO dto = new ManagerDTO(
                    "manager",
                    "1",
                    1,
                    "manager1@gmail.com",
                    "123"
            );
            Manager manager = new Manager(dto);
            managerService.add(manager);
        }
    }
}
