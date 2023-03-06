package entrega_final;

import java.util.Map;

public class HandlerIteracion2 {

    public static String consignacionDinero(Map<String, Cuenta> cuentas, String data) {
        // Se extraen los datos de la cadena recibida
        String[] parts = data.split("/");
        String idCuenta = parts[2];
        String cedula = parts[3];
        String monto = parts[4];

        // Se verifica si la cuenta existe y si la cédula ingresada es correcta
        if (!cuentas.containsKey(idCuenta) || !cuentas.get(idCuenta).getCedula().equals(cedula)) {
            return "No se ha consignado el monto/la información ingresada es incorrecta.";
        }

        // Se actualiza el monto de la cuenta
        Cuenta cuenta = cuentas.get(idCuenta);
        cuenta.setMonto(cuenta.getMonto() + Double.parseDouble(monto));

        // Se retorna la respuesta exitosa junto con el monto actualizado
        return "Consignación exitosa/Monto actual: " + cuenta.getMonto();
    }

    public static String tranferenciaBamcaria(Map<String, Cuenta> cuentas, String data) {
        // Obtener los datos necesarios de la petición
        String idCuentaOrigen = data.split("/")[2];
        String clave = data.split("/")[3];
        String idCuentaDestino = data.split("/")[4];
        String monto = data.split("/")[5];

        // Verificar que ambas cuentas existen y la clave de la cuenta de origen es correcta
        if (!(cuentas.containsKey(idCuentaOrigen) && cuentas.containsKey(idCuentaDestino) && cuentas.get(idCuentaOrigen).getClave().equals(clave))) {
            return "No se ha realizado la transferencia/la información ingresada es incorrecta.";
        }

        // Obtener las cuentas de origen y destino
        Cuenta cuentaOrigen = cuentas.get(idCuentaOrigen);
        Cuenta cuentaDestino = cuentas.get(idCuentaDestino);

        // Realizar la transferencia
        cuentaDestino.setMonto(cuentaDestino.getMonto() + Double.parseDouble(monto));
        cuentaOrigen.setMonto(cuentaOrigen.getMonto() - Double.parseDouble(monto));

        // Retornar un mensaje de éxito con el monto actual de la cuenta de origen
        return "Transferencia exitosa/Monto actual: " + cuentaOrigen.getMonto();
    }

    public static String retiroDineroCuenta(Map<String, Cuenta> cuentas, String data) {
        // Obtener los datos de la transacción
        String idCuenta = data.split("/")[2];
        String cedula = data.split("/")[3];
        String monto = data.split("/")[4];
        String clave = data.split("/")[5];

        // Verificar que la cuenta existe y que la información ingresada es correcta
        if (!(cuentas.containsKey(idCuenta) && cuentas.get(idCuenta).getCedula().equals(cedula) && cuentas.get(idCuenta).getClave().equals(clave))) {
            return "No se ha retirado el monto/la información ingresada es incorrecta.";
        }

        // Realizar el retiro
        Cuenta cuenta = cuentas.get(idCuenta);
        cuenta.setMonto(cuenta.getMonto() - Double.parseDouble(monto));

        // Retornar el mensaje de respuesta de la transacción
        return "Monto retirado con exito/Monto actual: " + cuenta.getMonto();
    }
}
