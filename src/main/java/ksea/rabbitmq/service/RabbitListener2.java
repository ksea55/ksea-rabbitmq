package ksea.rabbitmq.service;


import com.rabbitmq.client.Channel;
import ksea.rabbitmq.model.Deptinfo;
import ksea.rabbitmq.model.Userinfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author k.sea
 * @RabbitListener 注解到class上与方法上
 * @RabbitHandler 只能注解到方法上面
 */


@Slf4j
@Component
@RabbitListener(queues = {"userinfo.queue","deptinfo.queue"})
public class RabbitListener2 {


    /**
     * 用于主要处理userinfo的业务监听
     *
     * @param userinfo
     * @param channel
     */
    @RabbitHandler
    public void userinfoHandler(Userinfo userinfo, Channel channel) {

        log.info("接受到的用户信息[{}]", userinfo);

    }


    /**
     * 用于处理部门的业务信息
     *
     * @param deptinfo
     * @param channel
     */
    @RabbitHandler
    public void deptinfoHandler(Deptinfo deptinfo, Channel channel) {

        log.info("接受到的部门信息[{}]", deptinfo);

    }

}
