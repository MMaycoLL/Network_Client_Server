package c_users2;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class UsersTCPClient {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static final String SERVER = "localhost";
    public static final int PORT = 3400;

    private PrintWriter toNetwork;
    private BufferedReader fromNetwork;

    private Socket clientSideSocket;

    public UsersTCPClient() {
        System.out.println("Echo TCP client is running ...");
    }

    public void init() throws Exception {
        try {
            clientSideSocket = new Socket(SERVER, PORT);
            createStreams(clientSideSocket);
            protocol(clientSideSocket);
        } finally {
            try {
                clientSideSocket.close();
            } catch (IOException e) {
                // Manejar excepción
            }
        }
    }

    public void protocol(Socket socket) throws Exception {
        boolean exit = false;
        while (!exit) {
            System.out.print("Ingrese un comando (o escriba 'exit' para salir): ");
            String command = SCANNER.nextLine();
            String[] commandParts = command.split(" ");

            switch (commandParts[0]) {
                case ".LOGIN":
                    if (commandParts.length == 2) {
                        toNetwork.println(command);
                    } else {
                        System.out.println("El comando .LOGIN debe ir acompañado del nombre de usuario");
                    }
                    break;
                case ".INFORME":
                case ".INFORME_DETALLADO":
                    toNetwork.println(command);
                    break;
                case "exit":
                    exit = true;
                    break;
                default:
                    System.out.println("Comando inválido");
            }

            if (!exit) {
                String fromServer = fromNetwork.readLine();
                System.out.println("[Client] From server: " + fromServer);
            }
        }
        clientSideSocket.close();
    }
    private void createStreams(Socket socket) throws Exception {
        toNetwork = new PrintWriter(socket.getOutputStream(), true);
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static void main(String args[]) throws Exception {
        UsersTCPClient ec = new UsersTCPClient();
        ec.init();
    }
}