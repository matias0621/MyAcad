package MyAcad.Project.backend.Model.RegistrationStudent;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterStudentToCommissionByCsv {

    @CsvBindByName(column = "legajo")
    private String legajo;
}
