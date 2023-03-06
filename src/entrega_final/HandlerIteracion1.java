package entrega_final;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class HandlerIteracion1 {

    public static String consultarIdCuenta(Map<String, Cuenta> cuentas, String data) {
        System.out.println("Consultando ID...");
        String cedula = data.split("/")[2];
        String clave = data.split("/")[3];
        String resp = "La cuenta no fue hallada...";
        for (Cuenta cuenta : cuentas.values()) {
            if (cuenta.getCedula().equals(cedula) && cuenta.getClave().equals(clave)) {
                resp = "ID cuenta: " + cuenta.getNroCuenta();
            }
        }
        return resp;
    }


    // Definimos una función para modificar una cuenta según los datos recibidos en la variable `data`.
    public static String modificacionInfoCuenta(Map<String, Cuenta> cuentas, String data) {

        // Dividimos la cadena `data` en sus componentes y los almacenamos en un arreglo.
        String[] parts = data.split("/");

        // Obtenemos el ID de la cuenta a modificar, la clave y la información a modificar.
        String idCuenta = parts[2];
        String clave = parts[3];
        String campoMod = parts[4].toUpperCase();
        String nuevaInfo = parts[5];

        // Verificamos si la cuenta existe y si la clave es correcta.
        if (!cuentas.containsKey(idCuenta) || !cuentas.get(idCuenta).getClave().equals(clave)) {
            return "No se ha modificado la cuenta, la información ingresada es incorrecta.";
        }

        // Si la cuenta existe y la clave es correcta, modificamos la cuenta.
        System.out.println("Realizando modificacion de la cuenta: " + idCuenta);
        Cuenta cuenta = cuentas.get(idCuenta);

        switch (campoMod) {
            case "NOMBRE":
                cuenta.setNombre(nuevaInfo);
                break;
            case "APELLIDO":
                cuenta.setApellido(nuevaInfo);
                break;
            case "CLAVE":
                cuenta.setClave(nuevaInfo);
                break;
            default:
                return "El campo a no existe o no tiene permitido la modificacion.";
        }

        return "Cuenta modificada con exito...";
    }

    public static String cierreCuenta(Map<String, Cuenta> cuentas, String data) {
        String idCuenta = data.split("/")[2];
        String clave = data.split("/")[3];
        if (cuentas.containsKey(idCuenta) && cuentas.get(idCuenta).getClave().equals(clave)) {
            cuentas.remove(idCuenta);
            return "Cuenta cerrada...";
        }
        return "No se ha cerrado la cuenta, la información ingresada es incorrecta.";
    }

    public static void protocoloSalir(Socket socket) throws IOException {
        System.out.println("Cerrando conexión con el cliente: " + socket.getInetAddress().getHostAddress());
        EchoTCPServer.toNetwork.println("Conexion cerrada con exito.....");
        socket.close();
    }
}
