package ksea.rabbitmq.controller;


import ksea.rabbitmq.model.Deptinfo;
import ksea.rabbitmq.model.Userinfo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author k.sea
 */
@RestController
@RequestMapping("rabbitmq")
public class RabbitmqController {


    /**
     * 注入 RabbitTemplate 用于操作rabbitmq的信息发送
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @GetMapping("sendMsg")
    public Object sendMessage() {


        // 1. 首先指明要发送的交换机，然后指明要发送的路由routingKey，要发送的信息
        // rabbitTemplate.convertAndSend("java.exchange","java.key", "rabbitmq world");


        // 2.发送一个对象,这种情况下它是被java序列化
        /**
         *
         *
         * 这个是在消息队列中的数据信息
         * The server reported 0 messages remaining.
         *
         * Exchange	java.exchange
         * Routing Key	java.key
         * Redelivered	○
         * Properties
         * priority:	0
         * delivery_mode:	2
         * headers:
         * content_type:	application/x-java-serialized-object
         * Payload
         * 130 bytes
         * Encoding: base64
         * rO0ABXNyACRvcmcua3NlYS5rc2VhcmFiYml0bXEubW9kZWwuVXNlcmluZm/+pt17VNnd7AIAA0wAA3B3ZHQAEkxqYXZhL2xhbmcvU3RyaW5nO0wAA3VpZHEA
         * fgABTAAIdXNlcm5hbWVxAH4AAXhwdAAEa3NlYXQABHVpZDFxAH4AAw==
         */

        //   Userinfo userinfo = new Userinfo("uid1", "ksea", "ksea");
        //  rabbitTemplate.convertAndSend("java.exchange", "java.key", userinfo);


        // 3.在配置文件类中设置JSON转换器类之后 对象将转换成json
        /*The server reported 0 messages remaining.

        Exchange	    java.exchange
        Routing Key	    java.key
        Redelivered	     ○
        Properties
        priority:	    0
        delivery_mode:	2
        headers:
        __TypeId__:	        org.ksea.ksearabbitmq.model.Userinfo
        content_encoding:	UTF-8
        content_type:	    application/json
        Payload
        45 bytes
        Encoding: string
        {"uid":"uid1","username":"ksea","pwd":"ksea"}*/

        Userinfo userinfo = new Userinfo("uid1", "ksea", "ksea");
        rabbitTemplate.convertAndSend("java.exchange", "java.key", userinfo);


        return "发送成功";

    }


    @GetMapping("sendUserinfo")
    public Object sendUserinfo() {

        Userinfo userinfo = new Userinfo("uid2", "jacky", "jacky_pwd");

        rabbitTemplate.convertAndSend("userinfo.exchange", "userinfo.queue", userinfo);

        return "成功发送用户信息";

    }


    @GetMapping("sendDeptinfo")
    public Object sendDeptinfo() {

        Deptinfo deptinfo = new Deptinfo("deptid1", "开发部");
        rabbitTemplate.convertAndSend("deptinfo.exchange", "deptinfo.queue", deptinfo);

        return "成功发送部门信息";

    }


    @GetMapping("sendInfo")
    public Object sendInfo() {


        for (int i = 1; i <= 10; i++) {

            if (i % 2 == 0) {
                Userinfo userinfo = new Userinfo("uid" + i, "jacky", "jacky_pwd");

                rabbitTemplate.convertAndSend("userinfo.exchange", "userinfo.queue", userinfo);
            } else {
                Deptinfo deptinfo = new Deptinfo("deptid" + i, "开发部");
                rabbitTemplate.convertAndSend("deptinfo.exchange", "deptinfo.queue", deptinfo);
            }
        }
        return "成功发送信息";

    }


}
