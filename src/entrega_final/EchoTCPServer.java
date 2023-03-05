package entrega_final;

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

    private Map<String, Cuenta> cuentas;

    private String fileName = "mensaje.txt";
    public EchoTCPServer() {
        System.out.println("Echo TCP server is running on port: " + PORT);
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
        switch (opc){
            case "CUENTA":
                resp = protocoloCuenta(data);
                break;

            case "MOVI":
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


    public String protocoloMovimiento(String data){
        String opc = data.split("/")[1];
        System.out.println("Opc: " + opc);
        String resp = "";
        switch (opc){
            case "RETIRO":
                resp = protocoloRetiro(data);
                break;

            case "CONSIG":
                resp = protocoloConsignacion(data);
                break;

            case "TRANSFER":
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
        Cuenta cuentaOrigen = null;
        Cuenta cuentaDestino = null;
        if(!(cuentas.containsKey(idCuentaOrigen) && cuentas.containsKey(idCuentaDestino) &&
                cuentas.get(idCuentaOrigen).getClave().equals(clave))){
            return "No se ha realizado la transferencia/la información ingresada es incorrecta.";
        }
        cuentaOrigen = cuentas.get(idCuentaOrigen);
        cuentaDestino = cuentas.get(idCuentaDestino);
        cuentaDestino.setMonto(cuentaDestino.getMonto() + Double.parseDouble(monto));
        cuentaOrigen.setMonto(cuentaOrigen.getMonto() - Double.parseDouble(monto));
        return "Transferencia exitosa/Monto actual: " + cuentaOrigen.getMonto();
    }

    private String protocoloConsignacion(String data) {
        String idCuenta = data.split("/")[2];
        String cedula = data.split("/")[3];
        String monto = data.split("/")[4];
        Cuenta cuenta = null;
        if(!(cuentas.containsKey(idCuenta) && cuentas.get(idCuenta).getCedula().equals(cedula))){
            return "No se ha consignado el monto/la información ingresada es incorrecta.";
        }
        cuenta = cuentas.get(idCuenta);
        cuenta.setMonto(cuenta.getMonto() + Double.parseDouble(monto));
        return "Consignación exitosa/Monto actual: " + cuenta.getMonto();
    }

    private String protocoloRetiro(String data) {
        String idCuenta = data.split("/")[2];
        String cedula = data.split("/")[3];
        String monto = data.split("/")[4];
        String clave = data.split("/")[5];
        Cuenta cuenta = null;
        if(!(cuentas.containsKey(idCuenta) && cuentas.get(idCuenta).getCedula().equals(cedula) &&
                cuentas.get(idCuenta).getClave().equals(clave))){
            return "No se ha retirado el monto/la información ingresada es incorrecta.";
        }
        cuenta = cuentas.get(idCuenta);
        cuenta.setMonto(cuenta.getMonto() - Double.parseDouble(monto));
        return "Monto retirado con exito/Monto actual: " + cuenta.getMonto();
    }

    public String protocoloCuenta(String data) throws Exception {
        String opc = data.split("/")[1];
        System.out.println("Opc: " + opc);
        String resp = "";
        switch (opc){
            case "CONSUL":
                resp = protocoloConsultarId(data);
                break;

            case "ABRIR":
                resp = protocoloAbrir(data);
                break;

            case "MOD":
                resp = protocoloMod(data);
                break;

            case "CERRAR":
                resp = protocoloCerrar(data);
                break;
        }
        return resp;
    }

    public String protocoloConsultarId(String data){
        System.out.println("Consultando ID...");
        String cedula = data.split("/")[2];
        String clave = data.split("/")[3];
        String resp = "La cuenta no fue hallada...";
        for(Cuenta cuenta: cuentas.values()){
            if(cuenta.getCedula().equals(cedula) && cuenta.getClave().equals(clave)) {
                resp = "ID cuenta: " + cuenta.getNroCuenta();
            }
        }
        return resp;
    }

    public String protocoloAbrir(String data) {
        try {
            if (consultarCuenta(data)) {
                return "1";
            }
            System.out.println("Abriendo cuenta...");
            Cuenta cuenta = new Cuenta();
            cuenta.setNombre(data.split("/")[2]);
            cuenta.setApellido(data.split("/")[3]);
            cuenta.setCedula(data.split("/")[4]);
            cuenta.setMonto(Double.parseDouble(data.split("/")[5]));
            cuenta.setClave(data.split("/")[6]);
            cuentas.put(String.valueOf(cuenta.getNroCuenta()), cuenta);
            return "Cuenta abierta..." +
                    "/ID: " + cuenta.getNroCuenta() +
                    "/Nombre: " + cuenta.getNombre() +
                    "/Apellido: " + cuenta.getApellido() +
                    "/Monto depositado: " + cuenta.getMonto() +
                    "/Clave: " + cuenta.getClave();
        }catch (NumberFormatException ex){
            return "El monto solo debe contener valores numéricos...";
        }
    }

    public String protocoloMod(String data){
        String idCuenta = data.split("/")[2];
        String clave = data.split("/")[3];
        String campoMod = data.split("/")[4].toUpperCase();
        String nuevaInfo = data.split("/")[5];
        Cuenta cuenta = null;
        if(!(cuentas.containsKey(idCuenta) && cuentas.get(idCuenta).getClave().equals(clave))){
            return "No se ha modificado la cuenta, la información ingresada es incorrecta.";
        }
        System.out.println("Modificando cuenta: " + idCuenta);
        cuenta = cuentas.get(idCuenta);
        switch (campoMod){
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
                return "El campo a modificar no existe.";
        }
        return "Cuenta modificada...";
    }

    public String protocoloCerrar(String data){
        String idCuenta = data.split("/")[2];
        String clave = data.split("/")[3];
        if(cuentas.containsKey(idCuenta) && cuentas.get(idCuenta).getClave().equals(clave)){
            cuentas.remove(idCuenta);
            return "Cuenta cerrada...";
        }
        return "No se ha cerrado la cuenta, la información ingresada es incorrecta.";
    }

    public void protocoloSalir(Socket socket) throws IOException {
        System.out.println("Cerrando conexión con el cliente: " + socket.getInetAddress().getHostAddress());
        toNetwork.println("Adios desde el servidor :D");
        socket.close();
    }

    public boolean consultarCuenta(String data){
        String cedula = data.split("/")[4];
        for (Cuenta cuenta : cuentas.values()){
            if (cuenta.getCedula().equals(cedula)){
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
            for (Map.Entry<String, Cuenta> entry : cuentas.entrySet()) {
                String id = entry.getKey();
                Cuenta cuenta = entry.getValue();
                pw.println(id + "/" + cuenta.getNombre() + "/" + cuenta.getApellido() + "/" + cuenta.getCedula() + "/" + cuenta.getMonto() + "/" + cuenta.getClave());

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

                Cuenta cuenta = new Cuenta();
               // cuenta.setNroCuenta(id);
                cuenta.setNombre(nombre);
                cuenta.setApellido(apellido);
                cuenta.setCedula(cedula);
                cuenta.setMonto(monto);
                cuenta.setClave(clave);

                cuentas.put(id, cuenta);
            }
            br.close();
        } catch (IOException ex) {
            System.out.println("Error al leer las cuentas del archivo: " + ex.getMessage());
        }
    }
    public static void main(String args[]) throws Exception {
        EchoTCPServer es = new EchoTCPServer();
        es.init();
    }
}
