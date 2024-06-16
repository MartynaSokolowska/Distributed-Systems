package chat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Set;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final int clientId;
    private String nickname = null;
    private static Map<Integer, Socket> clients;

    public ClientHandler(Socket clientSocket, int clientId, Map<Integer, Socket> clients) {
        this.clientSocket = clientSocket;
        this.clientId = clientId;
        ClientHandler.clients = clients;
        try{
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
        e.printStackTrace();
    }

    }

    @Override
    public void run() {
        try {
            String inputLine;
            boolean runningFlag = true;

            while (runningFlag && in!= null && (inputLine = in.readLine()) != null) {
                if (nickname == null){
                    System.out.println("Client " + clientId + " sent: " + inputLine);
                }
                else{
                    System.out.println(nickname + " sent: " + inputLine);
                }
                runningFlag = msgHandler(inputLine);
            }

            out.close();
            in.close();
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMsgToAll(String msg) throws IOException {
        Set<Integer> clientIds = getClients().keySet();
        for (int receiverId : clientIds) {
            if (receiverId == clientId){
                continue;
            }
            Socket receiverSocket = getClients().get(receiverId);
            PrintWriter out = new PrintWriter(receiverSocket.getOutputStream(), true);
            out.println(nickname + ": " + msg);
        }
    }

    private boolean msgHandler(String input) throws IOException {
        String[] split = input.split(" ");
        if (split[0].equalsIgnoreCase("connect")){
            nickname = split[1];
            out.println("server: connected");
            addClient(clientId, clientSocket);
        }
        else if (input.equalsIgnoreCase("exit")){
            System.out.println(nickname + " disconnected") ;
            in.close();
            out.close();
            clientSocket.close();
            removeClient(clientId);
            return false;
        }
        else {
            try {
                sendMsgToAll(input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    public static synchronized void addClient(int clientId, Socket out) {
        clients.put(clientId, out);
    }

    public static synchronized void removeClient(int clientId) throws IOException {
        Socket socket = clients.get(clientId);
        socket.close();
        clients.remove(clientId);
    }

    public static synchronized Map<Integer, Socket> getClients() {
        return clients;
    }
}

