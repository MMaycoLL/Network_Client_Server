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
        System.out.println("Creando socket...");
        clientSideSocket = new Socket(SERVER, PORT);
        createStreams(clientSideSocket);
        menu(clientSideSocket);
    }

    public void menu(Socket socket) throws Exception {
        int opc;
        do{
            System.out.print("\n\tOPCIONES\n" +
                    "1\t====>\tManejar cuenta\n" +
                    "2\t====>\tMovimientos\n" +
                    "3\t====>\tSalir del programa\n" +
                    "- ");
            opc = Integer.parseInt(SCANNER.nextLine());
            switch (opc) {
                case 1:
                    protocoloCuenta();
                    break;

                case 2:
                    protocoloMovimiento();
                    break;

                case 3:
                    protocoloSalir(socket);
                    break;

                default:
                    System.out.println("Opción incorrecta...");
            }
        }while (!(opc==3));
    }

    public void protocoloCuenta() throws Exception {
        int opc;
        do{
            System.out.print("\n\tOPCIONES\n" +
                    "1\t====>\tConsultar ID cuenta\n" +
                    "2\t====>\tAbrir cuenta\n" +
                    "3\t====>\tModificar cuenta\n" +
                    "4\t====>\tCerrar cuenta\n" +
                    "5\t====>\tRegresar\n" +
                    "- ");
            opc = Integer.parseInt(SCANNER.nextLine());
            System.out.flush();
            switch (opc) {
                case 1:
                    protocoloConsultarId();
                    break;

                case 2:
                    protocoloAbrir();
                    break;

                case 3:
                    protocoloMod();
                    break;

                case 4:
                    protocoloCerrar();
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Opción incorrecta...");
            }
        }while (true);
    }

    public void protocoloMovimiento() throws Exception {
        int opc;
        do{
            System.out.print("\n\tOPCIONES\n" +
                    "1\t====>\tConsignación\n" +
                    "2\t====>\tTransferencia\n" +
                    "3\t====>\tRetiro\n" +
                    "4\t====>\tRegresar\n" +
                    "- ");
            opc = Integer.parseInt(SCANNER.nextLine());
            System.out.flush();
            switch (opc) {
                case 1:
                    protocoloConsignacion();
                    break;

                case 2:
                    protocoloTransferencia();
                    break;

                case 3:
                    protocoloRetiro();
                    break;

                case 4:
                    return;

                default:
                    System.out.println("Opción incorrecta...");
            }
        }while (true);
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
        System.out.print("Ingrese la siguiente información" +
                "\nNombre: ");
        String nombre = SCANNER.nextLine();
        System.out.print("Apellido: ");
        String apellido =  SCANNER.nextLine();
        System.out.print("Cedula: ");
        String cedula =  SCANNER.nextLine();
        System.out.print("Monto a depositar: ");
        String monto =  SCANNER.nextLine();
        System.out.print("Clave: ");
        String clave =  SCANNER.nextLine();
        toNetwork.println("CUENTA/ABRIR/" + nombre + "/" + apellido + "/" + cedula + "/" + monto + "/" + clave);
        String resp = fromNetwork.readLine().replace("/", "\n");
        if(resp.equals("1")){
            System.out.println("La cuenta ya existe, desea modificar la información?" +
                    "\n1\tSi" +
                    "\n2\tNo");
            int opc = Integer.parseInt(SCANNER.nextLine());
            switch (opc){
                case 1:
                    protocoloMod();
                    break;
                case 2:
                    return;
            }
        }else {
            System.out.println("\n" + resp + "\n");
        }
    }
    public void protocoloMod() throws IOException {
        System.out.print("Ingrese la siguiente información" +
                "\n\tID Cuenta: ");
        String idCuenta = SCANNER.nextLine();
        System.out.print("\tClave: ");
        String clave = SCANNER.nextLine();
        System.out.print("\tInformación a modificar (Nombre, Apellido, Clave): ");
        String infoMod = SCANNER.nextLine();
        System.out.print("\tNueva información: ");
        String nuevaInfo = SCANNER.nextLine();
        toNetwork.println("CUENTA/MOD/" + idCuenta + "/" + clave + "/" + infoMod + "/" + nuevaInfo);
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