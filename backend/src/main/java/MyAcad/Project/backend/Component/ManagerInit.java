package MyAcad.Project.backend.Component;

import MyAcad.Project.backend.Model.Users.Manager;
import MyAcad.Project.backend.Model.Users.ManagerDTO;
import MyAcad.Project.backend.Service.Users.ManagerService;
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
                    "123",
                    true
            );
            Manager manager = new Manager(dto);
            managerService.add(manager);
        }
    }
}
