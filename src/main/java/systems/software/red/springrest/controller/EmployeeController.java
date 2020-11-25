package systems.software.red.springrest.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import systems.software.red.springrest.EmployeeModelAssembler;
import systems.software.red.springrest.error.EmployeeNotFoundException;
import systems.software.red.springrest.repository.EmployeeRepository;
import systems.software.red.springrest.entity.Employee;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {
    private final EmployeeRepository repository;
    private final EmployeeModelAssembler assembler;

    public EmployeeController(
        EmployeeRepository repository,
        EmployeeModelAssembler employeeModelAssembler) {

        this.repository = repository;
        this.assembler = employeeModelAssembler;
    }

    @GetMapping("/employees")
    public CollectionModel<EntityModel<Employee>> all() {
        List<EntityModel<Employee>> employees = repository.findAll().stream()
            .map(assembler::toModel)
            .collect(Collectors.toList());

        return CollectionModel.of(employees,
            linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    @GetMapping("/employees/{id}")
    public EntityModel<Employee> one(@PathVariable long id){
        Employee employee = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));

        return assembler.toModel(employee);

    }

    @PostMapping("/employees")
    ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee){

        EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
    }

    @PutMapping("/employees/{id}")
    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable long id){
        Employee replacedEmployee = repository.findById(id)
            .map(employee -> {
                employee.setName(newEmployee.getName());
                employee.setRole(newEmployee.getRole());
                return repository.save(employee);
            }).orElseGet(() -> {
                newEmployee.setId(id);
                return repository.save(newEmployee);
            });

        EntityModel<Employee> entityModel = assembler.toModel(replacedEmployee);

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(replacedEmployee);
    }

    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable long id){
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
