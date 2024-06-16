package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class JavaClient {

    private static PrintWriter out;
    private static BufferedReader in;

    public static void main(String[] args) throws IOException {

        System.out.println("JAVA TCP CLIENT");
        String hostName = "localhost";
        int portNumber = 12345;
        Socket socket = null;

        try {
            socket = new Socket(hostName, portNumber);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            MsgListener msgListener = new MsgListener(in);
            msgListener.start();
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter your nickname: ");
            String nickname = scanner.nextLine();
            out.println("connect " + nickname);
            boolean runningFlag = true;
            while(runningFlag){
                String input = scanner.nextLine();
                runningFlag = msgHandler(input);
            }
            scanner.close();
            in.close();
            out.close();
            socket.close();

    } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                in.close();
                out.close();
                socket.close();
            }
        }
    }

    private static boolean msgHandler(String input) {
        if ((input.equalsIgnoreCase("exit"))) {
            System.out.println("Canceling connection");
            out.println(input);
            return false;
        } else {
            out.println(input);
        }
        return true;
    }
}





