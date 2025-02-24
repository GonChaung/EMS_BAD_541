package dev.freaks.BADProject02.service.impl;

import dev.freaks.BADProject02.dto.employee.EmployeeCreateDto;
import dev.freaks.BADProject02.dto.employee.EmployeeResponseDto;
import dev.freaks.BADProject02.dto.employee.EmployeeUpdateDto;
import dev.freaks.BADProject02.dto.employee.TopPaidEmployeeDto;
import dev.freaks.BADProject02.exception.ResourceNotFoundException;
import dev.freaks.BADProject02.mapper.EmployeeMapper;
import dev.freaks.BADProject02.model.*;
import dev.freaks.BADProject02.model.composite.DeptEmpId;
import dev.freaks.BADProject02.model.constant.Gender;
import dev.freaks.BADProject02.repository.*;
import dev.freaks.BADProject02.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentRepository departmentRepository;
    private final SalaryRepository salaryRepository;
    private final TitleRepository titleRepository;
    private final DeptEmpRepository deptEmpRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, DepartmentRepository departmentRepository, SalaryRepository salaryRepository, TitleRepository titleRepository, DeptEmpRepository deptEmpRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.departmentRepository = departmentRepository;
        this.salaryRepository = salaryRepository;
        this.titleRepository = titleRepository;
        this.deptEmpRepository = deptEmpRepository;
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
        // Create the employee entity
        Employee employee = employeeMapper.toEntity(employeeCreateDto);
        employee.setEmpNo(nextEmpNo);
        // Save the employee
        employee = employeeRepository.save(employee);
        // Handle department creation
        // Handle department, title, and salary creation
        if (employeeCreateDto.getDepartmentNo() != null) {
            Department department = departmentRepository.findById(employeeCreateDto.getDepartmentNo())
                    .orElseThrow(() -> new RuntimeException("Department not found"));

            // Create the composite key for DeptEmp
            DeptEmpId deptEmpId = new DeptEmpId(employee.getEmpNo(), employeeCreateDto.getDepartmentNo());

            // Set default to_date if not provided
            LocalDate toDate = LocalDate.of(9999, 01, 01); // Default far-future date

            // Create the DeptEmp entity with the default to_date
            DeptEmp deptEmp = new DeptEmp(deptEmpId, LocalDate.now(), toDate, employee, department);
            deptEmpRepository.save(deptEmp);
        }
        // Handle title creation
        if (employeeCreateDto.getTitle() != null) {
            LocalDate titleToDate = LocalDate.of(9999, 01, 01); // Set a default to_date for title
            Title title = new Title(employee.getEmpNo(), employeeCreateDto.getTitle(), LocalDate.now(), titleToDate, employee);
            titleRepository.save(title);
        }
        // Handle salary creation
        if (employeeCreateDto.getSalary() != null) {
            LocalDate salaryToDate = LocalDate.of(9999, 01, 01); // Set a default to_date for salary
            Salary salary = new Salary(employee.getEmpNo(), LocalDate.now(), employeeCreateDto.getSalary(), salaryToDate, employee);
            salaryRepository.save(salary);
        }
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

        // Update firstName if provided
        if (employeeUpdateDto.getFirstName() != null) {
            employee.setFirstName(employeeUpdateDto.getFirstName());
        }

        // Update lastName if provided
        if (employeeUpdateDto.getLastName() != null) {
            employee.setLastName(employeeUpdateDto.getLastName());
        }

        // Update birthDate if provided
        if (employeeUpdateDto.getBirthDate() != null) {
            employee.setBirthDate(convertToLocalDate(employeeUpdateDto.getBirthDate()));
        }

        // Update hireDate if provided
        if (employeeUpdateDto.getHireDate() != null) {
            employee.setHireDate(convertToLocalDate(employeeUpdateDto.getHireDate()));
        }

        // Update gender if provided
        if (employeeUpdateDto.getGender() != null) {
            employee.setGender(parseGender(employeeUpdateDto.getGender()));
        }

        // Save updated employee
        Employee updatedEmployee = employeeRepository.save(employee);

        // Convert to response DTO
        return employeeMapper.toDto(updatedEmployee);
    }

    /**
     * Helper method to convert Date to LocalDate.
     */
    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Helper method to parse gender from different formats.
     */
    private Gender parseGender(String genderValue) {
        return switch (genderValue.trim().toUpperCase()) {
            case "M", "MALE" -> Gender.MALE;
            case "F", "FEMALE" -> Gender.FEMALE;
            default -> throw new IllegalArgumentException("Invalid gender value: " + genderValue +
                    ". Please use M/F or MALE/FEMALE.");
        };
    }



    @Override
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id " + id);
        }

        employeeRepository.deleteById(id);
    }

    @Override
    public List<TopPaidEmployeeDto> getTop10HighestPaidEmployees() {
        List<Object[]> results = employeeRepository.findTop10HighestPaidEmployeesWithDepartment();
        return results.stream().map(obj -> {
            Integer empNo = (Integer) obj[0];
            String firstName = (String) obj[1];
            String lastName = (String) obj[2];
            String deptName = (String) obj[3];
            Double maxSalary = ((Number) obj[4]).doubleValue();
            // Fetch the title using empNo (separate query)
            String title = getTitleForEmployee(empNo);
            return new TopPaidEmployeeDto(empNo, firstName, lastName, deptName, title, maxSalary);
        }).toList();
    }


    public String getTitleForEmployee(Integer empNo) {
        // Fetch the title using the empNo (you can implement a repository call here)
        return employeeRepository.findTitleByEmpNo(empNo);  // Assuming you have a method for this
    }
}
