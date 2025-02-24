package dev.freaks.BADProject02.mapper;

import dev.freaks.BADProject02.dto.employee.EmployeeResponseDto;
import dev.freaks.BADProject02.dto.employee.MasterEmployeeDto;
import dev.freaks.BADProject02.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper extends BaseMapper<Employee, MasterEmployeeDto> {

    @Override
    @Mapping(target = "gender", expression = "java(Gender.fromString(masterEmployeeDto.getGender()))")
    Employee toEntity(MasterEmployeeDto masterEmployeeDto);

    @Override
    @Mapping(target = "id", source = "empNo")
    @Mapping(target = "gender", expression = "java(employee.getGender().toString())")
    EmployeeResponseDto toDto(Employee employee);
}

