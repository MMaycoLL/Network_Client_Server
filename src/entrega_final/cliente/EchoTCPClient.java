package entrega_final.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class EchoTCPClient {
    public static final String SERVER = "localhost";
    public static final int PORT = 4200;
    public static final Scanner SCANNER = new Scanner(System.in);
    public static PrintWriter toNetwork;
    public static BufferedReader fromNetwork;
    private Socket clientSideSocket;

    public EchoTCPClient() {
        System.out.println("Echo TCP client is running ...");
    }

    public static void main(String[] args) throws Exception {
        EchoTCPClient ec = new EchoTCPClient();
        ec.init();
    }

    public static void protocoloSalir(Socket socket) throws IOException {
        toNetwork.println("SALIR");
        String resp = fromNetwork.readLine();
        System.out.println(resp);
        System.out.println("Gracias por ingresar al bamco, vuelva pronto!");
        socket.close();
    }

    // Este método crea los flujos de entrada y salida necesarios para enviar y recibir datos a través del socket.
    public static void createStreams(Socket socket) throws Exception {
        // El objeto "toNetwork" se utiliza para enviar datos a través del socket.
        toNetwork = new PrintWriter(socket.getOutputStream(), true);

        // El objeto "fromNetwork" se utiliza para recibir datos del socket.
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void init() throws Exception {
        System.out.println("Creando socket...");
        clientSideSocket = new Socket(SERVER, PORT);
        createStreams(clientSideSocket);
        ClientMenu.menu(clientSideSocket);
    }
}