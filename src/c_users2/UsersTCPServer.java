package c_users2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class UsersTCPServer {
    public static final int PORT = 3400;

    private ServerSocket listener;
    private Socket serverSideSocket;

    private PrintWriter toNetwork;
    private BufferedReader fromNetwork;

    private HashMap<String, Integer> userAccessCount = new HashMap<>();

    public UsersTCPServer() {
        System.out.println("Echo TCP server is running on port: " + PORT);
    }

    public void init() throws Exception {
        listener = new ServerSocket(PORT);

        while (true) {
            serverSideSocket = listener.accept();

            createStreams(serverSideSocket);
            protocol(serverSideSocket);
        }
    }

    public void protocol(Socket socket) throws Exception {
        String message = fromNetwork.readLine();
        System.out.println("[Server] From client: " + message);

        String[] parts = message.split(" ");
        String command = parts[0];

        if (command.equals(".LOGIN")) {
            String username = parts[1];

            int accessCount = 0;
            if (userAccessCount.containsKey(username)) {
                accessCount = userAccessCount.get(username);
            } else {
                userAccessCount.put(username, 0);
            }

            accessCount++;
            userAccessCount.put(username, accessCount);

            String response = "BIENVENIDO " + username + ", usted es el usuario #" + userAccessCount.size();
            toNetwork.println(response);
        } else if (command.equals(".INFORME")) {
            String response = String.join(", ", userAccessCount.keySet());
            toNetwork.println(response);
        } else if (command.equals(".INFORME_DETALLADO")) {
            String response = "";
            for (String username : userAccessCount.keySet()) {
                int accessCount = userAccessCount.get(username);
                response += username + " " + accessCount + ", ";
            }

            // Eliminar la última coma
            response = response.substring(0, response.length() - 2);

            toNetwork.println(response);
        } else {
            String response = "Comando no reconocido";
            toNetwork.println(response);
        }
    }

    private void createStreams(Socket socket) throws Exception {
        toNetwork = new PrintWriter(socket.getOutputStream(), true);
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static void main(String args[]) throws Exception {
        UsersTCPServer es = new UsersTCPServer();
        es.init();
    }
}