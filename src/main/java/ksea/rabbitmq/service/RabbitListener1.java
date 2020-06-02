package ksea.rabbitmq.service;


import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import ksea.rabbitmq.model.Userinfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;


/**
 * 有的时候，可能比较特别我们单独监听某个队列
 *
 * @author k.sea
 * @RabbitListener 这个注解可以用在class上面也可以用在 method 方法上面
 * 一般主要是配合@RabbitHandler注解使用，@RabbitHandler只能注解在方法上面
 * @RabbitHandler 只能标注在方法上面
 */
@Slf4j
@Component
public class RabbitListener1 {


    // 1.第一种方式接受信息，通过message中的getBody返回的二进制 转json，然后序列化成对应的对象 @RabbitListener可以直接应用某于某个方法上指明监听
    //@RabbitListener(queues = {"userinfo.queue"})
    public void userinfoQueueHandler1(Message message) throws UnsupportedEncodingException {

        //返回的一个信息输byte信息数组，也就是我们的json数据
        byte[] body = message.getBody();

        Userinfo u = JSONObject.parseObject(body, Userinfo.class);


        log.info("接收到的用户信息[{}]", u);

        //消息的头部信息
        MessageProperties messageProperties = message.getMessageProperties();

        log.info("获取到的消息头部的信息[{}]", messageProperties);


    }


    /**
     * 2.当我们要获取某个json数据对象的时候，我们可以直接映射某个类即可
     *
     * @param message
     * @throws UnsupportedEncodingException
     */

    // @RabbitListener(queues = {"userinfo.queue"})
    public void userinfoQueueHandler2(Message message, Userinfo userinfo) throws UnsupportedEncodingException {

        log.info("接收到的用户信息[{}]", userinfo);


    }


    /**
     * 3. 当我们要获取某个json数据对象的时候，我们可以直接映射某个类即可
     *
     * @param message
     * @param channel 与客户端建立的通道，一个消息只能与一个客户端建立通道，一个信息只能有一个通道进行传输
     * @throws UnsupportedEncodingException
     */
    //@RabbitListener(queues = {"userinfo.queue"})
    public void userinfoQueueHandler3(Message message, Userinfo userinfo, Channel channel) throws UnsupportedEncodingException {


        log.info("接收到的用户信息[{}]", userinfo);


    }

    /**
     * 4. Queue 队列可以很多人都来进行监听，只要消费者接受到信息，队列就会删除该信息，而且只能由一个接受到消息
     * 4.1）当启动多个服务的时候，一个信息只能由一个客户端接受到
     * 4.2)客户端接受信息的时候，只有一个信息处理完毕，方法运行结束，才能进行下一个消息的接收
     *
     * @param message
     * @param userinfo
     * @param channel
     * @throws UnsupportedEncodingException
     */
    // @RabbitListener
    public void userinfoQueueHandler4(Message message, Userinfo userinfo, Channel channel) throws UnsupportedEncodingException {


        log.info("接收到的用户信息[{}]", userinfo);


    }
}
