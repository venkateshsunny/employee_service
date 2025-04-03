package com.elms.employeeservice.controller;

import com.elms.employeeservice.entity.Employee;
import com.elms.employeeservice.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createEmployee(@RequestBody Employee employee) {
        logger.info("POST /api/employees/create - Creating employee: {}", employee);
        try {
            Employee savedEmployee = employeeService.saveEmployee(employee);
            logger.debug("Employee saved successfully: {}", savedEmployee);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", HttpStatus.OK.value());
            response.put("timestamp", LocalDateTime.now());
            response.put("message", "Employee successfully saved to the database.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error occurred while saving employee: {}", employee, e);
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save Employee.");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllEmployees() {
        logger.info("GET /api/employees/all - Fetching all employees");
        try {
            List<Employee> employees = employeeService.getAllEmployees();
            logger.debug("Fetched employees: {}", employees);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", HttpStatus.OK.value());
            response.put("timestamp", LocalDateTime.now());
            response.put("message", "Fetched all employees successfully.");
            response.put("data", employees);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error occurred while fetching employees", e);
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch Employees.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getEmployeeById(@PathVariable("id") Long id) {
        logger.info("GET /api/employees/{} - Fetching employee by ID", id);
        try {
            Employee employee = employeeService.getEmployeeById(id);
            if (employee == null) {
                logger.warn("Employee not found with ID: {}", id);
                return buildErrorResponse(HttpStatus.NOT_FOUND, "Employee ID is not present in the database.");
            }

            logger.debug("Fetched employee with ID {}: {}", id, employee);
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", HttpStatus.OK.value());
            response.put("timestamp", LocalDateTime.now());
            response.put("message", "Fetched employee successfully.");
            response.put("data", employee);
            return ResponseEntity.ok(response);

        } catch (NoSuchElementException e) {
            logger.warn("Employee not found exception: {}", e.getMessage());
            return buildErrorResponse(HttpStatus.NOT_FOUND, "Employee ID is not present in the database.");
        } catch (Exception e) {
            logger.error("Error occurred while fetching employee with ID {}: {}", id, e.getMessage());
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateEmployee(@PathVariable("id") Long id, @RequestBody Employee employeeDetails) {
        logger.info("PUT /api/employees/update/{} - Updating employee with details: {}", id, employeeDetails);

        try {
            Employee existingEmployee = employeeService.getEmployeeById(id);
            if (existingEmployee == null) {
                logger.warn("Employee not found with ID: {}", id);
                return buildErrorResponse(HttpStatus.NOT_FOUND, "Employee ID is not present in the database.");
            }

            Employee updatedEmployee = employeeService.updateEmployee(id, employeeDetails);
            logger.debug("Employee updated successfully: {}", updatedEmployee);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", HttpStatus.OK.value());
            response.put("timestamp", LocalDateTime.now());
            response.put("message", "Employee successfully updated.");
            response.put("data", updatedEmployee);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error occurred while updating employee with ID {}: {}", id, e.getMessage());
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update Employee.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteEmployee(@PathVariable("id") Long id) {
        logger.info("DELETE /api/employees/delete/{} - Deleting employee", id);

        try {
            Employee existingEmployee = employeeService.getEmployeeById(id);
            if (existingEmployee == null) {
                logger.warn("Employee not found with ID: {}", id);
                return buildErrorResponse(HttpStatus.NOT_FOUND, "Employee ID is not present in the database.");
            }

            employeeService.deleteEmployee(id);
            logger.debug("Employee deleted successfully with ID: {}", id);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", HttpStatus.OK.value());
            response.put("timestamp", LocalDateTime.now());
            response.put("message", "Employee successfully deleted.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error occurred while deleting employee with ID {}: {}", id, e.getMessage());
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete Employee.");
        }
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", status.value());
        response.put("timestamp", LocalDateTime.now());
        response.put("message", message);
        response.put("data", null);
        return ResponseEntity.status(status).body(response);
    }
}