package dev.freaks.BADProject02.service;

import dev.freaks.BADProject02.dto.employee.EmployeeCreateDto;
import dev.freaks.BADProject02.dto.employee.EmployeeResponseDto;
import dev.freaks.BADProject02.dto.employee.EmployeeUpdateDto;
import dev.freaks.BADProject02.dto.employee.TopPaidEmployeeDto;

import java.util.List;

public interface EmployeeService {
    List<EmployeeResponseDto> getAllEmployees();
    EmployeeResponseDto createEmployee(EmployeeCreateDto employeeCreateDto);
    EmployeeResponseDto getEmployeeById(Integer id);
    EmployeeResponseDto updateEmployee(Integer id, EmployeeUpdateDto employeeUpdateDto);
    void deleteEmployee(Integer id);
    List<TopPaidEmployeeDto> getTop10HighestPaidEmployees();
}
