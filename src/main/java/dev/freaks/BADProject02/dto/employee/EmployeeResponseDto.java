package dev.freaks.BADProject02.dto.employee;

import dev.freaks.BADProject02.model.Employee;
import lombok.Data;

@Data
public class EmployeeResponseDto {
    private Integer empNo;
    private String firstName;
    private String lastName;
    private String departmentNo;
    private String title;
    private Integer salary;
    private String gender;  // Add gender field

    // Constructor to initialize from Employee entity
    public EmployeeResponseDto(Employee employee) {
        if (employee != null) {
            this.empNo = employee.getEmpNo();
            this.firstName = employee.getFirstName();
            this.lastName = employee.getLastName();
            this.departmentNo = getLatestDepartment(employee);
            this.title = getLatestTitle(employee);
            this.salary = getLatestSalary(employee);
            this.gender = employee.getGender() != null ? employee.getGender().toString() : null; // Ensure gender is mapped
        }
        // If employee is null, initialize defaults or handle the error accordingly
        else {
            // Optionally, set defaults or throw an exception depending on your requirements
            this.empNo = null;
            this.firstName = null;
            this.lastName = null;
            this.departmentNo = null;
            this.title = null;
            this.salary = null;
            this.gender = null;
        }
    }


    // Methods to get the latest department, title, and salary
    private String getLatestDepartment(Employee employee) {
        return employee.getDeptEmpList() != null && !employee.getDeptEmpList().isEmpty() ?
                employee.getDeptEmpList().get(0).getDeptNo() : null;
    }

    private String getLatestTitle(Employee employee) {
        return employee.getTitleList() != null && !employee.getTitleList().isEmpty() ?
                employee.getTitleList().get(0).getTitle() : null;
    }

    private Integer getLatestSalary(Employee employee) {
        return employee.getSalaryList() != null && !employee.getSalaryList().isEmpty() ?
                employee.getSalaryList().get(0).getSalary() : null;
    }
}

