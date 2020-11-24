package systems.software.red.springrest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import systems.software.red.springrest.entity.Employee;
import systems.software.red.springrest.repository.EmployeeRepository;

@Configuration
class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository){
        return args -> {
            log.info("Preloading " + repository.save(new Employee("Bilbo Baggins","burglar")));
            log.info("Preloading " + repository.save(new Employee("Frodo Baggins","thief")));
        };
    }
}
