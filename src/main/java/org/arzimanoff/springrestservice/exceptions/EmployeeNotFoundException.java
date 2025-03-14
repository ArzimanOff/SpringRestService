package org.arzimanoff.springrestservice.exceptions;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Long id) {
        super("Нет сотрудника с id = " + id);
    }
}
