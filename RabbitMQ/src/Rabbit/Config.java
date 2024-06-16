package Rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Config {
    public static final String HOSPITAL = "hospital_exchange";
    public static final String ADMIN = "admin_log";
    public static final String INFO = "admin_info";

    public Config(){
    }

    public static Channel setupChannel() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(HOSPITAL, "direct");

        // Declare queues
        channel.queueDeclare("doctor_queue_1", false, false, false, null);
        channel.queueDeclare("doctor_queue_2", false, false, false, null);

        channel.queueDeclare("technic_queue_1", false, false, false, null);
        channel.queueDeclare("technic_queue_2", false, false, false, null);

        channel.queueDeclare("administrator_queue", false, false, false, null);

        // Declare queues for technik types
        channel.queueDeclare("technic_queue_knee", false, false, false, null);
        channel.queueDeclare("technic_queue_hip", false, false, false, null);
        channel.queueDeclare("technic_queue_elbow", false, false, false, null);

        // Bind queues
        channel.queueBind("administrator_queue", HOSPITAL, ADMIN);
        channel.queueBind("doctor_queue_1", HOSPITAL, "1");
        channel.queueBind("doctor_queue_2", HOSPITAL, "2");

        channel.queueBind("technic_queue_knee", HOSPITAL, "knee");
        channel.queueBind("technic_queue_hip", HOSPITAL, "hip");
        channel.queueBind("technic_queue_elbow", HOSPITAL, "elbow");

        channel.queueBind("technic_queue_1", HOSPITAL, INFO);
        channel.queueBind("technic_queue_2", HOSPITAL, INFO);
        channel.queueBind("doctor_queue_1", HOSPITAL, INFO);
        channel.queueBind("doctor_queue_2", HOSPITAL, INFO);

        return channel;
    }
    public static final Channel CHANNEL;

    static {
        try {
            CHANNEL = setupChannel();
        } catch (Exception e) {
            throw new RuntimeException("Failed to setup channel", e);
        }
    }
}


