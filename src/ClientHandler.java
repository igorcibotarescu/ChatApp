import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {
    private static final List<ClientHandler> clients = new ArrayList<>();
    private final Socket socket;
    private String clientName;
    private final String clientIpAddress;
    private final int clientPort;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.clientIpAddress = socket.getInetAddress().getHostAddress();
        this.clientPort = socket.getPort();
    }

    @Override
    public void run() {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            this.clientName = bufferedReader.readLine();
            clients.add(this);
            System.out.println(this.clientName + "-> Joined the chat!");
            Utilities.sendMessage(bufferedWriter, "Successfully connected to chat!");
            broadcast(this.clientName + "-> Joined the chat, say hello!");

            for (; ; ) {

                String clientMessage = bufferedReader.readLine();
                System.out.printf("Client(%s:%d %s): %s%n", this.clientIpAddress, this.clientPort, this.clientName, clientMessage);

                if(clientMessage.equalsIgnoreCase("show")) {
                    Utilities.sendMessage(bufferedWriter, String.valueOf(clients.stream().
                            filter(client -> !client.clientName.equalsIgnoreCase(this.clientName)).map(c -> c.clientName).toList()));
                    continue;
                }

                if (clientMessage.equalsIgnoreCase("quit")) {
                    broadcast(this.clientName + " left the chat!");
                    clients.remove(this);
                    break;
                }
                broadcast(this.clientName + ": " + clientMessage);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void broadcast(final String msg){
        clients.stream().filter(client -> !client.clientName.equalsIgnoreCase(this.clientName)).map(c -> c.socket).forEach(s -> {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                Utilities.sendMessage(bufferedWriter, msg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
