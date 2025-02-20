package dev.freaks.BADProject02.model;

import dev.freaks.BADProject02.model.composite.TitleId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "titles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TitleId.class)
public class Title {

    @Id
    @Column(name = "emp_no")
    private Integer empNo;

    @Id
    @Column(name = "title")
    private String title;

    @Id
    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "to_date")
    private Date toDate;

    @ManyToOne
    @JoinColumn(name = "emp_no", insertable = false, updatable = false)
    private Employee employee;
}