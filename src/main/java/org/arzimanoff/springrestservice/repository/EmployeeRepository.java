package org.arzimanoff.springrestservice.repository;

import org.arzimanoff.springrestservice.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
