import java.io.*;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static final int PORT = 1234;

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(PORT);
             ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            while (!serverSocket.isClosed()) {
                //Wait for client connections, server keeps running even if client disconnects
                ClientHandler clientHandler = new ClientHandler(serverSocket.accept());
                executorService.submit(clientHandler);
            }
        }
    }
}
