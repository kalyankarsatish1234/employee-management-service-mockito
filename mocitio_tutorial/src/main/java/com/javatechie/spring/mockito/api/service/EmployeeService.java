package com.javatechie.spring.mockito.api.service;


import com.javatechie.spring.mockito.api.dto.EmployeeDTO;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    EmployeeDTO addEmployee(EmployeeDTO employeeDTO);
    List<EmployeeDTO> getAllEmployees();
    Optional<EmployeeDTO>  getEmployeeById(int id);
    void deleteEmployeeById(int id);
}
