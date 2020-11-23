import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private static String username;
    private static Socket client;
    private static BufferedReader keyboard;
    private static BufferedReader in;
    public static PrintWriter out;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.client = clientSocket;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
    }

    @Override
    public void run() {

        try {
            keyboard = new BufferedReader(new InputStreamReader(System.in));

            //new Thread(new ServerOutputHandler(client, in)).start();

            /*
            *   Benutzername abfragen: der client wird so lange nachgefragt, bis er einen nicht-vergebenen Name
            *   oder "bye" eingibt.
            */
            boolean disconnect = false;

            while (!disconnect) {
                System.out.println("Dein Name");
                username = keyboard.readLine();
                if (username == null || username.equalsIgnoreCase("")) {
                    continue;
                } else {
                    System.out.println(username);
                    out.println("/login " + username);
                    String feedback = in.readLine();
                    System.out.println(feedback);
                    if (feedback != null && feedback.equalsIgnoreCase("welcome " + username)) {
                        break;
                    }
                    if(feedback.equals("See you.")){
                        disconnect = true;
                    }
                    System.out.println(disconnect);
                }
            }

            if(!disconnect) {
                //neue Thread für die Servernachrichten (genauere Erläuterung in ServerOutputHandler.java
                new Thread(new ServerOutputHandler(client, in)).start();

                //client schreibt Nachrichten, sendet diese an den Server durch OutputStream
                //int n = 0;
                while (true) {
                    String chatMessage = keyboard.readLine();
                    out.println(chatMessage);

                    /*out.println(username + ": " + "Test Message " + n);
                    n++;
                    Thread.sleep(5000);*/
                }
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                System.out.println("logout erfolgreich...");
                client.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.close();
        }

    }
}
