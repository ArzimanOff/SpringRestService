package org.arzimanoff.springrestservice.datasource;

import org.arzimanoff.springrestservice.model.entity.Employee;
import org.arzimanoff.springrestservice.repository.EmployeeRepository;
import org.arzimanoff.springrestservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.arzimanoff.springrestservice.model.entity.Status;
import org.arzimanoff.springrestservice.model.entity.Order;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(
            EmployeeRepository employeeRepository,
            OrderRepository orderRepository
    ) {

        return args -> {
            employeeRepository.save(new Employee("Ivan", "Ivanov", "Designer"));
            employeeRepository.save(new Employee("Nikita", "Ivanov", "Cook"));
            employeeRepository.save(new Employee("Ahmed", "Arzimanov", "Senior Java Developer"));

            employeeRepository.findAll().forEach(employee -> log.info("Preloaded " + employee));

            orderRepository.save(new Order("MacBook Pro", Status.COMPLETED));
            orderRepository.save(new Order("iPhone", Status.IN_PROGRESS));

            orderRepository.findAll().forEach(order -> {
                log.info("Preloaded " + order);
            });
        };
    }
}
