package entrega_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Servidor {
    public static final int PORT = 3400;

    private ServerSocket listener;
    private Socket serverSideSocket;

    private PrintWriter toNetwork;
    private BufferedReader fromNetwork;

    private Map<Integer, CuentaAhorros> cuentas;
    // Declaración de la variable privada "cuentas" de tipo Map<Integer, CuentaAhorros>.
    // Esta variable se usará para almacenar las cuentas de ahorros creadas.
    public Servidor() {
        cuentas = new HashMap<>();// Inicialización de la variable "cuentas" como un nuevo HashMap vacío.
        System.out.println("Echo TCP server is running on port: " + PORT);
    }
    public void createStreams(Socket socket) throws IOException {
        toNetwork = new PrintWriter(socket.getOutputStream(), true);
        // Inicialización del objeto "toNetwork" como un nuevo PrintWriter que escribirá en el flujo de salida del socket.
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // Inicialización del objeto "fromNetwork" como un nuevo BufferedReader que leerá del flujo de entrada del socket.
    }
    public static void main(String[] args) throws Exception {
        Servidor server = new Servidor();
        server.init();
    }
    public void init() throws Exception {
        listener = new ServerSocket(PORT);
// Inicialización del objeto "listener" como un nuevo ServerSocket que escuchará en el puerto indicado por la constante "PORT".
        while (true) {  // Ciclo infinito que se encargará de aceptar nuevas conexiones entrantes.
            serverSideSocket = listener.accept();

            createStreams(serverSideSocket);// Invocación del método "createStreams()" para inicializar los flujos de entrada y salida de la conexión entrante.
            protocol(serverSideSocket);// Invocación del método "protocol()" para procesar las peticiones del cliente.
        }
    }

    public void protocol(Socket socket) throws Exception {
        //int clientPort = socket.getPort();
        System.out.println("[Server] Connected to client " + socket.getRemoteSocketAddress());

        String message = fromNetwork.readLine();
        System.out.println("[Server] From client: " + message);// Imprime en la consola el mensaje recibido del cliente.

        String[] splitMessage = message.split("/");// Divide el mensaje recibido en distintas partes separadas por el caracter "/".

        switch (splitMessage[0]) { // Selecciona una rama del switch según el primer elemento del mensaje recibido.
            case "APERTURA":
                String nombre = splitMessage[1];// Lee el segundo elemento del mensaje y lo guarda en la variable "nombre".
                String apellido = splitMessage[2];
                int cedula = Integer.parseInt(splitMessage[3]);
                double monto = Double.parseDouble(splitMessage[4]);
                String clave = splitMessage[5];
                CuentaAhorros cuenta = new CuentaAhorros(nombre, apellido,cedula, monto,  clave);
                int numCuenta = cuentas.size() + 11111;//se obtiene el tamaño del map y se le suma, esto sera la nueva
                cuentas.put(numCuenta, cuenta); //clave de la cuenta a crear
                toNetwork.println("APERTURA EXITOSA. Numero de cuenta: " + numCuenta);
                break;
            case "MODIFICACION": //String claveCuentaModif = Integer.parseInt(splitMessage[1]);
                int numCuentaModif = Integer.parseInt(splitMessage[1]);
                String parametro = splitMessage[2];
                String nuevoDato = splitMessage[3];
                CuentaAhorros cuentaModif = cuentas.get(numCuentaModif);
                if (cuentaModif != null) {
                    if (cuentaModif.getClave().equals(splitMessage[4])) {
                        switch (parametro) {
                            case "NOMBRE":
                                cuentaModif.setNombre(nuevoDato);
                                toNetwork.println("MODIFICACION EXITOSA");
                                break;
                            case "APELLIDO":
                                cuentaModif.setApellido(nuevoDato);
                                toNetwork.println("MODIFICACION EXITOSA");
                                break;
                            default:
                                toNetwork.println("ERROR. Parametro no permitido para modificacion");
                                break;
                        }
                    } else {
                        toNetwork.println("ERROR. Clave incorrecta");
                    }
                } else {
                    toNetwork.println("ERROR. Numero de cuenta invalido");
                }
                break;
            case "ELIMINACION":
                int numCuentaElim = Integer.parseInt(splitMessage[1]);
                String motivo = splitMessage[2];
                String claveElim = splitMessage[3];
                CuentaAhorros cuentaElim = cuentas.get(numCuentaElim);
                if (cuentaElim != null) {
                    String claveCuenta = cuentaElim.getClave();
                    if (claveCuenta.equals(claveElim)) {
                        double saldo = cuentaElim.getSaldo();
                        toNetwork.println("[Server] Saldo restante en la cuenta: " + saldo);
                        toNetwork.println("[Server] La cuenta número " + numCuentaElim + " ha sido eliminada por el siguiente motivo: " + motivo);
                        cuentas.remove(numCuentaElim);
                    } else {
                        toNetwork.println("[Server] La clave de acceso es incorrecta.");
                    }
                } else {
                    toNetwork.println("[Server] La cuenta número " + numCuentaElim + " no existe.");
                }
                break;
                //
            // case "CONSULTA":
                //
        }
    }
}

