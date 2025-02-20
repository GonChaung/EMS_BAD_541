package dev.freaks.BADProject02.dto.employee;

import dev.freaks.BADProject02.model.constant.Gender;
import lombok.Data;

import java.util.Date;

@Data
public class MasterEmployeeDto {
    private Date birthDate;
    private String firstName;
    private String lastName;
    private String gender;
    private Date hireDate;
}