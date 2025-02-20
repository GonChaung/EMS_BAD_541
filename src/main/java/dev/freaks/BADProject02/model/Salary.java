package dev.freaks.BADProject02.model;

import dev.freaks.BADProject02.model.composite.SalaryId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "salaries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(SalaryId.class)
public class Salary {

    @Id
    @Column(name = "emp_no")
    private Integer empNo;

    @Id
    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "salary")
    private Integer salary;

    @Column(name = "to_date")
    private Date toDate;

    @ManyToOne
    @JoinColumn(name = "emp_no", insertable = false, updatable = false)
    private Employee employee;
}