package entrega_final.cliente;

import java.net.Socket;
import static entrega_final.cliente.EchoTCPClient.SCANNER;

public class ClientMenu {
    public static void menu(Socket socket) throws Exception {
        boolean exit = false;
        while (!exit) {
            System.out.println("\u001B[32m\t╔═══════════════════════════════════╗");
                      System.out.println("\t║                 MENÚ              ║");
                      System.out.println("\t╠══════╦════════════════════════════╣");
                      System.out.println("\t║   1  ║ Operaciones basicas        ║");
                      System.out.println("\t║   2  ║ Movimientos transacionales ║");
                      System.out.println("\t║   3  ║ Salir                      ║");
                      System.out.println("\t╚══════╩════════════════════════════╝");
            int opc = Integer.parseInt(SCANNER.nextLine());
            System.out.flush();
            switch (opc) {
                case 1:
                    operacionesBasicas();
                    break;

                case 2:
                    movimientosTransaccionales();
                    break;

                case 3:
                    EchoTCPClient.salirCuenta(socket);
                    exit = true;
                    break;

                default:
                    System.out.println("Opción incorrecta...");
            }
        }
    }
//confi
    public static void operacionesBasicas() throws Exception {
        boolean exit = false;
        while (!exit) {
            System.out.println("\t╔════════════════════════════════╗");
            System.out.println("\t║              OPCIONES          ║");
            System.out.println("\t║══════╦═════════════════════════║");
            System.out.println("\t║  1   ║ Consultar ID cuenta     ║");
            System.out.println("\t║  2   ║ Abrir nueva cuenta      ║");
            System.out.println("\t║  3   ║ Modificar datos cuenta  ║");
            System.out.println("\t║  4   ║ Cancelar cuenta         ║");
            System.out.println("\t║  5   ║ Regresar                ║");
            System.out.println("\t╚══════╩═════════════════════════╝");
            int opc = Integer.parseInt(SCANNER.nextLine());
            System.out.flush();
            switch (opc) {
                case 1:
                    ClientHandlerIterationOne.consutarIdCuenta();
                    break;

                case 2:
                    ClientHandlerIterationOne.aperturaCuenta();
                    break;

                case 3:
                    ClientHandlerIterationOne.modificacionInfoCuenta();
                    break;

                case 4:
                    ClientHandlerIterationOne.cerrarCuenta();
                    break;

                case 5:
                    exit = true;
                    break;

                default:
                    System.out.println("Opción incorrecta...");
            }
        }
    }

    public static void movimientosTransaccionales() throws Exception {
        boolean exit = false;
        while (!exit) {
            System.out.println("\t╔════════════════════════════════╗");
            System.out.println("\t║              OPCIONES          ║");
            System.out.println("\t╠══════╦═════════════════════════╣");
            System.out.println("\t║   1  ║ Consignación bamcaria   ║");
            System.out.println("\t║   2  ║ Transferencia bamcaria  ║");
            System.out.println("\t║   3  ║ Retiro dinero           ║");
            System.out.println("\t║   4  ║ Regresar                ║");
            System.out.println("\t╚══════╩═════════════════════════╝");
            int opc = Integer.parseInt(SCANNER.nextLine());
            System.out.flush();
            switch (opc) {
                case 1:
                    ClientHandlerIterationTwo.consignacionDinero();
                    break;

                case 2:
                    ClientHandlerIterationTwo.transferenciaBancaria();
                    break;

                case 3:
                    ClientHandlerIterationTwo.retiroDinero();
                    break;

                case 4:
                    exit = true;
                    break;

                default:
                    System.out.println("Opción incorrecta...");
            }
        }
    }
}
