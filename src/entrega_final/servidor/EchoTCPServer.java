package entrega_final.servidor;


import entrega_final.cliente.Account;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class EchoTCPServer {
    public static final int PORT = 1212;

    public static String fileName = "dataBase.txt";
    private static PrintWriter toNetwork;
    public Socket serverSideSocket;
    private ServerSocket listener;
    private BufferedReader fromNetwork;
    private AccountsFileManager accountsFileManager;

    public EchoTCPServer() {
        System.out.println("Echo TCP server is running on port: " + PORT);
        this.accountsFileManager = new AccountsFileManager();
    }

    public static void main(String args[]) throws Exception {
        EchoTCPServer es = new EchoTCPServer();
        es.init();
    }


    public void init() throws Exception {
        listener = new ServerSocket(PORT);
        ServerHandler.cuentas = new HashMap<>();
        accountsFileManager.readAccountsFromFile();
        while (true) {
            serverSideSocket = listener.accept();
            System.out.println("Cliente recibido desde: " + serverSideSocket.getInetAddress().getHostAddress());
            createStreams(serverSideSocket);
            while (!serverSideSocket.isClosed()) {
                menu(serverSideSocket);
            }
        }
    }

    private void createStreams(Socket socket) throws Exception {
        toNetwork = new PrintWriter(socket.getOutputStream(), true);
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void menu(Socket socket) throws Exception {
        String data = fromNetwork.readLine();
        String opc = data.split("/")[0];
        System.out.println("Opc: " + opc);
        System.out.println(data);
        String resp = "";
        switch (opc) {
            case "CUENTA":
                resp = operacionesBasicas(data);
                break;

            case "MOVIMIENTO":
                resp = movimientosTransaccionales(data);
                break;

            case "SALIR":
                EchoTCPServer.salirCuenta(socket);
                return;
        }
        System.out.println("Server response: " + resp);
        for (Map.Entry<String, Account> entry : ServerHandler.cuentas.entrySet()) {
            String idCuenta = entry.getKey();
            Account account = entry.getValue();
            System.out.println("ID de cuenta: " + idCuenta);
            System.out.println("Cédula: " + account.getCedula());
            System.out.println("Clave: " + account.getClave());
            System.out.println("Monto: " + account.getMonto());
            System.out.println();
        }
        toNetwork.println(resp);
        AccountsFileManager.writeAccountsToFile();
    }

    public String operacionesBasicas(String data) throws Exception {
        String opc = data.split("/")[1];
        System.out.println("Opc: " + opc);
        String resp = "";
        switch (opc) {
            case "CONSULTAR":
                resp = ServerHandler.consutarIdCuenta(data);
                break;

            case "ABRIR":
                resp = ServerHandler.aperturaCuenta(data);
                break;

            case "MODIFICAR":
                resp = ServerHandler.modificacionInfoCuenta(data);
                break;

            case "CERRAR":
                resp = ServerHandler.cerrarCuenta(data);
                break;
        }
        return resp;
    }

    public String movimientosTransaccionales(String data) {
        String opc = data.split("/")[1];
        System.out.println("Opc: " + opc);
        String resp = "";
        switch (opc) {
            case "RETIRO":
                resp = ServerHandler.retiroDinero(data);
                break;

            case "CONSIGNACION":
                resp = ServerHandler.consignacionDinero(data);
                break;

            case "TRANSFERENCIA":
                resp = ServerHandler.transferenciaBancaria(data);
                break;
        }
        return resp;
    }

    public static void salirCuenta(Socket socket) throws IOException {
        System.out.println("Cerrando conexión con el cliente: " + socket.getInetAddress().getHostAddress());
        toNetwork.println("Conexion cerrada por el servidor...........");
        socket.close();
    }



}
