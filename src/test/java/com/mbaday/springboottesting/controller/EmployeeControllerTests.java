package com.mbaday.springboottesting.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbaday.springboottesting.model.Employee;
import com.mbaday.springboottesting.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;


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

            // this will return whatever argument we passed to createEmployee method
            BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class))).
                    willAnswer((invocation)->invocation.getArgument(0));

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

                BDDMockito.given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

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
                            .id(1L)
                            .lastName("Mbah")
                            .firstName("Somtochukwu")
                            .email("victorsomtochukwu@gmail.com")
                            .build();
                    long employeeId = 1L;

                    BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));


                    // when- action or behaviour we are going to test

                    ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employeeId));
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
                .id(1L)
                .lastName("Mbah")
                .firstName("Somtochukwu")
                .email("victorsomtochukwu@gmail.com")
                .build();
        long employeeId = 1L;

        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());


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
                    .id(1L)
                    .lastName("Mbah")
                    .firstName("Somtochukwu")
                    .email("victorsomtochukwu@gmail.com")
                    .build();

            Employee updatedEmployee = Employee.builder()
                    .id(1L)
                    .lastName("Mbadady")
                    .firstName("Victor")
                    .email("sopiaNnadi@gmail.com")
                    .build();
            long employeeId = 1L;

            BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));

            BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class))).
                    willAnswer(invocation->invocation.getArgument(0));

            // when- action or behaviour we are going to test

            ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}",
                    employeeId)
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
                .id(1L)
                .lastName("Mbah")
                .firstName("Somtochukwu")
                .email("victorsomtochukwu@gmail.com")
                .build();

        Employee updatedEmployee = Employee.builder()
                .id(1L)
                .lastName("Mbadady")
                .firstName("Victor")
                .email("sopiaNnadi@gmail.com")
                .build();
        long employeeId = 1L;

        BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class))).
                willAnswer(invocation->invocation.getArgument(0));

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

            long employeeId = 1L;

            BDDMockito.willDoNothing().given(employeeService).deleteById(employeeId);

            // when- action or behaviour we are going to test

           ResultActions response =  mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}",
                    employeeId));
            // the- verify the output

            response.andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print());
        }
}
