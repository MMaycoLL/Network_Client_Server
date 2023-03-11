package entrega_final.cliente;

import java.io.IOException;

import static entrega_final.cliente.EchoTCPClient.*;

public class ClientHandlerIterationOne {

    public static void consutarIdCuenta() throws IOException {
        System.out.print("Ingrese la siguiente información \n");
        System.out.print("Cedula: ");
        String cedula = SCANNER.nextLine().trim();


        System.out.print("\nClave: ");
        String clave = SCANNER.nextLine().trim();

        toNetwork.println(String.format("CUENTA/CONSULTAR/%s/%s", cedula, clave));
        String resp = fromNetwork.readLine();
        System.out.println(resp);
    }


    public static void aperturaCuenta() throws IOException {
        System.out.println("Ingrese la siguiente información:");

        String nombre;
        do {
            System.out.print("Nombre: ");
            nombre = SCANNER.nextLine().trim();
        } while (nombre.isEmpty());

        String apellido;
        do {
            System.out.print("Apellido: ");
            apellido = SCANNER.nextLine().trim();
        } while (apellido.isEmpty());

        String cedula;
        do {
            System.out.print("Cedula: ");
            cedula = SCANNER.nextLine().trim();
        } while (cedula.isEmpty());

        String monto;
        do {
            System.out.print("Monto a depositar: ");
            monto = SCANNER.nextLine().trim();
        } while (monto.isEmpty());

        String clave;
        do {
            System.out.print("Clave: ");
            clave = SCANNER.nextLine().trim();
        } while (clave.isEmpty());

        System.out.print("\n Cuenta creada con exito! \n ");
        // Enviar la información a través de la red
        String message = String.format("CUENTA/ABRIR/%s/%s/%s/%s/%s", nombre, apellido, cedula, monto, clave);
        toNetwork.println(message);

        // Leer la respuesta de la red y procesarla según corresponda
        String resp = fromNetwork.readLine().replace("/", "\n");
        if (resp.equals("1")) {
            System.out.println("La cuenta con el numero de documento introducido ya existe, intentelo de nuevo");

        }
    }


    public static void modificacionInfoCuenta() throws IOException {
        System.out.print("Ingrese la siguiente información");
        System.out.print("ID cuenta: ");
        String idCuenta = SCANNER.nextLine().trim();

        System.out.print("\tClave: ");
        String clave = SCANNER.nextLine().trim();

        System.out.print("\tDato a modificar (Nombre, Apellido, Clave): ");
        String infoMod = SCANNER.nextLine().trim();

        System.out.print("\tNuevo dato: ");
        String nuevaInfo = SCANNER.nextLine().trim();
        toNetwork.println(String.format("CUENTA/MODIFICAR/%s/%s/%s/%s", idCuenta, clave, infoMod, nuevaInfo));
        String resp = fromNetwork.readLine().trim();
        System.out.println(resp);
    }


    public static void cerrarCuenta() throws IOException {
        System.out.print("Ingrese la siguiente información");
        System.out.print("ID cuenta: ");
        String idCuenta = SCANNER.nextLine().trim();

        System.out.print("\n\tClave: ");
        String clave = SCANNER.nextLine().trim();

        System.out.print("\n\tMotivo de la cancelación: ");
        String motivo = SCANNER.nextLine().trim();

        toNetwork.println(String.format("CUENTA/CERRAR/%s/%s/%s", idCuenta, clave, motivo));
        String resp = fromNetwork.readLine();
        System.out.println(resp);
    }
}
