package org.sp.processor.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.sp.processor.domain.order.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@ApplicationScoped
public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM Orders o LEFT JOIN FETCH o.detailsOrders")
    List<Orders> findAllWithDetails();

    @Query("SELECT o FROM Orders o LEFT JOIN FETCH o.detailsOrders WHERE o.numberTable = ?1 AND o.status = ?2")
    Orders findOrderByNumberTableWithDetails(Long numberTable, String orderStatusPending);

}
