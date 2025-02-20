package dev.freaks.BADProject02.mapper;

import java.util.List;
import org.mapstruct.MappingTarget;

public interface BaseMapper<T,D>{
    D toDto(T entity); // Convert from entity to DTO
    T toEntity(D dto); // Convert from DTO to entity
    List<D> toDtoList(List<T> entityList); // Convert a list of entities to a list of DTOs
    List<T> toEntityList(List<D> dtoList); // Convert a list of DTOs to a list of entities
    void updateEntityFromDto(D dto, @MappingTarget T entity); // Update an existing entity from a DTO
}
