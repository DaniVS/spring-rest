package systems.software.red.springrest.entity;

import lombok.Getter;
import lombok.Setter;
import systems.software.red.springrest.Status;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "CUSTOMER_ORDER")
public class Order {
    private @Id @GeneratedValue long id;

    private String description;
    private Status status;

    public Order() {}

    public Order(String description, Status status){
        this.description = description;
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Order)) return false;

        Order order = (Order) obj;
        return Objects.equals(this.id, order.id)
            && Objects.equals(this.description, order.description)
            && this.status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.description, this.status);
    }

    @Override
    public String toString() {
        return "Order {" +
            "id=" + this.id +
            ", description=" + this.description +
            ", status=" + this.status +
            "}";
    }
}
