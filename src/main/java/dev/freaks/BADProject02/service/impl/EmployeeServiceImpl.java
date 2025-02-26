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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        Long maxEmpNo = employeeRepository.findMaxEmpNoWithLock();
        Integer nextEmpNo = maxEmpNo.intValue() + 1;

        // Create Employee
        Employee employee = employeeMapper.toEntity(employeeCreateDto);
        employee.setEmpNo(nextEmpNo);

        // Ensure lists are initialized
        employee.setDeptEmpList(new ArrayList<>());
        employee.setTitleList(new ArrayList<>());
        employee.setSalaryList(new ArrayList<>());

        // Save Employee first
        employee = employeeRepository.save(employee);

        // Assign Department if provided
        if (employeeCreateDto.getDepartmentNo() != null) {
            Department department = departmentRepository.findById(employeeCreateDto.getDepartmentNo())
                    .orElseThrow(() -> new RuntimeException("Department not found"));

            DeptEmp deptEmp = new DeptEmp(employee, department, LocalDate.now(), LocalDate.of(9999, 1, 1));
            deptEmpRepository.save(deptEmp);

            employee.getDeptEmpList().add(deptEmp);
        }

        // Retrieve the Employee object based on nextEmpNo
        Employee employeeFind = employeeRepository.findById(nextEmpNo)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Assign Title if provided
        if (employeeCreateDto.getTitle() != null) {
            Title title = new Title(
                    employeeFind, // Pass Employee object here
                    employeeCreateDto.getTitle(),
                    LocalDate.now(), // fromDate
                    LocalDate.of(9999, 1, 1) // toDate
            );
            titleRepository.save(title);
            employee.getTitleList().add(title); // Ensure Title is added to the employee's title list
        }

        // Assign Salary if provided
        if (employeeCreateDto.getSalary() != null) {
            Salary salary = new Salary(
                    employeeFind, // Pass Employee object here
                    LocalDate.now(), // fromDate
                    employeeCreateDto.getSalary(),
                    LocalDate.of(9999, 1, 1) // toDate
            );
            salaryRepository.save(salary);
            employee.getSalaryList().add(salary); // Ensure Salary is added to the employee's salary list
        }


        // Map response DTO
        EmployeeResponseDto responseDto = employeeMapper.toDto(employee);
        responseDto.setDepartmentNo(employeeCreateDto.getDepartmentNo());
        responseDto.setTitle(employeeCreateDto.getTitle());
        responseDto.setSalary(employeeCreateDto.getSalary());

        return responseDto;
    }





    @Override
    public EmployeeResponseDto getEmployeeById(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        // Check if employee is null (this is an extra precaution, but generally, it shouldn't be null here)
        if (employee == null) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }

        return employeeMapper.toDto(employee);
    }



    @Transactional
    @Override
    public EmployeeResponseDto updateEmployee(Integer empNo, EmployeeUpdateDto employeeUpdateDto) {
        // Fetch the existing employee
        Employee employee = employeeRepository.findByEmpNo(empNo)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Update employee details
        updateEmployeeDetails(employee, employeeUpdateDto);

        // Handle department change if applicable
        if (employeeUpdateDto.getDepartmentNo() != null) {
            updateEmployeeDepartment(employee, employeeUpdateDto.getDepartmentNo());
        }

        // Handle title change if applicable
        if (employeeUpdateDto.getTitle() != null) {
            updateEmployeeTitle(employee, employeeUpdateDto.getTitle());
        }

        // Handle salary change if applicable
        if (employeeUpdateDto.getSalary() != null) {
            updateEmployeeSalary(employee, employeeUpdateDto.getSalary());
        }

        // Save the updated employee and return the DTO
        employee = employeeRepository.save(employee);
        return new EmployeeResponseDto(employee);  // Should work now
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
    public void deleteEmployee(Integer id) {
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

    private void updateEmployeeDetails(Employee employee, EmployeeUpdateDto employeeUpdateDto) {
        // You may want to check if certain fields need to be updated
        if (employeeUpdateDto.getFirstName() != null) {
            employee.setFirstName(employeeUpdateDto.getFirstName());
        }
        if (employeeUpdateDto.getLastName() != null) {
            employee.setLastName(employeeUpdateDto.getLastName());
        }
        // Add more fields as required (gender, birthDate, hireDate should not be updated)
    }

    @Transactional
    public void updateEmployeeDepartment(Employee employee, String newDeptNo) {
        Department newDepartment = departmentRepository.findById(newDeptNo)
                .orElseThrow(() -> new RuntimeException("Department record not found for employee"));

        // Set the end date for the previous department record (if exists)
        deptEmpRepository.findByEmpNo(employee.getEmpNo()).ifPresent(existingDeptEmp -> {
            existingDeptEmp.setToDate(LocalDate.now());
            deptEmpRepository.save(existingDeptEmp);
        });

        // Create a new department assignment
        DeptEmp newDeptEmp = new DeptEmp(employee, newDepartment, LocalDate.now(), LocalDate.of(9999, 1, 1));
        deptEmpRepository.save(newDeptEmp);
    }



    @Transactional
    public void updateEmployeeTitle(Employee employee, String newTitle) {
        // Try to find the current title for the employee
        Title title = employee.getTitleList().stream()
                .filter(t -> t.getEmpNo().equals(employee.getEmpNo()))
                .findFirst()
                .orElse(null); // Don't throw error, just return null if title not found

        if (title == null) {
            // If no title exists, create a new title for the employee
            title = new Title(
                    employee, // Pass Employee object here
                    newTitle,
                    LocalDate.now(), // fromDate
                    LocalDate.of(9999, 1, 1) // toDate
            );
            titleRepository.save(title); // Save the new title
            employee.getTitleList().add(title); // Add title to employee's list
        } else {
            // If title exists, update it by creating a new record with a new fromDate
            // Create a new title entity to avoid changing the primary key
            Title newTitleEntity = new Title(
                    employee, // Pass Employee object here
                    newTitle,
                    LocalDate.now(), // new fromDate
                    LocalDate.of(9999, 1, 1) // toDate
            );
            titleRepository.save(newTitleEntity); // Save the new title record
            employee.getTitleList().add(newTitleEntity); // Add to the employee's list
        }
    }



    @Transactional
    private void updateEmployeeSalary(Employee employee, Integer newSalary) {
        // Try to find the current salary record (active salary)
        Salary currentSalary = salaryRepository.findByEmpNoAndToDateIsNull(employee.getEmpNo())
                .orElse(null);  // Return null if no active salary found

        if (currentSalary != null) {
            // If a salary record exists, mark the old one as ended (set toDate)
            currentSalary.setToDate(LocalDate.now());
            salaryRepository.save(currentSalary); // Save the updated old salary record
        }

        // Create a new salary record with the updated salary value
        Salary newSalaryEntity = new Salary(
                employee.getEmpNo(),
                LocalDate.now(),  // Set current date as fromDate
                newSalary,        // New salary value
                LocalDate.of(9999, 1, 1),  // Default toDate
                employee           // Associate with employee
        );

        // Save the new salary record
        salaryRepository.save(newSalaryEntity);
    }

}
