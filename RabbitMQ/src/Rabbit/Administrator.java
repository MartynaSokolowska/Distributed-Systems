package Rabbit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.Collections;

public class Administrator {
    private static final Channel channel = Config.CHANNEL;

    public static void log() throws Exception {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Log: " + message);
        };
        channel.basicConsume("administrator_queue", true, deliverCallback, consumerTag -> {});
    }

    public static void sendInfo(String info) throws Exception {
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .headers(Collections.singletonMap("id", "admin"))
                .build();

        channel.basicPublish(Config.HOSPITAL, Config.INFO, properties, info.getBytes());
    }
}
