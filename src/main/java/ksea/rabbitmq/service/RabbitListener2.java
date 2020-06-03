package ksea.rabbitmq.service;


import com.rabbitmq.client.Channel;
import ksea.rabbitmq.model.Deptinfo;
import ksea.rabbitmq.model.Userinfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @author k.sea
 * @RabbitListener 注解到class上与方法上
 * @RabbitHandler 只能注解到方法上面
 */


@Slf4j
@Component
@RabbitListener(queues = {"userinfo.queue", "deptinfo.queue"})
public class RabbitListener2 {


    /**
     * 用于主要处理userinfo的业务监听
     *
     * @param userinfo
     * @param channel
     */
    // @RabbitHandler
    public void userinfoHandler(Userinfo userinfo, Channel channel) {

        log.info("接受到的用户信息[{}]", userinfo);

    }


    /**
     * 用于处理部门的业务信息
     *
     * @param deptinfo
     * @param channel
     */
    // @RabbitHandler
    public void deptinfoHandler(Deptinfo deptinfo, Channel channel) {

        log.info("接受到的部门信息[{}]", deptinfo);

    }


    /**
     * 手动确认消费信息，保证消息的不丢失
     * <p>
     * 1. 要开启手动确认消息模式需要在配置文件中添加如下配置信息
     * <p>
     * # 开启消费者端手动确认消息模式，其默认是auto自动确认模式
     * spring.rabbitmq.listener.simple.acknowledge-mode=manual
     * <p>
     * 2.消费端手动确认模式的好处-> 保证每个消息能够被正确进行消费处理，正确处理之后才可以从服务器Broker中删除该信息
     * 2.1）消费端的默认方式是自动确认，只要消息被接收到，客户端就会自动进行确认，服务端就会移除该消息
     * 这种方式带来的问题->我们说到很多信息，如果都自动回复给服务器ack(自动确认)，假如只有一个信息处理成功，此刻客户端宕机，其没有消费的信息就会被自动确认，造成数据的丢失
     * <p>
     * 手动信息确认模式(spring.rabbitmq.listener.simple.acknowledge-mode=manual) 只要消费端没有明确告诉服务器，信息没有被ack（确认），该消息在队列中就一致是unacked状态，在这种情况下即使消费端宕机，消息也不会丢失，而状态会变成ready状态，当有新的消费端启动就会重新进行分发
     *
     * @param message
     * @param userinfo
     * @param channel
     */
    @RabbitHandler
    public void userinfoHandler(Message message, Userinfo userinfo, Channel channel) {


        // 获取当当前消息的交货标签
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {

            // deliveryTag 是消息通道中递增信息
            System.out.println("deliveryTag:" + deliveryTag);

            /**
             * 消息签收  basicAck(long deliveryTag, boolean multiple)
             * deliveryTag 签收的信息标签
             * multiple true批量签收  false 只是签收当前信息
             */

            // 这里模拟 %2==0的呗签收
            if (deliveryTag % 2 == 0) {
                channel.basicAck(deliveryTag, false);
            } else {


                /**
                 * 拒绝签收
                 *  basicNack(long deliveryTag, boolean multiple, boolean requeue)
                 *  requeue true重新进入队列 false 直接舍弃该信息
                 *
                 */
                channel.basicNack(deliveryTag, false, true);


                /**
                 * 这里就有一种业务场景，当某个信息在这里发生异常，我们拒绝签收该信息，那这个时候我们就可以调用
                 *
                 * channel.basicNack(deliveryTag,false,false);
                 *
                 * 拒绝签收该信息，并舍弃，然后将该信息直接发送到另外一个拒签队列，由拒签的队列消费端 进行对应的业务处理
                 *
                 */

            }
        } catch (IOException e) {
            e.printStackTrace();
            // 异常信息这里一般就是网络中断了
        }


    }


}
