package chat;

import java.io.BufferedReader;
import java.io.IOException;

public class MsgListener extends Thread {
    private final BufferedReader in;

    public MsgListener(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            String incomingMessage;
            while ((incomingMessage = in.readLine()) != null) {
                System.out.println(incomingMessage);
            }
        } catch (IOException e) {
            if (e.getMessage().equals("Socket closed")) {
                System.out.println("Connection closed.");
            } else {
                e.printStackTrace();
            }
        }
    }
}

