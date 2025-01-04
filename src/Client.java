import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final int PORT = 1234;

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket("localhost", PORT);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            Scanner scanner = new Scanner(System.in);
            for (; ; ) {

                //Writing to server...
                String userInput = scanner.nextLine();
                bufferedWriter.write(userInput);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                //Reading from server
                System.out.printf("Server response: %s%n", bufferedReader.readLine());

                if (userInput.equalsIgnoreCase("quit")) {
                    break;
                }
            }
        }
    }
}


