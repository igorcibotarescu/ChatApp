import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int PORT = 1234;

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            for (; ; ) {
                //Wait for client connections, server keeps running even if client disconnects
                try (Socket socket = serverSocket.accept();
                     BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                    for (; ; ) {
                        //Loop to talk with each client
                        String clientMessage = bufferedReader.readLine();
                        System.out.printf("Client: %s%n", clientMessage);

                        bufferedWriter.write("Server response: " + new StringBuilder(clientMessage).reverse());
                        bufferedWriter.newLine();
                        bufferedWriter.flush();

                        if (clientMessage.equalsIgnoreCase("quit")) {
                            break;
                        }
                    }
                }
            }
        }
    }
}
