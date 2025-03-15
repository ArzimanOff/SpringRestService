package org.arzimanoff.springrestservice.controllers;

import org.arzimanoff.springrestservice.exceptions.EmployeeNotFoundException;
import org.arzimanoff.springrestservice.mapper.EmployeeModelAssembler;
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
    private EmployeeModelAssembler employeeModelAssembler;

    public EmployeeController(EmployeeRepository employeeRepository, EmployeeModelAssembler employeeModelAssembler) {
        this.employeeRepository = employeeRepository;
        this.employeeModelAssembler = employeeModelAssembler;
    }

    @GetMapping("/employees")
    public CollectionModel<EntityModel<Employee>> getAllEmployeeList() {
        var employees = employeeRepository
                .findAll()
                .stream()
                .map(employeeModelAssembler::toModel)
                .toList();
        return CollectionModel.of(employees,
                linkTo(methodOn(EmployeeController.class).getAllEmployeeList()).withSelfRel());
    }

    @PostMapping("/employees")
    public Employee newEmployee(@RequestBody Employee newEmployee) {
        return employeeRepository.save(newEmployee);
    }

    @GetMapping("/employees/{id}")
    public EntityModel<Employee> getOneEmployee(@PathVariable Long id) {
        var employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return employeeModelAssembler.toModel(employee);
    }

    @PutMapping("/employees/{id}")
    public Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

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
