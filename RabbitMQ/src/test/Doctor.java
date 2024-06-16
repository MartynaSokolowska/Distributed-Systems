package test;

import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Doctor {
    private static final Channel channel = Config.CHANNEL;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new
                InputStreamReader(System.in));
        System.out.println("Podaj imię i nazwisko Doktora:");
        String name = br.readLine();
        System.out.println("Podaj imię i nazwisko pacjenta oraz uraz z jakim przychodzi(knee, elbow, hip):");
        String line = br.readLine();
        while (!line.equals("exit")){
            String[] parts = line.split(" ");
            if (parts.length != 3 || (!parts[2].equals("knee") && !parts[2].equals("hip") && !parts[2].equals("elbow"))){
                System.out.println("Złe dane");
                line = br.readLine();
                continue;
            }
            String patientName = parts[0] + " " + parts[1];
            order(patientName,parts[2],name);
            receive(name);
            line = br.readLine();
        }
    }

    public static void order(String patientName, String badanieType, String name) throws Exception {
        String message = patientName + ":" + badanieType;
        channel.basicPublish(Config.HOSPITAL, badanieType, null, message.getBytes());
        System.out.println("Lekarz " + name + " zlecił badanie " + message);
        channel.basicPublish(Config.HOSPITAL, Config.ADMIN, null, ("Lekarz " + name + " zlecił badanie " + message).getBytes());
    }

    public static void receive(String name) throws Exception {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            String answer = "Lekarz " + name + " otrzymał wynik: " + message;
            System.out.println(answer);
            channel.basicPublish(Config.HOSPITAL, Config.ADMIN, null, answer.getBytes());
        };
        channel.basicConsume("lekarz_queue", true, deliverCallback, consumerTag -> {});
    }
}
