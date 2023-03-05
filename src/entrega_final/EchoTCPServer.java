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

    private String fileName = "datos.txt";

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
        String respuestaServidor = "";
        switch (opc) {
            case "CUENTA":
                respuestaServidor = protocoloCuenta(data);
                break;

            case "MOVI":
                respuestaServidor = protocoloMovimiento(data);
                break;

            case "SALIR":
                protocoloSalir(socket);
                return;
        }
        System.out.println("Server response: " + respuestaServidor);
        System.out.println(cuentas);
        toNetwork.println(respuestaServidor);
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


    private String protocoloConsignacion(String data) {
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


    private String protocoloRetiro(String data) {
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

    public String protocoloCuenta(String data) {
        String opc = data.split("/")[1];
        System.out.println("Opc: " + opc);

        try {
            switch (opc) {
                case "CONSULTAR":
                    return protocoloConsultarId(data);

                case "ABRIR":
                    return protocoloAbrir(data);

                case "MODIFICAR":
                    return protocoloMod(data);

                case "CERRAR":
                    return protocoloCerrar(data);

                default:
                    return "Opción no válida";
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }


    public String protocoloConsultarId(String data) {
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

    public String protocoloAbrir(String data) {
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


    // Definimos una función para modificar una cuenta según los datos recibidos en la variable `data`.
    public String protocoloMod(String data) {

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
        System.out.println("Modificando cuenta: " + idCuenta);
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
                return "El campo a modificar no existe.";
        }

        return "Cuenta modificada...";
    }


    public String protocoloCerrar(String data) {
        String idCuenta = data.split("/")[2];
        String clave = data.split("/")[3];
        if (cuentas.containsKey(idCuenta) && cuentas.get(idCuenta).getClave().equals(clave)) {
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
