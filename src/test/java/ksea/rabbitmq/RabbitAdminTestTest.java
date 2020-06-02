package ksea.rabbitmq;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * 用于在测试类中如何通过java中创建exchange,queue,以及如何绑定他们
 */


@Slf4j
@SpringBootTest
public class RabbitAdminTestTest {


    @Autowired
    private AmqpAdmin amqpAdmin;


    @Test
    public void createExchangeTest() {


        /**
         *
         * DirectExchange(String name, boolean durable, boolean autoDelete, Map<String, Object> arguments)
         *
         * name 指定交换机面层
         * durable 是否持久化
         * autoDelete 是否自动删除
         *
         * arguments 参数列表
         */

        // 这个构造函数从源码可以看到this(name, true, false); durable=true autoDelete=false
        DirectExchange directExchange = new DirectExchange("java.exchange");
        //声明一个什么样的交换机
        amqpAdmin.declareExchange(directExchange);


        log.info("创建成功");

    }


    @Test
    public void createQueueTest() {


        Queue queue = new Queue("java.queue");
        amqpAdmin.declareQueue(queue);
        log.info("创建{}成功", queue.getName());

    }


    @Test
    public void createBindingTest() {

       /* Binding(String destination, 绑定的目的 也就是那个队列
                Binding.DestinationType destinationType,  目的地类型
                String exchange,
                String routingKey,
                @Nullable Map<String, Object> arguments
        )*/
        Binding binding = new Binding("java.queue", Binding.DestinationType.QUEUE, "java.exchange", "java.key", null);
        amqpAdmin.declareBinding(binding);

        log.info("绑定交换机与队列成功");


    }


}