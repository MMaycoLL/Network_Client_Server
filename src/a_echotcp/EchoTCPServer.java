package a_echotcp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class EchoTCPServer {
    public static final int PORT = 3400;

    private ServerSocket listener;
    private Socket serverSideSocket;

    private PrintWriter toNetwork;
    private BufferedReader fromNetwork;

    private HashMap<String, Integer> usuariosRegistrados;

    public EchoTCPServer() {
        System.out.println("Echo TCP server is running on port: " + PORT);
        usuariosRegistrados = new HashMap<>();
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
        String mensaje = fromNetwork.readLine();
        String[] partes = mensaje.split(" ");

        if (partes[0].equals("LOGIN")) {
            String nombre = partes[1];

            if (usuariosRegistrados.containsKey(nombre)) {
                int ingresosAnteriores = usuariosRegistrados.get(nombre);
                int nuevosIngresos = ingresosAnteriores + 1;
                usuariosRegistrados.put(nombre, nuevosIngresos);
                toNetwork.println(nombre + ": Usted ha ingresado #" + nuevosIngresos + " veces al sistema");
            } else {
                usuariosRegistrados.put(nombre, 1);
                int cantidadUsuariosRegistrados = usuariosRegistrados.size();
                toNetwork.println("BIENVENIDO " + nombre + ", usted es el usuario #" + cantidadUsuariosRegistrados);
            }
        } else {
            toNetwork.println("Comando inválido");
        }
    }

    private void createStreams(Socket socket) throws Exception {
        toNetwork = new PrintWriter(socket.getOutputStream(), true);
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static void main(String args[]) throws Exception {
        EchoTCPServer es = new EchoTCPServer();
        es.init();
    }
}
