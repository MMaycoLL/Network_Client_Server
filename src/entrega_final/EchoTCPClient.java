package entrega_final;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class EchoTCPClient {
    private static final Scanner SCANNER = new Scanner(System.in);
    public static final String SERVER = "localhost";
    public static final int PORT = 4200;
    private PrintWriter toNetwork;
    private BufferedReader fromNetwork;
    private Socket clientSideSocket;

    public EchoTCPClient() {
        System.out.println("Echo TCP client is running ...");
    }

    public void init() throws Exception {
        clientSideSocket = new Socket(SERVER, PORT);
        createStreams(clientSideSocket);
        menu(clientSideSocket);
    }

    public void menu(Socket socket) throws Exception {
        /// Mensaje de bienvenida
        System.out.println("\t\tBienvenido al sistema bancario.");

        while (true) {
            // Imprimir opciones
            System.out.println("\n\t\033╔══════════════════════════════════╗");
            System.out.println("\t║                                  ║");
            System.out.println("\t║      1. Manejar cuenta           ║");
            System.out.println("\t║      2. Movimientos              ║");
            System.out.println("\t║      3. Salir del programa       ║");
            System.out.println("\t║                                  ║");
            System.out.println("\t╚══════════════════════════════════╝\n");

            // Configurar color de fuente
            System.out.print("\t- \033[0;33m");

            // Esperar entrada del usuario
            String input = SCANNER.nextLine();

            // Configurar color de fuente
            System.out.print("\033[0m");

            // Verificar opción seleccionada
            switch (input) {
                case "1":
                    protocoloCuenta();
                    break;
                case "2":
                    protocoloMovimiento();
                    break;
                case "3":
                    protocoloSalir(socket);
                    return;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        }
    }

    public void protocoloCuenta() throws Exception {
        while (true) {
            System.out.print("\n\tOPCIONES\n" +
                    "1\t====>\tConsultar ID cuenta\n" +
                    "2\t====>\tAbrir cuenta\n" +
                    "3\t====>\tModificar cuenta\n" +
                    "4\t====>\tCerrar cuenta\n" +
                    "5\t====>\tRegresar\n" +
                    "- ");

            int opc = Integer.parseInt(SCANNER.nextLine());
            System.out.flush();

            switch (opc) {
                case 1 -> protocoloConsultarId();
                case 2 -> protocoloAbrir();
                case 3 -> protocoloMod();
                case 4 -> protocoloCerrar();
                case 5 -> {
                    System.out.println("Regresando...");
                    return;
                }
                default -> System.out.println("Opción incorrecta...");
            }
        }
    }


    public void protocoloMovimiento() throws Exception {
        int opc = 0;
        do {
            System.out.println("\n\tOPCIONES");
            System.out.println("1\t====>\tConsignación");
            System.out.println("2\t====>\tTransferencia");
            System.out.println("3\t====>\tRetiro");
            System.out.println("4\t====>\tRegresar");
            System.out.print("- ");

            try {
                opc = Integer.parseInt(SCANNER.nextLine());
                System.out.flush();
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Intente de nuevo.");
                continue;
            }

            switch (opc) {
                case 1:
                    // Ejecutar el protocolo de consignación
                    protocoloConsignacion();
                    break;

                case 2:
                    // Ejecutar el protocolo de transferencia
                    protocoloTransferencia();
                    break;

                case 3:
                    // Ejecutar el protocolo de retiro
                    protocoloRetiro();
                    break;

                case 4:
                    // Salir del protocolo de movimiento
                    return;

                default:
                    System.out.println("Opción incorrecta...");
            }
        } while (opc != 4);
    }

    private void protocoloConsignacion() throws IOException {
        System.out.print("Ingrese la siguiente información" +
                "\nID cuenta: ");
        String idCuenta = SCANNER.nextLine();
        System.out.print("Cedula: ");
        String cedula = SCANNER.nextLine();
        System.out.print("Monto: ");
        String monto = SCANNER.nextLine();
        toNetwork.println("MOVI/CONSIG/" + idCuenta + "/" + cedula + "/" + monto);
        String resp = fromNetwork.readLine();
        System.out.println(resp);
    }

    private void protocoloRetiro() throws IOException {
        System.out.print("Ingrese la siguiente información" +
                "\nCedula: ");
        String cedula = SCANNER.nextLine();
        System.out.print("ID cuenta: ");
        String idCuenta = SCANNER.nextLine();
        System.out.print("Monto a retirar: ");
        String monto = SCANNER.nextLine();
        System.out.print("Clave: ");
        String clave = SCANNER.nextLine();
        toNetwork.println("MOVI/RETIRO/" + idCuenta + "/" + cedula + "/" + monto + "/" + clave);
        String resp = fromNetwork.readLine();
        System.out.println(resp);
    }

    private void protocoloTransferencia() throws IOException {
        System.out.print("Ingrese la siguiente información" +
                "\nID cuenta origen: ");
        String idCuentaOrigen = SCANNER.nextLine();
        System.out.print("Clave: ");
        String clave = SCANNER.nextLine();
        System.out.print("ID cuenta destino: ");
        String idCuentaDestino = SCANNER.nextLine();
        System.out.print("Monto: ");
        String monto = SCANNER.nextLine();
        toNetwork.println("MOVI/TRANSFER/" + idCuentaOrigen + "/" + clave + "/" + idCuentaDestino + "/" + monto);
        String resp = fromNetwork.readLine();
        System.out.println(resp);
    }

    public void protocoloConsultarId() throws IOException {
        System.out.print("Ingrese la siguiente información" +
                "\nCédula: ");
        String cedula = SCANNER.nextLine();
        System.out.print("\nClave: ");
        String clave = SCANNER.nextLine();
        toNetwork.println("CUENTA/CONSUL/" + cedula + "/" + clave);
        String resp = fromNetwork.readLine();
        System.out.println(resp);
    }

    public void protocoloAbrir() throws IOException {
        System.out.println("Ingrese la siguiente información");
        System.out.print("Nombre: ");
        String nombre = SCANNER.nextLine();
        System.out.print("Apellido: ");
        String apellido = SCANNER.nextLine();
        System.out.print("Cedula: ");
        String cedula = SCANNER.nextLine();
        System.out.print("Monto a depositar: ");
        String monto = SCANNER.nextLine();
        System.out.print("Clave: ");
        String clave = SCANNER.nextLine();

        String request = String.format("CUENTA/ABRIR/%s/%s/%s/%s/%s", nombre, apellido, cedula, monto, clave);
        toNetwork.println(request);

        String resp = fromNetwork.readLine().replace("/", "\n");

        if (resp.equals("1")) {
            System.out.println("La cuenta ya existe, desea modificar la información?");
            System.out.println("1\tSi");
            System.out.println("2\tNo");
            int opc = Integer.parseInt(SCANNER.nextLine());

            if (opc == 1) {
                protocoloMod();
            }
        } else {
            System.out.println("\n" + resp + "\n");
        }
    }

    public void protocoloMod() throws IOException {
        System.out.println("Ingrese la siguiente información");
        System.out.print("\tID Cuenta: ");
        String idCuenta = SCANNER.nextLine();
        System.out.print("\tClave: ");
        String clave = SCANNER.nextLine();
        System.out.print("\tInformación a modificar (Nombre, Apellido, Clave): ");
        String infoMod = SCANNER.nextLine();
        System.out.print("\tNueva información: ");
        String nuevaInfo = SCANNER.nextLine();
        String mensaje = String.format("CUENTA/MOD/%s/%s/%s/%s", idCuenta, clave, infoMod, nuevaInfo);
        toNetwork.println(mensaje);
        String resp = fromNetwork.readLine();
        System.out.println(resp);
    }

    public void protocoloCerrar() throws IOException {
        System.out.print("Ingrese la siguiente información" +
                "\n\tID cuenta: ");
        String idCuenta = SCANNER.nextLine();
        System.out.print("\n\tClave: ");
        String clave = SCANNER.nextLine();
        System.out.print("\n\tMotivo de la cancelación: ");
        String motivo = SCANNER.nextLine();
        toNetwork.println("CUENTA/CERRAR/" + idCuenta + "/" + clave + "/" + motivo);
        String resp = fromNetwork.readLine();
        System.out.println(resp);
    }

    public void protocoloSalir(Socket socket) throws IOException {
        toNetwork.println("SALIR");
        String resp = fromNetwork.readLine();
        System.out.println(resp);
        System.out.println("Gracias por utilizar la aplicación :)");
        socket.close();
    }

    private void createStreams(Socket socket) throws Exception {
        toNetwork = new PrintWriter(socket.getOutputStream(), true);
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static void main(String args[]) throws Exception {
        EchoTCPClient ec = new EchoTCPClient();
        ec.init();
    }
}