package entrega_final.servidor;


import entrega_final.cliente.Account;

import java.io.*;
import java.util.Map;

import static entrega_final.servidor.EchoTCPServer.fileName;
import static entrega_final.servidor.ServerHandler.cuentas;


public class AccountsFileManager {

    public static void writeAccountsToFile() {
        try {
            // Abre un archivo para escribir
            FileWriter fw = new FileWriter(fileName);
            // Crea un objeto PrintWriter que escribirá en el archivo
            PrintWriter pw = new PrintWriter(fw);
            // Itera sobre cada entrada del mapa de cuentas
            for (Map.Entry<String, Account> entry : cuentas.entrySet()) {
                String id = entry.getKey();
                Account account = entry.getValue();
                // Escribe cada cuenta en una línea del archivo separando los campos con "/"
                pw.println(id + "/" + account.getNombre() + "/" + account.getApellido() + "/" + account.getCedula() + "/" + account.getMonto() + "/" + account.getClave());
            }
            // Limpia cualquier dato pendiente en el buffer del PrintWriter y lo cierra
            pw.flush();
            pw.close();
        } catch (IOException ex) {
            // Si ocurre algún error al escribir el archivo, imprime un mensaje de error
            System.out.println("Error al escribir las cuentas en el archivo: " + ex.getMessage());
        }
    }


    public static void readAccountsFromFile() {
        try {
            // Abrir el archivo y crear un BufferedReader para leerlo línea por línea
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String line;

            // Leer el archivo línea por línea hasta que no haya más líneas
            while ((line = br.readLine()) != null) {
                // Dividir cada línea en partes utilizando el caracter separador "/"
                String[] parts = line.split("/");

                // Obtener los valores de cada parte y guardarlos en variables
                String id = parts[0];
                String nombre = parts[1];
                String apellido = parts[2];
                String cedula = parts[3];
                double monto = Double.parseDouble(parts[4]);
                String clave = parts[5];

                // Crear una nueva cuenta y establecer sus valores utilizando los datos leídos del archivo
                Account account = new Account();
                account.setNroCuenta(id);
                account.setNombre(nombre);
                account.setApellido(apellido);
                account.setCedula(cedula);
                account.setMonto(monto);
                account.setClave(clave);

                // Añadir la cuenta al mapa de cuentas
                cuentas.put(id, account);
            }

            // Cerrar el BufferedReader
            br.close();
        } catch (IOException ex) {
            // Si ocurre un error al leer el archivo, mostrar un mensaje de error
            System.out.println("Error al leer las cuentas del archivo: " + ex.getMessage());
        }
    }


}
