package entrega_final.cliente;

import java.io.IOException;

import static entrega_final.cliente.EchoTCPClient.*;

public class ClientHandlerIterationOne {

    public static void consutarIdCuenta() throws IOException {
        System.out.print("Ingrese la siguiente información");
        System.out.print("Cedula: ");
        String cedula = SCANNER.nextLine();


        System.out.print("\nClave: ");
        String clave = SCANNER.nextLine();

        toNetwork.println(String.format("CUENTA/CONSULTAR/%s/%s", cedula, clave));
        String resp = fromNetwork.readLine();
        System.out.println(resp);
    }


    public static void aperturaCuenta() throws IOException {
        System.out.print("Ingrese la siguiente información");
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
        String message = String.format("CUENTA/ABRIR/%s/%s/%s/%s/%s", nombre, apellido, cedula, monto, clave);
        toNetwork.println(message);
        String resp = fromNetwork.readLine().replace("/", "\n");
        if (resp.equals("1")) {
            System.out.println("La cuenta ya existe, desea modificar la información?\n1\tSi\n2\tNo");
            int opc = Integer.parseInt(SCANNER.nextLine());
            switch (opc) {
                case 1:
                    modificacionInfoCuenta();
                    break;
                case 2:
                    return;
            }
        } else {
            System.out.println("\n" + resp + "\n");
        }
    }


    public static void modificacionInfoCuenta() throws IOException {
        System.out.print("Ingrese la siguiente información");
        System.out.print("ID cuenta: ");
        String idCuenta = SCANNER.nextLine();

        System.out.print("\tClave: ");
        String clave = SCANNER.nextLine();

        System.out.print("\tDato a modificar (Nombre, Apellido, Clave): ");
        String infoMod = SCANNER.nextLine();

        System.out.print("\tNuevo dato: ");
        String nuevaInfo = SCANNER.nextLine();
        toNetwork.println(String.format("CUENTA/MODIFICAR/%s/%s/%s/%s", idCuenta, clave, infoMod, nuevaInfo));
        String resp = fromNetwork.readLine();
        System.out.println(resp);
    }


    public static void cerrarCuenta() throws IOException {
        System.out.print("Ingrese la siguiente información");
        System.out.print("ID cuenta: ");
        String idCuenta = SCANNER.nextLine();

        System.out.print("\n\tClave: ");
        String clave = SCANNER.nextLine();

        System.out.print("\n\tMotivo de la cancelación: ");
        String motivo = SCANNER.nextLine();

        toNetwork.println(String.format("CUENTA/CERRAR/%s/%s/%s", idCuenta, clave, motivo));
        String resp = fromNetwork.readLine();
        System.out.println(resp);
    }
}
