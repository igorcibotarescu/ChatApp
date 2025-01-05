import java.io.IOException;

public class ClientRunner {
    private static final int PORT = 1234;

    public static void main(String[] args) throws IOException {
        new Client(PORT).run();
    }
}
