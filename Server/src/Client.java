import java.io.IOException;
import java.net.Socket;

public class Client {
    private static final String IP = "localhost";
    private static final int PORT = 9090;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(IP, PORT);

        ClientHandler clienthandler = new ClientHandler(socket);

        new Thread(clienthandler).start();
    }
}
