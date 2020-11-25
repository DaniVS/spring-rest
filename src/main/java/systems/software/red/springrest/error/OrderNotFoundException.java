package systems.software.red.springrest.error;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Could not found Order: " + id);
    }
}
