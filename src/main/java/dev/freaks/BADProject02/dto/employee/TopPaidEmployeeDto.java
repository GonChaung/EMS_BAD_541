package dev.freaks.BADProject02.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopPaidEmployeeDto {
    private Integer empNo;
    private String firstName;
    private String lastName;
    private String deptName;
    private String title;
    private Double maxSalary;
}
