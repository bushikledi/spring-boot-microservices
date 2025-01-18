package org.stockservice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.stockservice.dto.OrderUpdate;
import org.stockservice.model.enums.OrderStatus;

@Service
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${rabbitmq.exchange.stock}")
    private String exchange;

    @Value("${rabbitmq.routing-key.order.success}")
    private String successRoutingKey;

    @Value("${rabbitmq.routing-key.order.failed}")
    private String failedRoutingKey;

    public void updateSuccessOrderEvent(Long orderId) {
        try {
            String productJson = objectMapper.writeValueAsString(orderId);
            rabbitTemplate.convertAndSend(exchange, successRoutingKey, productJson);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize product to JSON", e);
        }
    }

    public void updateFailedOrderEvent(Long orderId) {
        try {
            OrderUpdate orderUpdate = new OrderUpdate();
            orderUpdate.setOrderId(orderId);
            String productJson = objectMapper.writeValueAsString(orderUpdate);
            rabbitTemplate.convertAndSend(exchange, failedRoutingKey, productJson);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize product to JSON", e);
        }
    }
}
