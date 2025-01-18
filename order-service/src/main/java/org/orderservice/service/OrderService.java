package org.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.orderservice.event.OrderEventPublisher;
import org.orderservice.model.Order;
import org.orderservice.model.enums.OrderStatus;
import org.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;


    public Order placeOrder(String productId, int quantity) {

        Order order = new Order();
        order.setQuantity(quantity);
        order.setProductId(productId);
        order.setOrderStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);
        savedOrder.setOrderStatus(OrderStatus.PENDING);
        orderEventPublisher.decreaseStockEvent(savedOrder);

        return orderRepository.save(savedOrder);
    }

    public Order cancelOrder(Long orderId) {
        try {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (order.getOrderStatus() != OrderStatus.CANCELLED) {
                order.setOrderStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);
                orderEventPublisher.cancelStockEvent(order);
            }
                return order;
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
        }

    public void updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setOrderStatus(orderStatus);
            orderRepository.save(order);
        }
    }
    }

