package d_echotcpmulticonexion2;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoTCPClient {
    public static final String SERVER = "localhost";
    public static final int PORT = 3400;

    private Socket clientSideSocket;
    private PrintWriter toNetwork;
    private BufferedReader fromNetwork;

    public EchoTCPClient() {
        System.out.println("Echo TCP client is running ...");
    }

    public void init() throws Exception {
        File file = new File("datos.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String message = reader.readLine();

        while (message != null) {
            System.out.println("Sending message: " + message);

            clientSideSocket = new Socket(SERVER, PORT);
            createStreams(clientSideSocket);

            protocol(message);

            clientSideSocket.close();
            message = reader.readLine();
        }

        reader.close();
    }

    private void createStreams(Socket socket) throws Exception {
        toNetwork = new PrintWriter(socket.getOutputStream(), true);
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void protocol(String message) throws Exception {
        toNetwork.println(message);
        String response = fromNetwork.readLine();
        System.out.println("Response from server: " + response);
    }

    public static void main(String[] args) throws Exception {
        EchoTCPClient client = new EchoTCPClient();
        client.init();
    }
}