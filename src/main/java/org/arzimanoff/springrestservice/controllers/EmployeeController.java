package org.arzimanoff.springrestservice.controllers;

import org.arzimanoff.springrestservice.exceptions.EmployeeNotFoundException;
import org.arzimanoff.springrestservice.model.entity.Employee;
import org.arzimanoff.springrestservice.repository.EmployeeRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {
    private EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> getAllEmployeeList() {
        var employees = employeeRepository.findAll().stream()
                .map(employee ->
                        EntityModel.of(employee,
                                linkTo(methodOn(EmployeeController.class).getOneEmployee(employee.getId())).withSelfRel(),
                                linkTo(methodOn(EmployeeController.class).getAllEmployeeList()).withRel("employees")
                        )
                )
                .toList();
        return CollectionModel.of(employees,
                linkTo(methodOn(EmployeeController.class).getAllEmployeeList()).withSelfRel());
    }

    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee newEmployee) {
        return employeeRepository.save(newEmployee);
    }

    @GetMapping("/employees/{id}")
    EntityModel<Employee> getOneEmployee(@PathVariable Long id) {
        var employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).getOneEmployee(id)).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getAllEmployeeList()).withRel("employees")
        );
    }

    @PutMapping("/employees/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

        return employeeRepository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return employeeRepository.save(employee);
                })
                .orElseGet(() -> employeeRepository.save(newEmployee));
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        employeeRepository.deleteById(id);
    }
}
