package com.javatechie.spring.mockito.api.service;

import com.javatechie.spring.mockito.api.dao.EmployeeRepository;
import com.javatechie.spring.mockito.api.dto.EmployeeDTO;
import com.javatechie.spring.mockito.api.mapper.EmployeeMapper;
import com.javatechie.spring.mockito.api.model.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO) {
        logger.debug("Adding employee: {}", employeeDTO);
        Employee employee = employeeMapper.toEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        logger.debug("Added employee: {}", savedEmployee);
        return employeeMapper.toDTO(savedEmployee);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        logger.debug("Fetching all employees");
        List<Employee> employees = employeeRepository.findAll();
        logger.debug("Fetched employees: {}", employees);
        return employees.stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EmployeeDTO> getEmployeeById(int id) {
        logger.debug("Fetching employee by id: {}", id);
        Optional<Employee> employee = employeeRepository.findById(id);
        Optional<EmployeeDTO> employeeDTO = employee.map(employeeMapper::toDTO);
        logger.debug("Fetched employee: {}", employeeDTO.orElse(null));
        return employeeDTO;
    }

    @Override
    public void deleteEmployeeById(int id) {
        logger.debug("Deleting employee by id: {}", id);
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            logger.debug("Employee with id {} deleted", id);
        } else {
            logger.warn("Employee with id {} does not exist", id);
        }
    }
}
