package dev.freaks.BADProject02.model.composite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TitleId implements Serializable {
    private Integer empNo;
    private String title;
    private Date fromDate;
}