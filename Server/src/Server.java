import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 9090;
    private static final String HOST = "localhost";

    public static void main (String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(PORT);
        System.out.println("Server running");

        ExecutorService clientPool = Executors.newFixedThreadPool(100);

        while (true){
            Socket client = listener.accept();
            synchronized (clientPool){
                clientPool.execute(new ServerHandler(client));
            }

            //new ServerHandler(client).run();
           // BufferedReader buff = new BufferedReader(new InputStreamReader(client.getInputStream()));
            //String message = buff.readLine();
            //System.out.println(message);
        }
    }

}
