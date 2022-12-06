package com.mbaday.springboottesting.service;

import com.mbaday.springboottesting.exception.ResourceNotFoundException;
import com.mbaday.springboottesting.model.Employee;
import com.mbaday.springboottesting.repository.EmployeeRepository;
import com.mbaday.springboottesting.service.serviceImpl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AssertionFailureBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class) // used to tell junit that we are using annotations
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setUp() {
//        employeeRepository = Mockito.mock(EmployeeRepository.class); this is now replaced with @Mock annotation
//        employeeService = new EmployeeServiceImpl(employeeRepository); this is now replaced with @InjectMock Annotation
        employee = Employee.builder()
                .id(1L)
                .email("victorsomtochukwu@gmail.com")
                .lastName("Mbah")
                .firstName("Somtochukwu")
                .build();
    }

    // Junit for savedEmployee method

    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        // given- precondition or setup
//    stubbing of method after creating the object of Employee
//            Employee employee = Employee.builder()
//                    .email("victorsomtochukwu@gmail.com")
//                    .lastName("Mbah")
//                    .firstName("Somtochukwu")
//                    .build();

        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());

        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        // when- action or behaviour we are going to test

        Employee savedEmployee = employeeService.saveEmployee(employee);
        // the- verify the output

        Assertions.assertThat(savedEmployee).isNotNull();

    }
    // Junit for savedEmployee method that throws exception

    @Test
    public void givenExistingEmail_whenSaveEmployee_thenReturnException() {
        // given- precondition or setup
//    stubbing of method after creating the object of Employee

        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).
                willReturn(Optional.of(employee));

        // when- action or behaviour we are going to test

        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () ->
                employeeService.saveEmployee(employee));
        // the- verify the output

        // this will ensure that the statement exits without returning the saved employee after throwing error
        Mockito.verify(employeeRepository, Mockito.never()).save(Mockito.any(Employee.class));

    }

    // Junit for get all employee method (Positive scenario). When there is actually a list of employees

    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnListOfEmployees() {
        // given- precondition or setup
        Employee employee1 = Employee.builder()
                .email("victorsomtochukwu@gmail.com")
                .lastName("Mbah")
                .firstName("Somtochukwu")
                .build();

        // when- action or behaviour we are going to test
        BDDMockito.given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        List<Employee> employeeList = employeeService.getAllEmployees();

        // the- verify the output

        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isEqualTo(2);


    }
    // Junit for get all employee method (Negative scenario). When there is an empty list

    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyListOfEmployees() {
        // given- precondition or setup
        Employee employee1 = Employee.builder()
                .email("victorsomtochukwu@gmail.com")
                .lastName("Mbah")
                .firstName("Somtochukwu")
                .build();

        // when- action or behaviour we are going to test
        BDDMockito.given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        List<Employee> employeeList = employeeService.getAllEmployees();

        // the- verify the output

        Assertions.assertThat(employeeList).isEmpty();
        Assertions.assertThat(employeeList.size()).isEqualTo(0);
    }

    // Junit for get Employee by id

        @Test
        public void givenEmployeeObject_whenGetEmployeeById_thenReturnEmployeeObject(){
            // given- precondition or setup

            BDDMockito.given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
            // when- action or behaviour we are going to test

            Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();
            // the- verify the output

            Assertions.assertThat(savedEmployee).isNotNull();
        }

        // Junit for updating Employee information

            @Test
            public void givenEmployeeObject_whenUpdateEmployee_thenReturnEmployeeObject(){
                // given- precondition or setup
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
        employee.setFirstName("Emeka");
        employee.setEmail("azubuine.emeka@gmail.com");

                // when- action or behaviour we are going to test
        Employee updatedEmployee = employeeService.updateEmployee(employee);
                // the- verify the output

        Assertions.assertThat(employee.getFirstName()).isEqualTo("Emeka");
        Assertions.assertThat(employee.getEmail()).isEqualTo("azubuine.emeka@gmail.com");
            }

            // Junit for delete employee by id

            @Test
            public void givenEmployeeId_whenDeleteId_thenNothing(){
            // given- precondition or setup

                BDDMockito.willDoNothing().given(employeeRepository).deleteById(1L);

            // when- action or behaviour we are going to test

                employeeService.deleteById(1L);

            // the- verify the output

                Mockito.verify(employeeRepository, Mockito.times(1)).deleteById(1L);

                }
}

