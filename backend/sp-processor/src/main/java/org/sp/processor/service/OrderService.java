package org.sp.processor.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.sp.processor.domain.business.Business;
import org.sp.processor.domain.order.*;
import org.sp.processor.domain.product.Product;
import org.sp.processor.domain.user.User;
import org.sp.processor.helper.exception.PVException;
import org.sp.processor.repository.DetailOrderRepository;
import org.sp.processor.repository.OrderRepository;
import org.sp.processor.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Inject
    private DetailOrderRepository detailOrderRepository;

    @Inject
    private ProductRepository productRepository;

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

    public InformationOrderDTO getOrdersByNumberTable(long numberTable) {
        LOG.infof("@getOrdersByNumberTable SERV > Searching orders for table number: %s", numberTable);

        List<Orders> ordersList = findPendingOrderByTable(numberTable);
        double totalAmount = calculateTotalAmountOrder(ordersList);

        InformationOrderDTO informationOrderDTO = InformationOrderDTO.builder()
                .ordersListInfo(ordersList)
                .totalAmountOrder(totalAmount)
                .build();

        LOG.infof("@getOrdersByNumberTable SERV > Orders found: %s for table number: %s", ordersList.size(), numberTable);
        return informationOrderDTO;
    }

    private double calculateTotalAmountOrder(List<Orders> ordersList){
        return ordersList.stream()
                .flatMap(order -> order.getDetailsOrders().stream())
                .mapToDouble(DetailOrder::getSubTotal)
                .sum();
    }

    public void changeStatusOrder(Long numberTable, String newStatus) {
        LOG.infof("@changeStatusOrder SERV > Changing status to %s for table number: %s", newStatus, numberTable);

        List<Orders> orders = findPendingOrderByTable(numberTable);

        for (Orders order : orders) {
            order.setStatus(newStatus);
            orderRepository.save(order);
            LOG.infof("@changeStatusOrder SERV > Status changed to %s for order ID: %s", newStatus, order.getIdOrder());
        }

        LOG.infof("@changeStatusOrder SERV > Status changed to %s for orders: %s", newStatus, orders.size());
    }

    private List<Orders> findPendingOrderByTable(Long numberTable) {
        List<Orders> order = orderRepository.findOrderByNumberTableWithDetails(numberTable, orderStatusPending);

        if (order.isEmpty()) {
            LOG.warnf("@findPendingOrderByTable SERV > No pending orders found for table number: %s", numberTable);
            throw new PVException(Response.Status.NOT_FOUND.getStatusCode(),
                    "No se encontraron detalles de pedidos pendientes de la mesa: " + numberTable + ".");
        }

        return order;
    }

    public void createOrderByNumberTable(OrderDTO orderDTO) {
        LOG.infof("@createOrderByNumberTable SERV > Starting order creation for table number: %s", orderDTO.getTableNumber());

        Orders orders = buildOrder(orderDTO);
        orderRepository.save(orders);
        LOG.infof("@createOrderByNumberTable SERV > Order created with ID: %s for table number: %s", orders.getIdOrder(), orders.getNumberTable());

        long totalAmount = 0L;
        List<DetailOrder> detailOrders = new ArrayList<>();

        for (DetailOrderDTO detailOrderDTO : orderDTO.getDetailsOrders()) {
            LOG.infof("@createOrderByNumberTable SERV > Processing product with ID: %s", detailOrderDTO.getProductId());

            Product product = productRepository.findById(detailOrderDTO.getProductId());
            if (product == null) {
                LOG.warnf("@createOrderByNumberTable SERV > Product with ID: %s not found", detailOrderDTO.getProductId());
                continue;
            }

            long valueAmount = product.getValue() * detailOrderDTO.getQuantity();
            totalAmount += valueAmount;

            DetailOrder detailOrder = buildDetailOrder(orders, product, detailOrderDTO, valueAmount);
            detailOrders.add(detailOrder);
        }

        detailOrderRepository.saveAll(detailOrders);
        LOG.infof("@createOrderByNumberTable SERV > Saved %s order details for order ID: %s", detailOrders.size(), orders.getIdOrder());

        orders.setAmount(totalAmount);
        orderRepository.save(orders);
        LOG.infof("@createOrderByNumberTable SERV > Order ID: %s finalized with total amount: %s", orders.getIdOrder(), totalAmount);
    }

    private Orders buildOrder(OrderDTO orderDTO) {
        LOG.infof("@buildOrder SERV > Creating order object for table number: %s", orderDTO.getTableNumber());

        return Orders.builder()
                .businessId(Business.builder()
                        .idBusiness(orderDTO.getBusinessId())
                        .build())
                .numberTable(orderDTO.getTableNumber())
                .userId(User.builder()
                        .documentNumber(orderDTO.getUserId())
                        .build())
                .status(orderStatusPending)
                .dateCreation(LocalDateTime.now())
                .build();
    }

    private DetailOrder buildDetailOrder(Orders orders, Product product, DetailOrderDTO detailOrderDTO, long valueAmount) {
        LOG.infof("@buildDetailOrder SERV > Creating detail order for product ID: %s, quantity: %s, subtotal: %s",
                product.getIdProduct(), detailOrderDTO.getQuantity(), valueAmount);

        return DetailOrder.builder()
                .order(orders)
                .product(product)
                .priceUnit(product.getValue())
                .subTotal(valueAmount)
                .observation(detailOrderDTO.getObservation())
                .quantity(detailOrderDTO.getQuantity())
                .build();
    }
}
