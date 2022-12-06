package com.mbaday.springboottesting.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbaday.springboottesting.model.Employee;
import com.mbaday.springboottesting.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
//@Testcontainers. we have started the containers manually
public class EmployeeControllerITests extends AbstractContainerBaseTest {

    // to make http request using perform method
    @Autowired
    private MockMvc mockMvc;

    // to perform operation of deleteAll before each test
    @Autowired
    private EmployeeRepository employeeRepository;

    // for serialization and deserialization
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        employeeRepository.deleteAll();
    }

    // Junit for create Employee

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception{

        // given- precondition or setup
        Employee employee = Employee.builder()
                .id(1L)
                .lastName("Mbah")
                .firstName("Somtochukwu")
                .email("victorsomtochukwu@gmail.com")
                .build();

        // when- action or behaviour we are going to test

        // make rest api call

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // the- verify the output
        // andDo(MockMvcResultHandlers.print()) - helps to print to the console the expected value

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())));
    }

    // Junit test to get All Employees

    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenListOfEmployees() throws Exception{
        // given- precondition or setup
        List<Employee> listOfEmployees = new ArrayList<>();

        listOfEmployees.add(Employee.builder().lastName("Mbah").firstName("Somtochukwu")
                .email("victorsomtochukwu@gmail.com").build());
        listOfEmployees.add(Employee.builder().lastName("Azubuine").firstName("Chukwuemeka")
                .email("azubuine.emeka@gmail.com").build());
        employeeRepository.saveAll(listOfEmployees);

        // when- action or behaviour we are going to test

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));
        // the- verify the output

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
                        CoreMatchers.is(listOfEmployees.size())));
    }

    // Junit test for get employee by id (Positive scenario i.e when there is an employee with the id)

    @Test
    public void givenEmployeeObject_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception{
        // given- precondition or setup
        Employee employee = Employee.builder()
//                .id(1L)
                .lastName("Mbah")
                .firstName("Somtochukwu")
                .email("victorsomtochukwu@gmail.com")
                .build();
        employeeRepository.save(employee);
        // when- action or behaviour we are going to test

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}",
                employee.getId()));
        // the- verify the output

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(employee.getEmail())))
                .andDo(MockMvcResultHandlers.print());

    }

    // Junit test for get employee by id (Negative scenario i.e when there is an invalid employeeId provided)

    @Test
    public void givenEmployeeObject_whenGetEmployeeById_thenReturnEmptyEmployeeObject() throws Exception{
        // given- precondition or setup
        Employee employee = Employee.builder()
//                .id(1L)
                .lastName("Mbah")
                .firstName("Somtochukwu")
                .email("victorsomtochukwu@gmail.com")
                .build();
        long employeeId = 1L;

        employeeRepository.save(employee);

        // when- action or behaviour we are going to test

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employeeId));
        // the- verify the output

        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }

    // Junit for update employee by id (Positive scenario)

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception{
        // given- precondition or setup

        Employee savedEmployee = Employee.builder()
                .lastName("Mbah")
                .firstName("Somtochukwu")
                .email("victorsomtochukwu@gmail.com")
                .build();

        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .lastName("Mbadady")
                .firstName("Victor")
                .email("sopiaNnadi@gmail.com")
                .build();

        // when- action or behaviour we are going to test

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}",
                        savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        // the- verify the output

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                        CoreMatchers.is(updatedEmployee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",
                        CoreMatchers.is(updatedEmployee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                        CoreMatchers.is(updatedEmployee.getEmail())));

    }

    // Junit for update employee by id (Negative scenario)

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnEmptyEmployeeObject() throws Exception{
        // given- precondition or setup

        Employee savedEmployee = Employee.builder()
                .lastName("Mbah")
                .firstName("Somtochukwu")
                .email("victorsomtochukwu@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .lastName("Mbadady")
                .firstName("Victor")
                .email("sopiaNnadi@gmail.com")
                .build();
        long employeeId = 1L;

        // when- action or behaviour we are going to test

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}",
                        employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));
        // the- verify the output

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    // Junit for delete by id

    @Test
    public void givenEmployeeId_whenDeleteById_thenReturn200() throws Exception{
        // given- precondition or setup

        Employee savedEmployee = Employee.builder()
                .lastName("Mbah")
                .firstName("Somtochukwu")
                .email("victorsomtochukwu@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        // when- action or behaviour we are going to test

        ResultActions response =  mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}",
               savedEmployee.getId()));
        // the- verify the output

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

}
