package systems.software.red.springrest;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(long id) {
        super("Could not found Employee: " + id);
    }
}
