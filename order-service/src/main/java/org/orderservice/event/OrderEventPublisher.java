package org.orderservice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.orderservice.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    @Value("${rabbitmq.exchange.order}")
    private String exchange;
    @Value("${rabbitmq.routing-key.stock.decrease}")
    private String decreaseRoutingKey;
    @Value("${rabbitmq.routing-key.stock.increase}")
    private String increaseRoutingKey;
    @Value("${rabbitmq.routing-key.stock.cancel}")
    private String cancelRoutingKey;

    public void decreaseStockEvent(Order order) {
        try {
            String productJson = objectMapper.writeValueAsString(order);
            rabbitTemplate.convertAndSend(exchange, decreaseRoutingKey, productJson);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize product to JSON", e);
        }
    }

    public void cancelStockEvent(Order order) {
        try {
            String productJson = objectMapper.writeValueAsString(order);
            rabbitTemplate.convertAndSend(exchange, cancelRoutingKey, productJson);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize product to JSON", e);
        }
    }

    public void increaseStockEvent(Order order) {
        try {
            String productJson = objectMapper.writeValueAsString(order);
            rabbitTemplate.convertAndSend(exchange, increaseRoutingKey, productJson);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize product to JSON", e);
        }
    }
}
