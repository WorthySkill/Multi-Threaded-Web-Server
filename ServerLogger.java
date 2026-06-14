import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class ServerLogger {
    // The 'synchronized' keyword acts as a mutex lock, preventing data corruption by forcing threads to write one at a time
    public static synchronized void logAccess(String message) {
        // 'true' appends text to the end of the file rather than overwriting it
        try (PrintWriter out = new PrintWriter(new FileWriter("access.log", true))) {
            out.println("[ACCESS] " + message);
        } catch (IOException e) {
            System.out.println("Could not write to access log.");
        }
    }

    // Synchronized lock for the error log
    public static synchronized void logError(String message) {
        try (PrintWriter out = new PrintWriter(new FileWriter("error.log", true))) {
            out.println("[ERROR] " + message);
        } catch (IOException e) {
            System.out.println("Could not write to error log.");
        }
    }
}
