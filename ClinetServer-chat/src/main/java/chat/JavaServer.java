package chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class JavaServer {
    private final static Map<Integer, Socket> clientsTcp = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("JAVA SERVER");
        int portNumber = 12345;

        int clientId = 0;
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {

            while (true) {
                Socket clientSocket = serverSocket.accept();

                Thread clientThread = new Thread(new ClientHandler(clientSocket, clientId, clientsTcp));
                clientThread.start();
                clientId++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
