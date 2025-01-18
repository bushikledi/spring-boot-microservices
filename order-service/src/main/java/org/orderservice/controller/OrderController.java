package org.orderservice.controller;

import lombok.RequiredArgsConstructor;
import org.orderservice.model.Order;
import org.orderservice.dto.OrderRequest;
import org.orderservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest orderRequest) {
        Order order = orderService.placeOrder(orderRequest.getProductId(), orderRequest.getQuantity());
        return ResponseEntity.ok(order);
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId) {
        Order order = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(order);
    }
}

