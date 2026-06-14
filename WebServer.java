import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.File;

public class WebServer {
    // Default settings
    static int port = 8080;
    static int threads = 10;
    static String docRoot = "./www";

    public static void main(String[] args) throws Exception {
        // Read command-line arguments to override default settings (e.g., -p 8080 -t 20)
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-p")) port = Integer.parseInt(args[i+1]);
            if (args[i].equals("-t")) threads = Integer.parseInt(args[i+1]);
            if (args[i].equals("-d")) docRoot = args[i+1];
        }

        // Check if the document root folder exists, and create it if it does not
        File folder = new File(docRoot);
        if (!folder.exists()) {
            folder.mkdir();
        }

        // THREAD POOL
        // Create a fixed thread pool of worker threads that wait for incoming client connections
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        // SOCKET LISTENER
        // Bind the ServerSocket to the specified port to listen for incoming network traffic
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Boss is listening on port " + port + " with " + threads + " workers.");

        // Infinite dispatcher loop: keeps the server running continuously to accept new connections
        while (true) {
            // Block and wait for a client connection. This operates efficiently without consuming CPU cycles while idle
            Socket client = serverSocket.accept();
            // Dispatch the new client connection to a worker thread in the pool and immediately return to listening
            pool.execute(new ClientHandler(client, docRoot));
        }
    }
}
