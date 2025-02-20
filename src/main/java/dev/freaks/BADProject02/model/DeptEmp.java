package dev.freaks.BADProject02.model;

import dev.freaks.BADProject02.model.composite.DeptEmpId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "dept_emp")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(DeptEmpId.class)
public class DeptEmp {

    @Id
    @Column(name = "emp_no")
    private Integer empNo;

    @Id
    @Column(name = "dept_no", columnDefinition = "CHAR(4)") // Explicit column mapping
    private String deptNo;

    @Column(name = "from_date") // Explicit column mapping
    private Date fromDate;

    @Column(name = "to_date") // Explicit column mapping
    private Date toDate;

    @ManyToOne
    @JoinColumn(name = "emp_no", referencedColumnName = "emp_no", insertable = false, updatable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "dept_no", referencedColumnName = "dept_no", insertable = false, updatable = false)
    private Department department;
}
