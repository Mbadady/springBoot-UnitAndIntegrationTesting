package com.mbaday.springboottesting.repository;

import com.mbaday.springboottesting.model.Employee;
import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;


//    this is going to run before each test cases are ran
    @BeforeEach
    public void setUp(){
        employee = Employee.builder()
                .firstName("Somtochukwu")
                .lastName("Mbah")
                .email("victorsomtochukwu@gmail.com")
                .build();
    }

    // junit test for saved employee operation

//    @DisplayName("Junit test for saved employee operation") // displays this instead of the method name
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){
        // given- precondition or setup

//        Refactored***********

//        Employee employee = Employee.builder()
//                .firstName("Somtochukwu")
//                .lastName("Mbah")
//                .email("victorsomtochukwu@gmail.com")
//                .build();

        // when- action or behaviour we are going to test

        Employee savedEmployee = employeeRepository.save(employee);

        // the- verify the output
       assertThat(savedEmployee).isNotNull(); // remember assertThat is a static method and it is now fixed on the import statement
       assertThat(savedEmployee.getId()).isGreaterThan(0);

    }

    // Junit test for get all employees

    @Test
    public void givenListOfEmployees_whenFindAll_thenReturnListOfEmployees(){
        // given- precondition or setup

//        Refactored**********

//        Employee employee = Employee.builder()
//                .firstName("Somtochukwu")
//                .lastName("Mbah")
//                .email("Victorsomtochukwu@gmail.com")
//                .build();
        Employee employee2 = Employee.builder()
                .firstName("Chukwuemeka")
                .lastName("Azubuine")
                .email("azubuine.emeka@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);


        // when- action or behaviour we are going to test

        List<Employee> employeeList = employeeRepository.findAll();

        // the- verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);

    }

    // Junit test for finding employee by Id

    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject(){
        // given- precondition or setup

//        Refactored**********

//        Employee employee = Employee.builder()
//                .firstName("Somtochukwu")
//                .lastName("Mbah")
//                .email("victorsomtchukwu@gmail.com")
//                .build();

        employeeRepository.save(employee);

        // when- action or behaviour we are going to test

        Employee employeeDb = employeeRepository.findById(employee.getId()).get();

        // the- verify the output

        assertThat(employeeDb).isNotNull();
    }

    // Junit for finding employee by email

        @Test
        public void givenEmployeeObject_whenFindByEmail_thenReturnEmployeeObject(){
            // given- precondition or setup

//            Refactored**********

//            Employee employee = Employee.builder()
//                    .firstName("Somtochukwu")
//                    .lastName("Mbah")
//                    .email("victorsomtochukwu@gmail.com")
//                    .build();
            employeeRepository.save(employee);

            // when- action or behaviour we are going to test

            Employee employeeDb = employeeRepository.findByEmail(employee.getEmail()).get();

            // the- verify the output

            assertThat(employeeDb).isNotNull();
        }

        // Junit for updating employee details
            @DisplayName("Junit for updating employee details")
            @Test
            public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
                // given- precondition or setup

//                Refactored*************

//                Employee employee = Employee.builder()
//                        .firstName("Somtochukwu")
//                        .lastName("Mbah")
//                        .email("victorsomtochukwu@gmail.com")
//                        .build();
                employeeRepository.save(employee);

                // when- action or behaviour we are going to test

                Employee savedEmployee = employeeRepository.findById(employee.getId()).get();

                savedEmployee.setEmail("Mbadady@gmail.com");
                savedEmployee.setFirstName("Victor");

                Employee updatedEmployee = employeeRepository.save(savedEmployee);

                // the- verify the output

                assertThat(updatedEmployee.getEmail()).isEqualTo("Mbadady@gmail.com");
                assertThat(updatedEmployee.getFirstName()).isEqualTo("Victor");

            }

            // Junit for deleting an employee

                @Test
                public void givenEmployeeObject_whenDeleteById_thenDeleteObject(){
                    // given- precondition or setup

//                    Refactored*************

//                    Employee employee = Employee.builder()
//                            .firstName("Somtochukwu")
//                            .lastName("Mbah")
//                            .email("victorsomtochukwu@gmail.com")
//                            .build();
                    employeeRepository.save(employee);

                    // when- action or behaviour we are going to test

                    employeeRepository.delete(employee);
                    Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

                    // the- verify the output
                    assertThat(employeeOptional).isEmpty();
                }

                // // Junit for custom query using JPQL with index params

                    @Test
                    public void givenFirstNameAndLastName_whenFindByJPQL_thenEmployeeObject(){
                        // given- precondition or setup

//                        Refactored*************

//                        Employee employee = Employee.builder()
//                                .firstName("Somtochukwu")
//                                .lastName("Mbah")
//                                .email("victorsomtochukwu@gmail.com")
//                                .build();
                        employeeRepository.save(employee);

                        String firstName = "Somtochukwu";
                        String lastName = "Mbah";

                        // when- action or behaviour we are going to test

                        Employee savedEmployee = employeeRepository.findByJPQL(firstName, lastName);

                        // the- verify the output

                        assertThat(savedEmployee).isNotNull();

                    }

    // // Junit for custom query using JPQL with named params

    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParam_thenEmployeeObject(){
        // given- precondition or setup

//        Refactored*************

//        Employee employee = Employee.builder()
//                .firstName("Somtochukwu")
//                .lastName("Mbah")
//                .email("victorsomtochukwu@gmail.com")
//                .build();
        employeeRepository.save(employee);

        String firstName = "Somtochukwu";
        String lastName = "Mbah";

        // when- action or behaviour we are going to test

        Employee savedEmployee = employeeRepository.findByJPQLNamedParam(firstName, lastName);

        // the- verify the output

        assertThat(savedEmployee).isNotNull();

    }

    // Junit for custom query using native SQL with index params

    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenEmployeeObject(){
        // given- precondition or setup

//        Refactored*************

//        Employee employee = Employee.builder()
//                .firstName("Somtochukwu")
//                .lastName("Mbah")
//                .email("victorsomtochukwu@gmail.com")
//                .build();
        employeeRepository.save(employee);

//        String firstName = "Somtochukwu";
//        String lastName = "Mbah";

        // when- action or behaviour we are going to test

        Employee savedEmployee = employeeRepository.findByNativeSQL(employee.getFirstName(), employee.getLastName());

        // the- verify the output

        assertThat(savedEmployee).isNotNull();

    }

    // Junit for custom query using native SQL with named params

    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamed_thenEmployeeObject(){
        // given- precondition or setup

//        Refactored*************

//        Employee employee = Employee.builder()
//                .firstName("Somtochukwu")
//                .lastName("Mbah")
//                .email("victorsomtochukwu@gmail.com")
//                .build();
        employeeRepository.save(employee);

//        String firstName = "Somtochukwu";
//        String lastName = "Mbah";

        // when- action or behaviour we are going to test

        Employee savedEmployee = employeeRepository.findByNativeSQLNamedParam(employee.getFirstName(),
                employee.getLastName());

        // the- verify the output

        assertThat(savedEmployee).isNotNull();

    }
}
