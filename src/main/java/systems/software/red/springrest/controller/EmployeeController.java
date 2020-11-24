package systems.software.red.springrest.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import systems.software.red.springrest.error.EmployeeNotFoundException;
import systems.software.red.springrest.repository.EmployeeRepository;
import systems.software.red.springrest.entity.Employee;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all(){
        List<EntityModel<Employee>> employees = employeeRepository.findAll().stream()
            .map(employee ->
                // Creo l'EntityModel per ogni Employee estratto dalla query
                EntityModel.of(employee,
                    linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
                    linkTo(methodOn(EmployeeController.class).all()).withRel("employees")

                )
            ).collect(Collectors.toList());

        return CollectionModel.of(employees,
            linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));

        return EntityModel.of(employee,
            linkTo(methodOn(EmployeeController.class).one(id)).withSelfRel(),
            linkTo(methodOn(EmployeeController.class).all()).withRel("employees")
            );
    }

    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee newEmployee){
        return employeeRepository.save(newEmployee);
    }

    @PutMapping("/employees/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable long id){
        return employeeRepository.findById(id)
            .map(employee -> {
                employee.setName(newEmployee.getName());
                employee.setRole(newEmployee.getRole());
                return employeeRepository.save(employee);
            }).orElseGet(() -> {
                newEmployee.setId(id);
                return employeeRepository.save(newEmployee);
            });
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable long id){
        employeeRepository.deleteById(id);
    }
}
