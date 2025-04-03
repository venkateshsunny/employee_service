package com.elms.employeeservice.service;

import com.elms.employeeservice.entity.Employee;
import com.elms.employeeservice.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null); // Return null if not found
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee existingEmployee = getEmployeeById(id);
        existingEmployee.setName(employeeDetails.getName());
        existingEmployee.setDepartment(employeeDetails.getDepartment());
        existingEmployee.setRole(employeeDetails.getRole());
        existingEmployee.setEmail(employeeDetails.getEmail());
        return employeeRepository.save(existingEmployee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}