package dev.freaks.BADProject02.model;

import dev.freaks.BADProject02.model.composite.DeptManagerId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "dept_manager")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(DeptManagerId.class)
public class DeptManager {

    @Id
    @Column(name = "emp_no")
    private Integer empNo;

    @Id
    @Column(name = "dept_no", columnDefinition = "CHAR(4)")
    private String deptNo;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "to_date")
    private Date toDate;

    @ManyToOne
    @JoinColumn(name = "emp_no", insertable = false, updatable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "dept_no", insertable = false, updatable = false)
    private Department department;
}
