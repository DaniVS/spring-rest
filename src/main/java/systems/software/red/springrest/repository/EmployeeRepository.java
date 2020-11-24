package systems.software.red.springrest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.software.red.springrest.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
