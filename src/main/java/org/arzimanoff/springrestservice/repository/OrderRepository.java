package org.arzimanoff.springrestservice.repository;


import org.arzimanoff.springrestservice.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
