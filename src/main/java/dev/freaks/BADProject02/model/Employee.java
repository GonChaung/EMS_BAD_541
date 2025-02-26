package dev.freaks.BADProject02.model;

import dev.freaks.BADProject02.model.constant.Gender;
import dev.freaks.BADProject02.model.converter.GenderConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "employees")
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @Column(name = "emp_no")
    private Integer empNo;

    @Column(name = "birth_date", columnDefinition = "DATE")
    private LocalDate birthDate;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Convert(converter = GenderConverter.class)
    @Column(name = "gender", columnDefinition = "ENUM('M', 'F')")
    private Gender gender;

    @Column(name = "hire_date", columnDefinition = "DATE")
    private LocalDate hireDate;

    // Fetch department info
    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @OrderBy("fromDate DESC")
    private List<DeptEmp> deptEmpList;

    // Fetch title info
    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @OrderBy("fromDate DESC")
    private List<Title> titleList;

    // Fetch salary info
    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @OrderBy("fromDate DESC")
    private List<Salary> salaryList;
}


