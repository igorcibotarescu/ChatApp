import java.io.BufferedWriter;
import java.io.IOException;


public class Utilities {
    public static void sendMessage(BufferedWriter bufferedWriter, String msg) throws IOException {
        bufferedWriter.write(msg);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }
}
