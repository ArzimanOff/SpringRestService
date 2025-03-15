package org.arzimanoff.springrestservice.controllers;

import org.arzimanoff.springrestservice.exceptions.OrderNotFoundException;
import org.arzimanoff.springrestservice.mapper.OrderModelAssembler;
import org.arzimanoff.springrestservice.model.entity.Order;
import org.arzimanoff.springrestservice.model.entity.Status;
import org.arzimanoff.springrestservice.repository.OrderRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderController {
    private OrderRepository orderRepository;
    private OrderModelAssembler orderModelAssembler;

    public OrderController(OrderRepository orderRepository, OrderModelAssembler orderModelAssembler) {
        this.orderRepository = orderRepository;
        this.orderModelAssembler = orderModelAssembler;
    }

    @GetMapping("/orders")
    public CollectionModel<EntityModel<Order>> getAllOrders() {
        var orders = orderRepository.findAll().stream()
                .map(orderModelAssembler::toModel)
                .toList();

        return CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).getAllOrders()).withSelfRel());
    }

    @GetMapping("/orders/{id}")
    public EntityModel<Order> getOneOrder(@PathVariable Long id) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return orderModelAssembler.toModel(order);
    }

    @PostMapping("/orders")
    public ResponseEntity<EntityModel<Order>> newOrder(@RequestBody Order order) {
        order.setStatus(Status.IN_PROGRESS);
        var newOrder = orderRepository.save(order);

        return ResponseEntity
                .created(
                        linkTo(
                                methodOn(OrderController.class)
                                        .getOneOrder(newOrder.getId())
                        ).toUri()
                )
                .body(orderModelAssembler.toModel(newOrder));
    }

    @DeleteMapping("/orders/{id}/cancel")
    public ResponseEntity<?> cancel(@PathVariable Long id) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.CANCELLED);
            return ResponseEntity.ok(
                    orderModelAssembler.toModel(orderRepository.save(order))
            );
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Метод не доступен!")
                        .withDetail("Нельзя закрыть заказ, который находится в статусе " + order.getStatus())
                );
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<?> complete(@PathVariable Long id) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() == Status.IN_PROGRESS) {
            order.setStatus(Status.COMPLETED);
            return ResponseEntity.ok(
                    orderModelAssembler.toModel(orderRepository.save(order))
            );
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Метод не доступен!")
                        .withDetail("Нельзя завершить заказ, который находится в статусе " + order.getStatus())
                );
    }


}
