package dev.freaks.BADProject02.dto.employee;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ResignationRequest {
    private LocalDate resignDate;
}
