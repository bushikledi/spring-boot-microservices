package org.orderservice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.orderservice.dto.StockMessage;
import org.orderservice.model.enums.OrderStatus;
import org.orderservice.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockEventConsumer {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "stockQueue")
    public void receiveMessage(String message, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {

        try {
        StockMessage stockMessage = objectMapper.readValue(message, StockMessage.class);

        switch (routingKey) {
            case "order.success":
                orderService.updateOrderStatus(stockMessage.getOrderId(), OrderStatus.COMPLETED);
                break;
            case "order.failed":
                orderService.updateOrderStatus(stockMessage.getOrderId(), OrderStatus.FAILED);
                break;
            default:
                handleUnknown(routingKey, message);
                break;
        }
        } catch (Exception e) {
            System.err.println("Failed to parse message: " + e.getMessage());
        }
    }

    private void handleUnknown(String routingKey, String message) {
        System.err.println("Handling unknown routing key: " + routingKey + " with message: " + message);
    }
}
