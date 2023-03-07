package entrega_final.cliente;

import java.io.IOException;

import static entrega_final.cliente.EchoTCPClient.SCANNER;
import static entrega_final.cliente.EchoTCPClient.fromNetwork;
import static entrega_final.cliente.EchoTCPClient.toNetwork;

public class ClientHandlerIterationTwo {

    public static void consignacionDinero() throws IOException {
        System.out.println("Ingrese la siguiente información:");
        System.out.print("ID cuenta: ");
        String idCuenta = SCANNER.nextLine();

        System.out.print("Cedula: ");
        String cedula = SCANNER.nextLine();

        System.out.print("Monto: ");
        String monto = SCANNER.nextLine();

        String mensaje = String.format("MOVI/CONSIG/%s/%s/%s", idCuenta, cedula, monto);
        toNetwork.println(mensaje);

        String resp = fromNetwork.readLine();
        System.out.println(resp);
    }


    public static void transferenciaBancaria() throws IOException {
        System.out.println("Ingrese la siguiente información");
        System.out.print("ID cuenta origen: ");
        String idCuentaOrigen = SCANNER.nextLine();

        System.out.print("Clave: ");
        String clave = SCANNER.nextLine();

        System.out.print("ID cuenta destino: ");
        String idCuentaDestino = SCANNER.nextLine();

        System.out.print("Monto: ");
        String monto = SCANNER.nextLine();

        String request = String.format("MOVIMIENTO/TRANSFERENCIA/%s/%s/%s/%s", idCuentaOrigen, clave, idCuentaDestino, monto);
        toNetwork.println(request);

        String resp = fromNetwork.readLine();
        System.out.println(resp);
    }


    public static void retiroDinero() throws IOException {
        System.out.print("Ingrese la siguiente información");
        System.out.print("Cédula: ");
        String cedula = SCANNER.nextLine();

        System.out.print("ID cuenta: ");
        String idCuenta = SCANNER.nextLine();

        System.out.print("Monto a retirar: ");
        String monto = SCANNER.nextLine();

        System.out.print("Clave: ");
        String clave = SCANNER.nextLine();

        String message = String.format("MOVIMIENTO/RETIRO/%s/%s/%s/%s", idCuenta, cedula, monto, clave);
        toNetwork.println(message);
        String resp = fromNetwork.readLine();
        System.out.println(resp);
    }
}
