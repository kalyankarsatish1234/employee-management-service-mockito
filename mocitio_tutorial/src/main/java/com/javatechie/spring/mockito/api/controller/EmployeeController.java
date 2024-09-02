package com.javatechie.spring.mockito.api.controller;

import com.javatechie.spring.mockito.api.dto.EmployeeDTO;
import com.javatechie.spring.mockito.api.model.Response;
import com.javatechie.spring.mockito.api.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/EmployeeService")
public class EmployeeController {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	@Autowired
	private EmployeeService employeeService;

	@PostMapping("/addEmployee")
	public ResponseEntity<EmployeeDTO> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
		EmployeeDTO addedEmployee = employeeService.addEmployee(employeeDTO);
//		Response response = new Response(addedEmployee.getId() + " inserted", Boolean.TRUE);
		return new ResponseEntity<>(addedEmployee, HttpStatus.CREATED);
	}

	@GetMapping("/getEmployees")
	public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
		List<EmployeeDTO> employees = employeeService.getAllEmployees();
		//Response response = new Response("record counts: " + employees.size(), Boolean.TRUE);
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}

	@GetMapping("/getEmployee/{id}")
	public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable int id) {
		Optional<EmployeeDTO> employee = employeeService.getEmployeeById(id);
		return employee.map(employeeDTO ->
				new ResponseEntity<>(employeeDTO, HttpStatus.OK)
		).orElseGet(() ->
				new ResponseEntity<>(HttpStatus.NOT_FOUND)
		);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteEmployeeById(@PathVariable int id) {
		logger.debug("Request to delete employee with id: {}", id);
		employeeService.deleteEmployeeById(id);
		logger.debug("Employee with id {} deleted successfully", id);
		return ResponseEntity.ok().build(); // Use build() instead of HttpStatus.NO_CONTENT
	}

}
