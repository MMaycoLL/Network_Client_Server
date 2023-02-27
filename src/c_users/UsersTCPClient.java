package c_users;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class UsersTCPClient {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String SERVER = "localhost";
    private static final int PORT = 1234;

    private PrintWriter toServer;
    private BufferedReader fromServer;

    private Socket socket;

    public UsersTCPClient() {
        System.out.println("Aplicación cliente iniciada");
    }

    public void init() throws Exception {
        socket = new Socket(SERVER, PORT);

        createStreams(socket);

        while (true) {
            System.out.print("Ingrese un comando (use .LOGIN [nombre] para iniciar sesión): ");
            String comando = scanner.nextLine();

            // Comando .LOGIN
            if (comando.startsWith(".LOGIN ")) {
                toServer.println(comando);
                String respuesta = fromServer.readLine();
                System.out.println(respuesta);
            }
            // Comando .INFORME o .INFORME_DETALLADO
            else if (comando.equals(".INFORME") || comando.equals(".INFORME_DETALLADO")) {
                toServer.println(comando);
                String respuesta = fromServer.readLine();
                System.out.println(respuesta);
            }
            // Comando no válido
            else {
                System.out.println("Comando no válido");
            }
        }
    }

    private void createStreams(Socket socket) throws Exception {
        toServer = new PrintWriter(socket.getOutputStream(), true);
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static void main(String[] args) throws Exception {
        UsersTCPClient cliente = new UsersTCPClient();
        cliente.init();
    }
}