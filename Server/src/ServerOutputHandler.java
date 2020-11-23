import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ServerOutputHandler implements Runnable {

    private BufferedReader in;
    private Socket client;

    public ServerOutputHandler(Socket client, BufferedReader in) {
        this.client = client;
        this.in = in;
    }

    @Override
    public void run() {
        String incomingMessage = null;
        //client liest Nachrichten aus der Inputsstream
        try {
            while (true) {
                if (in.ready()) {
                    incomingMessage = in.readLine();
                    System.out.println(incomingMessage);
                    if (incomingMessage.equalsIgnoreCase("See you.")) {
                        //System.out.println("logout erfolgreich...");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("logout erfolgreich...");
                client.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ClientHandler.out.close();
        }

    }
}
