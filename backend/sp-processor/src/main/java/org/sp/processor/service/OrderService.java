package org.sp.processor.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.sp.processor.domain.order.Orders;
import org.sp.processor.helper.exception.PVException;
import org.sp.processor.repository.OrderRepository;

import java.util.List;

@ApplicationScoped
public class OrderService {

    private final Logger LOG = Logger.getLogger(OrderService.class);

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
}
