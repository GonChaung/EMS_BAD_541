package dev.freaks.BADProject02.mapper;

import dev.freaks.BADProject02.dto.employee.EmployeeResponseDto;
import dev.freaks.BADProject02.dto.employee.MasterEmployeeDto;
import dev.freaks.BADProject02.model.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper extends BaseMapper<Employee, MasterEmployeeDto> {

    @Override
    Employee toEntity(MasterEmployeeDto masterEmployeeDto);

    @Override
    EmployeeResponseDto toDto(Employee employee);
}

