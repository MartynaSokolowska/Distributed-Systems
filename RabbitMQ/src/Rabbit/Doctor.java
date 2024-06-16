package Rabbit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.LongString;

import java.util.Collections;
import java.util.Map;

import static java.lang.Thread.sleep;

public class Doctor {
    private static final Channel channel = Config.CHANNEL;
    private final String id;
    private final int perPatientTime;

    public Doctor (String id, int perPatientTime){
        this.id = id;
        this.perPatientTime = perPatientTime;
    }

    public void order(String patientName, String injuryType) throws Exception {
        String message = patientName + " " + injuryType;
        sleep(perPatientTime);

        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .headers(Collections.singletonMap("id", id))
                .build();

        channel.basicPublish(Config.HOSPITAL, injuryType, properties, message.getBytes());
        channel.basicPublish(Config.HOSPITAL, Config.ADMIN, properties, (message).getBytes());
    }

    public void receive() throws Exception {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            String message = new String(delivery.getBody(), "UTF-8");
            Map<String, Object> headers = delivery.getProperties().getHeaders();
            LongString idMessage = (LongString) headers.get("id");
            String idToString = idMessage.toString();
            if (idToString.equals("admin")){
                System.out.println("Doctor " + id + " got message from admin: " + message);
            }
            try {
                sleep(perPatientTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        channel.basicConsume("doctor_queue_" + id, false, deliverCallback, consumerTag -> {});
    }

    public String getId() {
        return id;
    }
}
