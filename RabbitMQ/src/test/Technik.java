package test;

import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Technik {
    private static final Channel channel = Config.CHANNEL;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new
                InputStreamReader(System.in));
        System.out.println("Podaj imię i nazwisko Technika:");
        String name = br.readLine();
        String[] typyBadan = new String[2];
        System.out.println("Podaj pierwszy uraz w których się specjalizuje:");
        String input = br.readLine();
        while (!(input.equals("elbow") || input.equals("hip") || input.equals("knee"))){
            System.out.println("Złe dane, podaj knee, hip lub elbow");
            input = br.readLine();
        }
        typyBadan[0] = input;
        System.out.println("Podaj drugi uraz w których się specjalizuje:");
        input = br.readLine();
        while (!(input.equals("elbow") || input.equals("hip") || input.equals("knee"))){
            System.out.println("Złe dane, podaj knee, hip lub elbow");
            input = br.readLine();
        }
        typyBadan[1] = input;
        System.out.println("Poprawnie wybrane urazy");
        while (true){
            Exam(typyBadan,name);
        }
    }

    public static void Exam(String[] typyBadan, String name) throws Exception {
        for (String badanieType : typyBadan) {
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                String[] parts = message.split(":");
                String patientName = parts[0];
                String result = patientName + " " + badanieType;

                // Send result back
                channel.basicPublish(Config.HOSPITAL, Config.RESULT, null, result.getBytes());
                String messageToSend = "test.Technik "+ name +" wykonuje badania: " + result;
                System.out.println(messageToSend);

                // Send admin message
                channel.basicPublish(Config.HOSPITAL, Config.ADMIN, null, messageToSend.getBytes());
            };
            AMQP.Queue.DeclareOk declareOk = Config.CHANNEL.queueDeclarePassive("technik_queue_" + badanieType);
            if (declareOk.getMessageCount() > 0) {
                channel.basicConsume("technik_queue_" + badanieType, true, deliverCallback, consumerTag -> {
                });
            }
        }
    }
}
