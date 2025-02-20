package dev.freaks.BADProject02.service;

import dev.freaks.BADProject02.dto.employee.EmployeeCreateDto;
import dev.freaks.BADProject02.dto.employee.EmployeeResponseDto;
import dev.freaks.BADProject02.dto.employee.EmployeeUpdateDto;

import java.util.List;

public interface EmployeeService {
    List<EmployeeResponseDto> getAllEmployees();
    EmployeeResponseDto createEmployee(EmployeeCreateDto employeeCreateDto);
    EmployeeResponseDto getEmployeeById(Long id);
    EmployeeResponseDto updateEmployee(Long id, EmployeeUpdateDto employeeUpdateDto);
    void deleteEmployee(Long id);
}
