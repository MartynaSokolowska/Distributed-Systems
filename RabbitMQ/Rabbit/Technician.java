package Rabbit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.LongString;

import java.util.Collections;
import java.util.Map;

import static java.lang.Thread.sleep;


public class Technician {
    private static final Channel channel = Config.CHANNEL;
    private final String[] types;
    private final int examinationTime;
    private final String id;

    public Technician(String  id, String[] types, int examinationTime){
        this.id = id;
        this.types = types;
        this.examinationTime = examinationTime;
    }

    public void Exam() throws Exception {
        for (String type : types) {
            try {
                sleep(examinationTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                String message = new String(delivery.getBody(), "UTF-8");

                Map<String, Object> headers = delivery.getProperties().getHeaders();
                LongString doctorId = (LongString) headers.get("id");
                String doctorIdString = doctorId.toString();

                message += " done";

                AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                        .headers(Collections.singletonMap("id", id))
                        .build();

                channel.basicPublish(Config.HOSPITAL, doctorIdString, properties, message.getBytes());
                channel.basicPublish(Config.HOSPITAL, Config.ADMIN, properties, message.getBytes());
            };
            channel.basicConsume("technic_queue_" + type, false, deliverCallback, consumerTag -> {
            });
        }
        DeliverCallback deliverCallbackInfo = (consumerTag, delivery) -> {
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Technic " + id + " got message from admin: " + message);
        };
        channel.basicConsume("technic_queue_" + id, false, deliverCallbackInfo, consumerTag -> {
        });
    }
}
