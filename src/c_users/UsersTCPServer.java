package c_users;

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

    private HashMap<String, Integer> users = new HashMap<>();

    public UsersTCPServer() {
        System.out.println("Echo TCP server is running on port: " + PORT);
    }

    public void init() throws Exception {
        listener = new ServerSocket(PORT);

        while (true) {
            serverSideSocket = listener.accept();
            System.out.println("Connection established.");

            createStreams(serverSideSocket);

            String message = fromNetwork.readLine();
            System.out.println("[Server] From client: " + message);

            String[] parts = message.split(" ");
            String command = parts[0];

            switch (command) {
                case ".LOGIN":
                    String username = parts[1];
                    if (users.containsKey(username)) {
                        int count = users.get(username) + 1;
                        users.put(username, count);
                        toNetwork.println("Acceso #" + count);
                    } else {
                        int count = 1;
                        users.put(username, count);
                        int userCount = users.size();
                        toNetwork.println("BIENVENIDO " + username + ", usted es el usuario #" + userCount);
                    }
                    break;
                case ".INFORME":
                    String userList = String.join(", ", users.keySet());
                    toNetwork.println(userList);
                    break;
                case ".INFORME_DETALLADO":
                    StringBuilder detailedList = new StringBuilder();
                    for (String user : users.keySet()) {
                        int count = users.get(user);
                        detailedList.append(user).append(" ").append(count).append(", ");
                    }
                    if (detailedList.length() > 0) {
                        detailedList.setLength(detailedList.length() - 2);
                    }
                    toNetwork.println(detailedList.toString());
                    break;
                default:
                    toNetwork.println("Comando inválido.");
                    break;
            }

            serverSideSocket.close();
            System.out.println("Connection closed.");
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