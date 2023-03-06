package entrega_final;

import java.util.Map;

public class ProtocolHandler {
    public static String protocoloTransferencia(Map<String, Cuenta> cuentas, String data) {
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
}
