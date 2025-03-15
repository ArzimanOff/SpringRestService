package org.arzimanoff.springrestservice.datasource;

import org.arzimanoff.springrestservice.model.entity.Employee;
import org.arzimanoff.springrestservice.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Employee("Ivan", "Ivanov", "Designer")));
            log.info("Preloading " + repository.save(new Employee("Nikita", "Ivanov", "Cook")));
            log.info("Preloading " + repository.save(new Employee("Ahmed", "Arzimanov", "Senior Java Developer")));
        };
    }
}
