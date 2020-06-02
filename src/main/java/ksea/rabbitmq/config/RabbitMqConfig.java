package ksea.rabbitmq.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author k.sea
 */
@Configuration
public class RabbitMqConfig {


    // 给容器中设定消息转换成JSON的转换成器，如果容器中有则 rabbitTemplate 使用的就是我们的转换器

    @Bean
    public MessageConverter messageConverter() {

        return new Jackson2JsonMessageConverter();
    }

}
