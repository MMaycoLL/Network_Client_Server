package b_echoudp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class EchoUDPServer {
    public static final int PORT = 3500;

    private DatagramSocket serverSideSocket;
    private InetAddress clientIPAddress;
    private int clientPort;

    public EchoUDPServer() throws Exception {
        System.out.println("Echo UDP server is running on port: " + PORT);
    }

    public void init() throws Exception {
        serverSideSocket = new DatagramSocket(PORT);

        while (true) {
            protocol();
        }
    }

    public void protocol() throws Exception {
        String message = (String) receive();
        System.out.println("[Server] From client: " + message);

        String answer = message;

        send(answer);
    }

    private void send(String messageToSend) throws Exception {
        byte[] bufferToSend = messageToSend.getBytes();
        DatagramPacket packetToSend = new DatagramPacket
                (bufferToSend, bufferToSend.length, clientIPAddress, clientPort);
        serverSideSocket.send(packetToSend);
    }

    private String receive() throws Exception {
        byte[] bufferToReceive = new byte [ 1024 ];
        DatagramPacket packetToReceive = new DatagramPacket(bufferToReceive, bufferToReceive.length);
        serverSideSocket.receive(packetToReceive);

        clientIPAddress = packetToReceive.getAddress ( );
        clientPort = packetToReceive.getPort ( );
        String receivedMessage = new String(packetToReceive.getData(), 0, packetToReceive.getLength());

        return receivedMessage;
    }

    public static void main(String args[]) throws Exception {
        EchoUDPServer es = new EchoUDPServer();
        es.init();
    }
}