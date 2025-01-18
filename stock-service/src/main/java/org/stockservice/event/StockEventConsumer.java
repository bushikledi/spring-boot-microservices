package org.stockservice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.stockservice.dto.StockUpdate;
import org.stockservice.service.StockService;

@Service
@RequiredArgsConstructor
public class StockEventConsumer {

    private final StockService stockService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "orderQueue")
    public void receiveMessage(String message, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
        System.out.println("Received message: " + message + " with routing key: " + routingKey);
        try {
            StockUpdate stockUpdate = objectMapper.readValue(message, StockUpdate.class);
            System.out.println(routingKey);
            switch (routingKey) {
                case "stock.decrease":
                    stockService.decreaseStock(stockUpdate);
                    break;
                case "stock.increase":
                    stockService.increaseStock(stockUpdate, false);
                    break;
                case "stock.cancel":
                    stockService.increaseStock(stockUpdate, true);
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
        System.out.println("Handling unknown routing key: " + routingKey + " with message: " + message);
    }
}
