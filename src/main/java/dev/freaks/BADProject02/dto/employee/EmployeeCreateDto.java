package dev.freaks.BADProject02.dto.employee;

import lombok.Data;

@Data
public class EmployeeCreateDto extends MasterEmployeeDto {
    private String departmentNo; // The dept_no that links to the department
    private String title; // Title of the employee
    private Integer salary;
}
