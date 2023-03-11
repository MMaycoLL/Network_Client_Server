package entrega_final.servidor;

import entrega_final.cliente.Account;

import java.util.Map;


public class ServerHandler {
    public static Map<String, Account> cuentas;

    public static String consutarIdCuenta(String data) {
        System.out.println("Consultando ID...");
        String cedula = data.split("/")[2];
        String clave = data.split("/")[3];
        String resp = "La cuenta no fue encontrada...";
        for (Account account : cuentas.values()) {
            if (account.getCedula().equals(cedula) && account.getClave().equals(clave)) {
                resp = "ID cuenta: " + account.getNroCuenta();
            }
        }
        return resp;
    }

    public static String aperturaCuenta(String data) {
        try {
            if (ServerHandler.consultarCuenta(data)) {
                return "1";
            }
            System.out.println("Cuenta creada...");
            Account account = new Account();
            account.setNombre(data.split("/")[2]);
            account.setApellido(data.split("/")[3]);
            account.setCedula(data.split("/")[4]);
            account.setMonto(Double.parseDouble(data.split("/")[5]));
            account.setClave(data.split("/")[6]);
            cuentas.put(String.valueOf(account.getNroCuenta()), account);
            return "Account abierta..." +
                    "/ID: " + account.getNroCuenta() +
                    "/Nombre: " + account.getNombre() +
                    "/Apellido: " + account.getApellido() +
                    "/Monto depositado: " + account.getMonto() +
                    "/Clave: " + account.getClave();
        } catch (NumberFormatException ex) {
            return "El monto solo debe contener valores numéricos...";
        }
    }

    public static String modificacionInfoCuenta(String data) {
        String idCuenta = data.split("/")[2];
        String clave = data.split("/")[3];
        String campoMod = data.split("/")[4].toUpperCase();
        String nuevaInfo = data.split("/")[5];
        Account account = null;
        if (!(cuentas.containsKey(idCuenta) && cuentas.get(idCuenta).getClave().equals(clave))) {
            return "No se ha modificado la cuenta, la información ingresada es incorrecta.";
        }
        System.out.println("Modificando cuenta: " + idCuenta);
        account = cuentas.get(idCuenta);
        switch (campoMod) {
            case "NOMBRE":
                account.setNombre(nuevaInfo);
                break;
            case "APELLIDO":
                account.setApellido(nuevaInfo);
                break;
            case "CLAVE":
                account.setClave(nuevaInfo);
                break;
            default:
                return "El campo a modificar no existe.";
        }
        return "Cuenta modificada...";
    }

    public static String cerrarCuenta(String data) {
        String idCuenta = data.split("/")[2];
        String clave = data.split("/")[3];
        if (cuentas.containsKey(idCuenta) && cuentas.get(idCuenta).getClave().equals(clave)) {
            cuentas.remove(idCuenta);
            return "Cuenta cancelada...";
        }
        return "No se ha cerrado la cuenta, la información ingresada es incorrecta.";
    }

    public static boolean consultarCuenta(String data) {
        String cedula = data.split("/")[4];
        for (Account account : cuentas.values()) {
            if (account.getCedula().equals(cedula)) {
                return true;
            }
        }
        return false;
    }

    public static String transferenciaBancaria(String data) {
        String idCuentaOrigen = data.split("/")[2];
        String clave = data.split("/")[3];
        String idCuentaDestino = data.split("/")[4];
        String monto = data.split("/")[5];
        Account sourceAccount = null;
        Account destinationAccount = null;
        if (!(cuentas.containsKey(idCuentaOrigen) && cuentas.containsKey(idCuentaDestino) &&
                cuentas.get(idCuentaOrigen).getClave().equals(clave))) {
            return "No se ha realizado la transferencia --> la información ingresada es incorrecta.";
        }
        sourceAccount = cuentas.get(idCuentaOrigen);
        destinationAccount = cuentas.get(idCuentaDestino);
        destinationAccount.setMonto(destinationAccount.getMonto() + Double.parseDouble(monto));
        sourceAccount.setMonto(sourceAccount.getMonto() - Double.parseDouble(monto));
        return "Transferencia exitosa --> Monto actual: " + sourceAccount.getMonto();
    }

    public static String consignacionDinero(String data) {
        String idCuenta = data.split("/")[2];
        String cedula = data.split("/")[3];
        String monto = data.split("/")[4];
        Account account = null;
        if (!(cuentas.containsKey(idCuenta) && cuentas.get(idCuenta).getCedula().equals(cedula))) {
            return "No se ha consignado el monto -->la información ingresada es incorrecta.";
        }
        account = cuentas.get(idCuenta);
        account.setMonto(account.getMonto() + Double.parseDouble(monto));
        return "Consignación exitosa --> Monto actual: " + account.getMonto();
    }

    public static String retiroDinero(String data) {
        String idCuenta = data.split("/")[2];
        String cedula = data.split("/")[3];
        String monto = data.split("/")[4];
        String clave = data.split("/")[5];
        Account account = null;
        if (!(cuentas.containsKey(idCuenta) && cuentas.get(idCuenta).getCedula().equals(cedula) &&
                cuentas.get(idCuenta).getClave().equals(clave))) {
            return "No se ha retirado el monto --> la información ingresada es incorrecta.";
        }
        account = cuentas.get(idCuenta);
        account.setMonto(account.getMonto() - Double.parseDouble(monto));
        return "Monto retirado con exito -->  Monto actual: " + account.getMonto();
    }


}
