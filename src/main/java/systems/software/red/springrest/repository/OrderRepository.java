package systems.software.red.springrest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systems.software.red.springrest.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
