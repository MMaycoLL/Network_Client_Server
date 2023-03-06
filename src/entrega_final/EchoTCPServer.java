package entrega_final;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class EchoTCPServer {
    public static final int PORT = 4200;
    private final String fileName = "datos.txt";
    private ServerSocket listener;
    private Socket serverSideSocket;

    public static PrintWriter toNetwork;
    private BufferedReader fromNetwork;
    private Map<String, Cuenta> cuentas;

    public EchoTCPServer() {
        System.out.println("Echo TCP server is running on port: " + PORT);
    }

    public static void main(String[] args) throws Exception {
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
        String respuestaServidor = "";
        switch (opc) {
            case "CUENTA":
                respuestaServidor = primeraIteracion(data);
                break;

            case "MOVIMIENTOS":
                respuestaServidor = segundaIteracion(data);
                break;

            case "SALIR":
                HandlerIteracion1.protocoloSalir(socket);
                return;
        }
        System.out.println("Server response: " + respuestaServidor);
        System.out.println(cuentas);
        toNetwork.println(respuestaServidor);
        writeCuentasToFile();
    }

    public String primeraIteracion(String data) {
        String opc = data.split("/")[1];
        System.out.println("Opc: " + opc);

        try {
            switch (opc) {
                case "CONSULTAR_ID":
                    return HandlerIteracion1.consultarIdCuenta(cuentas, data);

                case "ABRIR":
                    return aperturaCuenta(data);

                case "MODIFICAR":
                    return HandlerIteracion1.modificacionInfoCuenta(cuentas, data);

                case "CERRAR":
                    return HandlerIteracion1.cierreCuenta(cuentas, data);

                default:
                    return "Opción no válida";
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }


    public String segundaIteracion(String data) {
        String opcion = data.split("/")[1];
        System.out.println("Opcion: " + opcion);
        String resp = "";
        switch (opcion) {

            case "CONSIGNACION":
                resp = HandlerIteracion2.consignacionDinero(cuentas, data);
                break;

            case "TRANSFERERENCIA":
                resp = HandlerIteracion2.tranferenciaBamcaria(cuentas, data);
                break;

            case "RETIRO":
                resp = HandlerIteracion2.retiroDineroCuenta(cuentas, data);
                break;
        }
        return resp;
    }


    public String aperturaCuenta(String data) {
        try {
            // Verificar si la cuenta ya existe
            if (consultarCuenta(data)) {
                return "1"; // Se retorna "1" si la cuenta ya existe
            }

            // Crear la nueva cuenta y asignar los valores de los parámetros
            System.out.println("Abriendo cuenta...");
            Cuenta cuenta = new Cuenta();
            cuenta.setNombre(data.split("/")[2]);
            cuenta.setApellido(data.split("/")[3]);
            cuenta.setCedula(data.split("/")[4]);
            cuenta.setMonto(Double.parseDouble(data.split("/")[5]));
            cuenta.setClave(data.split("/")[6]);

            // Agregar la cuenta al mapa de cuentas
            cuentas.put(String.valueOf(cuenta.getNroCuenta()), cuenta);

            // Retornar un mensaje con la información de la cuenta creada
            return "Cuenta abierta..." + "/ID: " + cuenta.getNroCuenta() + "/Nombre: " + cuenta.getNombre() + "/Apellido: " + cuenta.getApellido() + "/Monto depositado: " + cuenta.getMonto() + "/Clave: " + cuenta.getClave();
        } catch (NumberFormatException ex) {
            // Capturar la excepción si el monto no es un número válido
            return "El monto solo debe contener valores numéricos...";
        }
    }





    public boolean consultarCuenta(String data) {
        String cedula = data.split("/")[4];
        for (Cuenta cuenta : cuentas.values()) {
            if (cuenta.getCedula().equals(cedula)) {
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
}
