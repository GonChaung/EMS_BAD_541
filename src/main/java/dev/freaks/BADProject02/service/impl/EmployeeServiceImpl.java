package dev.freaks.BADProject02.service.impl;

import dev.freaks.BADProject02.dto.employee.EmployeeCreateDto;
import dev.freaks.BADProject02.dto.employee.EmployeeResponseDto;
import dev.freaks.BADProject02.dto.employee.EmployeeUpdateDto;
import dev.freaks.BADProject02.exception.ResourceNotFoundException;
import dev.freaks.BADProject02.mapper.EmployeeMapper;
import dev.freaks.BADProject02.model.Employee;
import dev.freaks.BADProject02.model.constant.Gender;
import dev.freaks.BADProject02.repository.EmployeeRepository;
import dev.freaks.BADProject02.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }


    @Override
    public List<EmployeeResponseDto> getAllEmployees() {
        return List.of();
    }

    @Transactional
    @Override
    public EmployeeResponseDto createEmployee(EmployeeCreateDto employeeCreateDto) {
        Long maxEmpNo = employeeRepository.findMaxEmpNoWithLock(); // Assuming this returns a Long
        Integer nextEmpNo = maxEmpNo.intValue() + 1; // Cast Long to Integer and increment

        Employee employee = employeeMapper.toEntity(employeeCreateDto);
        employee.setEmpNo(nextEmpNo);

        employee = employeeRepository.save(employee);
        return employeeMapper.toDto(employee);
    }


    @Override
    public EmployeeResponseDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return employeeMapper.toDto(employee);
    }

    @Override
    public EmployeeResponseDto updateEmployee(Long id, EmployeeUpdateDto employeeUpdateDto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));

        // Validate and set gender
        String genderValue = employeeUpdateDto.getGender();
        if (genderValue != null) {
            try {
                // Try to match by value (M/F) first
                if (genderValue.equalsIgnoreCase("M")) {
                    employee.setGender(Gender.MALE);
                } else if (genderValue.equalsIgnoreCase("F")) {
                    employee.setGender(Gender.FEMALE);
                }
                // Then try to match by enum name
                else if (genderValue.equalsIgnoreCase("MALE")) {
                    employee.setGender(Gender.MALE);
                } else if (genderValue.equalsIgnoreCase("FEMALE")) {
                    employee.setGender(Gender.FEMALE);
                } else {
                    throw new IllegalArgumentException("Invalid gender value: " + genderValue);
                }
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid gender value: " + genderValue
                        + ". Please use M/F or MALE/FEMALE");
            }
        }

        employee.setFirstName(employeeUpdateDto.getFirstName());
        employee.setLastName(employeeUpdateDto.getLastName());
        employee.setBirthDate(employeeUpdateDto.getBirthDate());
        employee.setHireDate(employeeUpdateDto.getHireDate());

        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDto(updatedEmployee);
    }
    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id " + id);
        }

        employeeRepository.deleteById(id);
    }
}
