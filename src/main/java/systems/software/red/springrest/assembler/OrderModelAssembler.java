package systems.software.red.springrest.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import systems.software.red.springrest.Status;
import systems.software.red.springrest.controller.OrderController;
import systems.software.red.springrest.entity.Order;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

    @Override
    public EntityModel<Order> toModel(Order order) {
        // Unconditional links to single-item resource and aggregate root
        EntityModel<Order> orderModel = EntityModel.of(order,
            linkTo(methodOn(OrderController.class).one(order.getId())).withSelfRel(),
            linkTo(methodOn(OrderController.class).all()).withRel("orders"));

        // Conditional links based on state of the order
        if (order.getStatus() == Status.IN_PROGRESS){
            // Aggiungo i link per le operazioni effettuabili
            // L'ordine può essere solo 'cancellato' oppure 'completato' quando si trova IN_PROGRESS
            orderModel.add(linkTo(methodOn(OrderController.class).cancel(order.getId())).withRel("cancel"));
            orderModel.add(linkTo(methodOn(OrderController.class).complete(order.getId())).withRel("complete"));
        }

        return orderModel;
    }
}
