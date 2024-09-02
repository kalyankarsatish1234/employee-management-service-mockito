package com.javatechie.spring.mockito.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatechie.spring.mockito.api.dto.EmployeeDTO;
import com.javatechie.spring.mockito.api.service.EmployeeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeControllerTestCase {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private ObjectMapper om = new ObjectMapper();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    public void addEmployeeTest() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1);  // Ensure that the ID is set in the DTO for consistency
        employeeDTO.setName("satish");
        employeeDTO.setDept("IT");

        String jsonRequest = om.writeValueAsString(employeeDTO);

        // Mock the service layer
        when(employeeService.addEmployee(employeeDTO)).thenReturn(employeeDTO);

        MvcResult result = mockMvc.perform(post("/EmployeeService/addEmployee")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        EmployeeDTO responseDTO = om.readValue(resultContent, EmployeeDTO.class);

        Assert.assertNotNull(responseDTO);
        Assert.assertEquals("satish", responseDTO.getName());
        Assert.assertEquals("IT", responseDTO.getDept());
        Assert.assertEquals(1, responseDTO.getId());  // Ensure the ID matches
    }

    @Test
    public void getEmployeeByIdTest() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1);
        employeeDTO.setName("Satish");
        employeeDTO.setDept("IT");

        // Correctly mock the service method
        when(employeeService.getEmployeeById(1)).thenReturn(Optional.of(employeeDTO));

        MvcResult result = mockMvc.perform(get("/EmployeeService/getEmployee/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        EmployeeDTO responseDTO = om.readValue(resultContent, EmployeeDTO.class);

        Assert.assertNotNull(responseDTO);
        Assert.assertEquals("Satish", responseDTO.getName());
        Assert.assertEquals("IT", responseDTO.getDept());
        Assert.assertEquals(1, responseDTO.getId());  // Ensure the ID matches
    }

    @Test
    public void getAllEmployees() throws Exception {
        EmployeeDTO employeeDTO1 = new EmployeeDTO();
        employeeDTO1.setId(1);
        employeeDTO1.setName("Satish");
        employeeDTO1.setDept("IT");

        EmployeeDTO employeeDTO2 = new EmployeeDTO();
        employeeDTO2.setId(2); // Ensure different IDs for different employees
        employeeDTO2.setName("neha");
        employeeDTO2.setDept("HR");

        List<EmployeeDTO> employeeDTOList = Arrays.asList(employeeDTO1, employeeDTO2);
        when(employeeService.getAllEmployees()).thenReturn(employeeDTOList);

        MvcResult result = mockMvc.perform(get("/EmployeeService/getEmployees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String resultContent = result.getResponse().getContentAsString();
        EmployeeDTO[] responseDTOs = om.readValue(resultContent, EmployeeDTO[].class);

        Assert.assertNotNull(responseDTOs);
        Assert.assertEquals(2, responseDTOs.length);
        Assert.assertEquals("Satish", responseDTOs[0].getName());
        Assert.assertEquals("neha", responseDTOs[1].getName());
    }

    @Test
    public void deleteEmployeeById() throws Exception {
        int employeeId = 1;

        System.out.println("Request to delete employee with ID: " + employeeId);

        doNothing().when(employeeService).deleteEmployeeById(employeeId);

        MvcResult result = mockMvc.perform(delete("/EmployeeService/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println("Response status: " + result.getResponse().getStatus());

        verify(employeeService).deleteEmployeeById(employeeId);
    }

}
