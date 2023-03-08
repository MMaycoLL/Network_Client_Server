package entrega_final.servidor;


import entrega_final.cliente.Account;

import java.io.*;
import java.util.Map;

import static entrega_final.servidor.ServerHandler.cuentas;
import static entrega_final.servidor.EchoTCPServer.fileName;


public class AccountsFileManager {

    public static void writeAccountsToFile() {
        try {
            FileWriter fw = new FileWriter(fileName);
            PrintWriter pw = new PrintWriter(fw);
            for (Map.Entry<String, Account> entry : cuentas.entrySet()) {
                String id = entry.getKey();
                Account account = entry.getValue();
                pw.println(id + "/" + account.getNombre() + "/" + account.getApellido() + "/" + account.getCedula() + "/" + account.getMonto() + "/" + account.getClave());

            }
            pw.flush();
            pw.close();
        } catch (IOException ex) {
            System.out.println("Error al escribir las cuentas en el archivo: " + ex.getMessage());
        }
    }

    public static void readAccountsFromFile() {
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("/");
                String id = parts[0];
                String nombre = parts[1];
                String apellido = parts[2];
                String cedula = parts[3];
                double monto = Double.parseDouble(parts[4]);
                String clave = parts[5];

                Account account = new Account();
                account.setNroCuenta(id);
                account.setNombre(nombre);
                account.setApellido(apellido);
                account.setCedula(cedula);
                account.setMonto(monto);
                account.setClave(clave);

                cuentas.put(id, account);
            }
            br.close();
        } catch (IOException ex) {
            System.out.println("Error al leer las cuentas del archivo: " + ex.getMessage());
        }
    }


}
