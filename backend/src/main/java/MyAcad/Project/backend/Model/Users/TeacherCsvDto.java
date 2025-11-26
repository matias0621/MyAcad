package MyAcad.Project.backend.Model.Users;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherCsvDto {

    @CsvBindByName(column = "dni")
    private String dni;

    @CsvBindByName(column = "nombre")
    private String name;

    @CsvBindByName(column = "apellido")
    private String lastname;

    @CsvBindByName(column = "email")
    private String email;
}
