package ksea.rabbitmq.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author k.sea
 */
@Configuration
public class RabbitMqConfig {


    //注入RabbitTemplate组件

    @Autowired
    private RabbitTemplate rabbitTemplate;


    // 给容器中设定消息转换成JSON的转换成器，如果容器中有则 rabbitTemplate 使用的就是我们的转换器
    @Bean
    public MessageConverter messageConverter() {

        return new Jackson2JsonMessageConverter();
    }


    /**
     * 给RabbitTemplate 定制化，开启发送确认抵达回调
     */
    @PostConstruct
    public void initRabbitTemplate() {


        /***
         *1.ConfirmCallback触发的机制是生产者发送的消息成功抵达服务器的时候触发
         *2.要开启发送者确认 必须的配置文件中添加
         * spring.rabbitmq.publisher-confirm-type=correlated 配置信息
         *
         *    @FunctionalInterface
         *    pubc interface ConfirmCallback {
         *
         *
         * 		 * Confirmation callback.
         * 		 * @param correlationData correlation data for the callback.
         * 		 * @param ack true for ack, false for nack
         * 		 * @param cause An optional cause, for nack, when available, otherwise null.
         *
         *   confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause);
         *}
         *
         *
         * ConfirmCallback 是RabbitTemplate 中的一个接口 从源码信息中我们可以看打动
         * correlationData 当前消息唯一关联的数据(也就是消息的唯一id，在发送消息的时候我们可以指定消息的唯一id)
         * ack 消息是否成功收到
         * cause 失败的原因信息
         */

        // 1. 设置消息抵达服务器的确认回调
        this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {

            System.out.println("获取当前发送消息的唯一id:" + correlationData.getId());

            System.out.println("confirm==>correlationData[" + correlationData + "]==>ack[" + ack + "]==>cause[" + cause + "]");

        });


        /**
         *1.要开启该回调需要在配置文件中配置
         *
         *
         * #开启消息抵达到队列的回调配置
         * spring.rabbitmq.publisher-returns=true
         * #开启消息只要抵达到队列的时候，以其异步的方式优先执行returnConfirm回调
         * spring.rabbitmq.template.mandatory=true

         @FunctionalInterface public interface ReturnCallback {
          * Returned message callback.
          * @param message the returned message. 投递失败的信息的详细信息
         * @param replyCode the reply code.回复的状态码
         * @param replyText the reply text. 回复文本信息
         * @param exchange the exchange.  当时该消息发送给那个交换机的
         * @param routingKey the routing key.  当时该消息发送的路由键

        void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey);

        }

        2. 该回调触发的机制是 当消息未能成功抵达到队列的时候才会触发，成功不会触发
         */
        // 2. 设置消息抵达到队列的确认回调，如果消息成功抵达队列将不会触发，如果消息未成功抵达到队列则会触发该回调
        this.rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {


            System.out.println("ReturnCallback::==> message[" + message + "]==>replyCode[" + replyCode + "]==>replyText[" + replyText + "]==>exchange[" + exchange + "]==>routingKey[" + routingKey + "]");


           /* ReturnCallback::
                    ==>
            message[(Body:'{"deptid":"deptid1","deptname":"开发部"}' MessageProperties [headers={__TypeId__=ksea.rabbitmq.model.Deptinfo}, contentType=application/json, contentEncoding=UTF-8, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0, deliveryTag=0])]==>
            replyCode[312]==>
            replyText[NO_ROUTE]==>
            exchange[deptinfo.exchange]==>
            routingKey[deptinfo.queue-111]

            这里打印的信息，可以明确的知道期失败的原因NO_ROUTE 不存在的路由键 导致的信息未能正确抵达队列，因此该回调呗调用了，这种情况，我们就可以让数据重新继续发送到队列这里就需要记录失败的信息

            */

        });
    }


}
