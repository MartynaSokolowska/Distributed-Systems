package test;

import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Administrator {
    private static final Channel channel = Config.CHANNEL;

    public static void main(String[] args) throws Exception {
        log();
    }

    public static void log() throws Exception {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Log: " + message);
        };
        channel.basicConsume("administrator_queue", true, deliverCallback, consumerTag -> {});
    }

    public void sendInfo(String info) throws Exception {
        channel.basicPublish(Config.HOSPITAL, Config.INFO, null, info.getBytes());
        System.out.println("test.Administrator przesłał informację: " + info);
    }
}
