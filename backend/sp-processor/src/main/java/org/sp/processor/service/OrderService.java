package org.sp.processor.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.sp.processor.domain.order.Orders;
import org.sp.processor.helper.exception.PVException;
import org.sp.processor.repository.OrderRepository;

import java.util.List;

@ApplicationScoped
public class OrderService {

    private final Logger LOG = Logger.getLogger(OrderService.class);

    @ConfigProperty(name = "CONSTANTS.PROPERTIES.STATUS_ORDER.PENDING")
    private String orderStatusPending;

    @ConfigProperty(name = "CONSTANTS.PROPERTIES.STATUS_ORDER.COMPLETED")
    private String orderStatusCompleted;

    @ConfigProperty(name = "CONSTANTS.PROPERTIES.STATUS_ORDER.CANCELED")
    private String orderStatusCanceled;

    @Inject
    private OrderRepository orderRepository;

    public List<Orders> getOrders() {
        LOG.infof("@getOrders SERV > Starting order retrieval process");

        List<Orders> ordersList = orderRepository.findAllWithDetails();
        LOG.infof("@getOrders SERV > Retrieved %s orders from the database", ordersList.size());

        validExistOrders(ordersList);

        LOG.infof("@getOrders SERV > Returning order list");
        return ordersList;
    }

    private void validExistOrders(List<Orders> ordersList) {
        LOG.infof("@validExistOrders SERV > Validating order existence");

        if (ordersList.isEmpty()) {
            LOG.warnf("@validExistOrders SERV > No orders found in the database");
            throw new PVException(Response.Status.NOT_FOUND.getStatusCode(), "No se encontraron pedidos registrados.");
        }

        LOG.infof("@validExistOrders SERV > Order existence validated successfully");
    }

    public Orders getOrdersByNumberTable(Long numberTable) {
        LOG.infof("@getOrdersByNumberTable SERV > Searching orders for table number: %s", numberTable);

        Orders order = findPendingOrderByTable(numberTable);

        LOG.infof("@getOrdersByNumberTable SERV > Order found: %s for table number: %s", order.getIdOrder(), numberTable);
        return order;
    }

    public void changeStatusOrder(Long numberTable, String newStatus) {
        LOG.infof("@changeStatusOrder SERV > Changing status to %s for table number: %s", newStatus, numberTable);

        Orders order = findPendingOrderByTable(numberTable);

        order.setStatus(newStatus);
        orderRepository.save(order);

        LOG.infof("@changeStatusOrder SERV > Status changed to %s for order: %s", newStatus, order.getIdOrder());
    }

    private Orders findPendingOrderByTable(Long numberTable) {
        Orders order = orderRepository.findOrderByNumberTableWithDetails(numberTable, orderStatusPending);

        if (order == null) {
            LOG.warnf("@findPendingOrderByTable SERV > No pending orders found for table number: %s", numberTable);
            throw new PVException(Response.Status.NOT_FOUND.getStatusCode(),
                    "No se encontraron detalles de pedidos pendientes de la mesa: " + numberTable + ".");
        }

        return order;
    }




}
