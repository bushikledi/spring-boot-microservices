package org.stockservice.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderRabbitMQConfig {

    @Value("${rabbitmq.exchange.order}")
    private String orderExchange;
    @Value("${rabbitmq.queue.order}")
    private String orderQueueName;
    @Value("${rabbitmq.routing-key.stock.increase}")
    private String increaseRoutingKey;
    @Value("${rabbitmq.routing-key.stock.decrease}")
    private String decreaseRoutingKey;
    @Value("${rabbitmq.routing-key.stock.cancel}")
    private String cancelRoutingKey;

    @Bean
    public Queue orderQueue() {
        return new Queue(orderQueueName, true);
    }

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(orderExchange);
    }


    @Bean
    public Binding increaseOrderBinding(Queue orderQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(increaseRoutingKey);
    }


    @Bean
    public Binding decreaseOrderBinding(Queue orderQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(decreaseRoutingKey);
    }

    @Bean
    public Binding cancelOrderBinding(Queue orderQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(cancelRoutingKey);
    }
}
