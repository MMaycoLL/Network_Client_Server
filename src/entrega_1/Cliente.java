package entrega_1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class Cliente {
    public static final String SERVER_IP = "localhost"; // localhost
    public static final int SERVER_PORT = 3400;

    private Socket clientSideSocket;
    private PrintWriter toServer;
    private BufferedReader fromServer;

    public Cliente() {
        System.out.println("Echo TCP client is running");
    }

    public void init() throws Exception {
        clientSideSocket = new Socket(SERVER_IP, SERVER_PORT);

        createStreams(clientSideSocket);
        protocol(clientSideSocket);
    }

    public void protocol(Socket clientSideSocket) throws Exception {
        BufferedReader fromUser = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("\nIngrese el mensaje a enviar al servidor (o escriba 'exit' para detener) \n\n" +
                    "*******************************************************************************\n"+
                    "*** APERTURA/nombre/apellido/cedula/monto/clave                             ***\n"+
                    "*** MODIFICACION/numero_cuenta/parametro_modificar/dato_nuevo/clave_cuenta ***\n"+
                    "*** ELIMINACION/numero_cuenta/motivo_eliminacion/clave                      ***\n"+
                    "*******************************************************************************");
            String message = fromUser.readLine();

            if (message.equalsIgnoreCase("exit")) {
                break;
            }

            toServer.println(message);

            String response = fromServer.readLine();
            System.out.println("[Client] From server: " + response);
        }
    }

    public void createStreams(Socket socket) throws Exception {
        toServer = new PrintWriter(socket.getOutputStream(), true);
        fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static void main(String[] args) {
        try {
            Cliente client = new Cliente();
            client.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}