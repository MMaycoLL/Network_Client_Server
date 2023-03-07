package entrega_final.servidor;


import entrega_final.cliente.Account;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class EchoTCPServer {
    public static final int PORT = 4200;
    private ServerSocket listener;
    private Socket serverSideSocket;
    private PrintWriter toNetwork;
    private BufferedReader fromNetwork;

    private Map<String, Account> cuentas;

    private String fileName = "datos.txt";

    public EchoTCPServer() {
        System.out.println("Echo TCP server is running on port: " + PORT);
    }

    public static void main(String args[]) throws Exception {
        EchoTCPServer es = new EchoTCPServer();
        es.init();
    }

    public void init() throws Exception {
        listener = new ServerSocket(PORT);
        cuentas = new HashMap<>();
        readCuentasFromFile();
        while (true) {
            serverSideSocket = listener.accept();
            System.out.println("Cliente recibido desde: " + serverSideSocket.getInetAddress().getHostAddress());
            createStreams(serverSideSocket);
            while (!serverSideSocket.isClosed()) {
                menu(serverSideSocket);
            }
        }
    }

    public void menu(Socket socket) throws Exception {
        String data = fromNetwork.readLine();
        String opc = data.split("/")[0];
        System.out.println("Opc: " + opc);
        System.out.println(data);
        String resp = "";
        switch (opc) {
            case "CUENTA":
                resp = protocoloCuenta(data);
                break;

            case "MOVIMIENTO":
                resp = protocoloMovimiento(data);
                break;

            case "SALIR":
                protocoloSalir(socket);
                return;
        }
        System.out.println("Server response: " + resp);
        System.out.println(cuentas);
        toNetwork.println(resp);
        writeCuentasToFile();
    }

    public String protocoloMovimiento(String data) {
        String opc = data.split("/")[1];
        System.out.println("Opc: " + opc);
        String resp = "";
        switch (opc) {
            case "RETIRO":
                resp = protocoloRetiro(data);
                break;

            case "CONSIGNACION":
                resp = protocoloConsignacion(data);
                break;

            case "TRANSFERENCIA":
                resp = protocoloTransferencia(data);
                break;
        }
        return resp;
    }

    private String protocoloTransferencia(String data) {
        String idCuentaOrigen = data.split("/")[2];
        String clave = data.split("/")[3];
        String idCuentaDestino = data.split("/")[4];
        String monto = data.split("/")[5];
        Account sourceAccount = null;
        Account destinationAccount = null;
        if (!(cuentas.containsKey(idCuentaOrigen) && cuentas.containsKey(idCuentaDestino) &&
                cuentas.get(idCuentaOrigen).getClave().equals(clave))) {
            return "No se ha realizado la transferencia/la información ingresada es incorrecta.";
        }
        sourceAccount = cuentas.get(idCuentaOrigen);
        destinationAccount = cuentas.get(idCuentaDestino);
        destinationAccount.setMonto(destinationAccount.getMonto() + Double.parseDouble(monto));
        sourceAccount.setMonto(sourceAccount.getMonto() - Double.parseDouble(monto));
        return "Transferencia exitosa/Monto actual: " + sourceAccount.getMonto();
    }

    private String protocoloConsignacion(String data) {
        String idCuenta = data.split("/")[2];
        String cedula = data.split("/")[3];
        String monto = data.split("/")[4];
        Account account = null;
        if (!(cuentas.containsKey(idCuenta) && cuentas.get(idCuenta).getCedula().equals(cedula))) {
            return "No se ha consignado el monto/la información ingresada es incorrecta.";
        }
        account = cuentas.get(idCuenta);
        account.setMonto(account.getMonto() + Double.parseDouble(monto));
        return "Consignación exitosa/Monto actual: " + account.getMonto();
    }

    private String protocoloRetiro(String data) {
        String idCuenta = data.split("/")[2];
        String cedula = data.split("/")[3];
        String monto = data.split("/")[4];
        String clave = data.split("/")[5];
        Account account = null;
        if (!(cuentas.containsKey(idCuenta) && cuentas.get(idCuenta).getCedula().equals(cedula) &&
                cuentas.get(idCuenta).getClave().equals(clave))) {
            return "No se ha retirado el monto/la información ingresada es incorrecta.";
        }
        account = cuentas.get(idCuenta);
        account.setMonto(account.getMonto() - Double.parseDouble(monto));
        return "Monto retirado con exito/Monto actual: " + account.getMonto();
    }

    public String protocoloCuenta(String data) throws Exception {
        String opc = data.split("/")[1];
        System.out.println("Opc: " + opc);
        String resp = "";
        switch (opc) {
            case "CONSULTAR":
                resp = protocoloConsultarId(data);
                break;

            case "ABRIR":
                resp = protocoloAbrir(data);
                break;

            case "MODIFICAR":
                resp = protocoloMod(data);
                break;

            case "CERRAR":
                resp = protocoloCerrar(data);
                break;
        }
        return resp;
    }

    public String protocoloConsultarId(String data) {
        System.out.println("Consultando ID...");
        String cedula = data.split("/")[2];
        String clave = data.split("/")[3];
        String resp = "La cuenta no fue hallada...";
        for (Account account : cuentas.values()) {
            if (account.getCedula().equals(cedula) && account.getClave().equals(clave)) {
                resp = "ID account: " + account.getNroCuenta();
            }
        }
        return resp;
    }

    public String protocoloAbrir(String data) {
        try {
            if (consultarCuenta(data)) {
                return "1";
            }
            System.out.println("Abriendo account...");
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

    public String protocoloMod(String data) {
        String idCuenta = data.split("/")[2];
        String clave = data.split("/")[3];
        String campoMod = data.split("/")[4].toUpperCase();
        String nuevaInfo = data.split("/")[5];
        Account account = null;
        if (!(cuentas.containsKey(idCuenta) && cuentas.get(idCuenta).getClave().equals(clave))) {
            return "No se ha modificado la account, la información ingresada es incorrecta.";
        }
        System.out.println("Modificando account: " + idCuenta);
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
        return "Account modificada...";
    }

    public String protocoloCerrar(String data) {
        String idCuenta = data.split("/")[2];
        String clave = data.split("/")[3];
        if (cuentas.containsKey(idCuenta) && cuentas.get(idCuenta).getClave().equals(clave)) {
            cuentas.remove(idCuenta);
            return "Account cerrada...";
        }
        return "No se ha cerrado la cuenta, la información ingresada es incorrecta.";
    }

    public void protocoloSalir(Socket socket) throws IOException {
        System.out.println("Cerrando conexión con el cliente: " + socket.getInetAddress().getHostAddress());
        toNetwork.println("Adios desde el servidor :D");
        socket.close();
    }

    public boolean consultarCuenta(String data) {
        String cedula = data.split("/")[4];
        for (Account account : cuentas.values()) {
            if (account.getCedula().equals(cedula)) {
                return true;
            }
        }
        return false;
    }

    private void createStreams(Socket socket) throws Exception {
        toNetwork = new PrintWriter(socket.getOutputStream(), true);
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void writeCuentasToFile() {
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

    private void readCuentasFromFile() {
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
                // account.setNroCuenta(id);
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
