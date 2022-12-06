package com.mbaday.springboottesting.service.serviceImpl;

import com.mbaday.springboottesting.exception.ResourceNotFoundException;
import com.mbaday.springboottesting.model.Employee;
import com.mbaday.springboottesting.repository.EmployeeRepository;
import com.mbaday.springboottesting.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {



    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());

        if(savedEmployee.isPresent()){
            throw new ResourceNotFoundException("Employee with email "+ employee.getEmail() + " already exists");
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {

//        Optional<Employee> employee = employeeRepository.findById(id);
//
//        if(employee.isEmpty()){
//            throw new ResourceNotFoundException("Employee does not exist with id: "+ id);
//        }
        return employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee(Employee updatedEmployee) {
        return employeeRepository.save(updatedEmployee);
    }

    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }


}
