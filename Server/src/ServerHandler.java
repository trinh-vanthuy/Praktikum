import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
public class ServerHandler implements Runnable{
    private Socket clientSocket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static final Map<String, PrintWriter> allClients = new HashMap<>();
    public ServerHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        //chatUsers
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }
    @Override
    public void run() {
        try {
            while (true) {
                String message = in.readLine();
                MessageType messageType = getMessageType(message);
                //continue ???
                switch (messageType){
                    case EMPTY:
                        continue;
                    case LOGIN:
                        if (!isDuplicate(message)) {
                            String yourUsername = getUsernameFromMessage(message);
                            sendMessageToAll(yourUsername + " joined the room");
                            synchronized (allClients) {
                                allClients.put(yourUsername, out);
                            }
                            sendMessage("welcome " + yourUsername, out);
                            break;
                        } else if (messageType.equals(MessageType.LOGOUT)){
                            out.println("See you.");
                            break;
                        } else {
                            //sendMessage("\"" + getUsernameFromMessage(message) + "\"" + " ist vergeben. Neuer Versuch...", out);
                            out.println("\"" + getUsernameFromMessage(message) + "\"" + " ist vergeben. Neuer Versuch...");
                            continue;
                        }
                    case LOGOUT:
                        sendMessage("See you.", out);
                        break;
                    case PRIVATE:
                        String helpMessage = message.substring(3);
                        String[] helpArray = helpMessage.split(" ");
                        String privateUser = helpArray[0];
                        String realMessage = null;
                        for(int i=1; i<helpArray.length; i++){
                            realMessage = realMessage + " " + helpArray[i];
                        }
                        out = allClients.get(privateUser);
                        sendMessage(realMessage, out);
                    case CREATEGAME:
                        //TODO
                    case JOIN:
                        //TODO
                    case STARTGAME:
                        //TODO
                    case SPIELZUG:
                        //TODO
                    case MESSAGE:
                        sendMessageToAll(message);
                }
                /*if (messageType.equals(MessageType.EMPTY)) {
                    continue;
                } else if (messageType.equals(MessageType.LOGIN)) {
                    if (!isDuplicate(message)) {
                        String yourUsername = getUsernameFromMessage(message);
                        sendMessageToAll(yourUsername + " joined the room");
                        synchronized (allClients) {
                            allClients.put(yourUsername, out);
                        }
                        sendPrivateMessage("welcome " + yourUsername, out);
                    } else if (messageType.equals(MessageType.LOGOUT)){
                        sendPrivateMessage("See you.", out);

                    } else {
                        sendPrivateMessage("\"" + getUsernameFromMessage(message) + "\"" + " ist vergeben. Neuer Versuch...", out);
                    }
                } else if (messageType.equals(MessageType.LOGOUT)) {
                    sendPrivateMessage("See you.", out);
                    String currentUser = "Anonym";
                    synchronized (allClients) {
                        for (Map.Entry<String, PrintWriter> entry : allClients.entrySet()) {
                            if (entry.getValue().equals(out)) {
                                currentUser = entry.getKey();
                                break;
                            }
                        }
                    }
                    //TODO: find currenUser
                    System.out.println(currentUser);
                    synchronized (allClients){
                        allClients.remove(currentUser);
                    }
                    sendMessageToAll(currentUser + " left the room...");
                    break;
                }else if(messageType.equals(MessageType.PRIVATE)){
                    String helpMessage = message.substring(3);
                    String[] helpArray = helpMessage.split(" ");
                    String privateUser = helpArray[0];
                    String realMessage = null;
                    for(int i=1; i<helpArray.length; i++){
                        realMessage = realMessage + " " + helpArray[i];
                    }
                    out = allClients.get(privateUser);
                    sendPrivateMessage(realMessage, out);
                } else {
//                        String currentUser = "Anonym";
//                        synchronized (allClients) {
//                            for (Map.Entry<String, PrintWriter> entry : allClients.entrySet()) {
//                                if (entry.getValue().equals(out)) {
//                                    currentUser = entry.getKey();
//                                    break;
//                                }
//                            }
//                        }
                    sendMessageToAll(*//*currentUser + ": " + *//*message);
                }*/
            }
        } catch (IOException e) {
            //System.out.println("Listening error:" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static MessageType getMessageType (String message) {
        if (message == null) {
            return MessageType.EMPTY;
        }else if (message.startsWith("/login ")){
            if(getUsernameFromMessage(message).equalsIgnoreCase("bye")){
                return MessageType.LOGOUT;
            }
            return MessageType.LOGIN;
        } else if (message.equalsIgnoreCase("bye")) {
            return MessageType.LOGOUT;
        } else if(message.startsWith("/dm")){
            return MessageType.PRIVATE;
        } else{
            return  MessageType.MESSAGE;
        }
    }
    private synchronized boolean isDuplicate(String username) {
        return allClients.containsKey(getUsernameFromMessage(username));
    }
    private static void sendMessage(String message, PrintWriter writer){
/*        String currentUser = null;
        for (Map.Entry<String, PrintWriter> entry : allClients.entrySet()) {
            if (entry.getValue().equals(writer)) {
                currentUser = entry.getKey();
                System.out.println("send to " + currentUser+ " :" + message);
                break;
            }
        }*/
        writer.println(/*currentUser + ": " + */message);
    }
    private static synchronized void sendMessageToAll(String message){
        if (allClients.size() <= 0){
            return;
        }
        for (PrintWriter chatUser: allClients.values()){
            sendMessage(message, chatUser);
        }
        /*
        allClients.values().forEach(writer -> {
            try {
                sendPrivateMessage(message, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });*/
    }
    private static String getUsernameFromMessage(String loginMessage) {
        return loginMessage.substring(7);
    }
}