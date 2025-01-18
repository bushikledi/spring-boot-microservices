package org.stockservice.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StockRabbitMQConfig {

    @Value("${rabbitmq.exchange.stock}")
    private String stockExchange;
    @Value("${rabbitmq.queue.stock}")
    private String stockQueueName;
    @Value("${rabbitmq.routing-key.order.success}")
    private String successRoutingKey;
    @Value("${rabbitmq.routing-key.order.failed}")
    private String failedRoutingKey;

    @Bean
    public Queue stockQueue() {
        return new Queue(stockQueueName, true);
    }

    @Bean
    public DirectExchange stockExchange() {
        return new DirectExchange(stockExchange);
    }


    @Bean
    public Binding failedStockBinding(Queue stockQueue, DirectExchange stockExchange) {
        return BindingBuilder.bind(stockQueue).to(stockExchange).with(failedRoutingKey);
    }


    @Bean
    public Binding successStockBinding(Queue stockQueue, DirectExchange stockExchange) {
        return BindingBuilder.bind(stockQueue).to(stockExchange).with(successRoutingKey);
    }
}
