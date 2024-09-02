package com.javatechie.spring.mockito.api.mapper;


import com.javatechie.spring.mockito.api.dto.EmployeeDTO;
import com.javatechie.spring.mockito.api.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    EmployeeDTO toDTO(Employee employee);
    Employee toEntity(EmployeeDTO employeeDTO);
}
