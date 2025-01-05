import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private final int port;
    private static final Scanner SCANNER = new Scanner(System.in);

    public Client(final int port) {
        this.port = port;
    }

    public void run() throws IOException {
        try (Socket socket = new Socket("localhost", port);
             ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            executorService.submit(new SocketWriter(socket));
            executorService.submit(new SocketReader(socket));
        }
    }

    private record SocketWriter(Socket socket) implements Runnable {

        @Override
        public void run() {
            System.out.print("Enter your username: ");
            try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
                while (true) {
                    String userInput = SCANNER.nextLine();
                    Utilities.sendMessage(bufferedWriter, userInput);
                    if (userInput.equalsIgnoreCase("quit")) {
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private record SocketReader(Socket socket) implements Runnable {

        @Override
        public void run() {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                while (true) {
                    System.out.println(bufferedReader.readLine());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


