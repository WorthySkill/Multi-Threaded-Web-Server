import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

// The worker thread that handles the client request and returns to the pool when finished
public class ClientHandler implements Runnable {
    private Socket clientSocket; // The connection to the specific user
    private String docRoot;      // The directory containing the static HTML files

    // Constructor to initialize the worker with the client socket and document root
    public ClientHandler(Socket socket, String docRoot) {
        this.clientSocket = socket;
        this.docRoot = docRoot;
    }

    // Executed by the thread pool to handle the client's request
    @Override
    public void run() {
        try {
            // Set up I/O streams to read from and write to the client
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream out = clientSocket.getOutputStream();
            PrintWriter textOut = new PrintWriter(out, true);

            // Read the first line of the HTTP request
            String requestLine = in.readLine();
            if (requestLine == null) return; // Stop processing if the request is empty

            // Split the request line into its component parts
            String[] words = requestLine.split(" ");
            String method = words[0];
            String requestedFile = words[1];
            // NEW: URL Decoding
            // Translate internet codes (like %20) back into normal spaces
            // ---------------------------------------------------------
            requestedFile = java.net.URLDecoder.decode(requestedFile, "UTF-8");

            // Only allow GET requests as per project requirements
            if (!method.equals("GET")) {
                textOut.print("HTTP/1.1 500 Internal Server Error\r\n\r\n");
                textOut.flush(); // Flush the output to ensure the error message is sent before the socket closes
                ServerLogger.logError("Bad method used: " + method);
                return; // Stop working
            }

            // Security check: Path validation
            // Prevent path traversal attacks (e.g., requesting "../../etc/passwd")
            if (requestedFile.contains("..")) {
                textOut.print("HTTP/1.1 500 Internal Server Error\r\n\r\n");
                textOut.flush(); // Flush the output to ensure the error message is sent before the socket closes
                ServerLogger.logError("Hacker attempt detected: " + requestedFile);
                return; // Stop working
            }
            // ---------------------------------------------------------
            // NEW FEATURE: API Endpoint for the File Explorer
            // ---------------------------------------------------------
            if (requestedFile.equals("/api/get-files")) {
                textOut.print("HTTP/1.1 200 OK\r\n");
                textOut.print("Content-Type: text/plain\r\n\r\n"); // Tell the browser it's plain text
                File folder = new File(docRoot);
                File[] listOfFiles = folder.listFiles();
                if (listOfFiles != null) {
                    for (File file : listOfFiles) {
                        if (file.isFile()) {
                            // Print each file name on a new line that doesnt end with .html
                            if(file.isFile() && !file.getName().endsWith(".html")) {
                            textOut.println(file.getName());
                        }
                      }
                    }
                }
                textOut.flush();
                ServerLogger.logAccess("Served dynamic file API.");
                return; // Stop here so it doesn't try to look for a physical file named "/api/get-files"
            }
            // ---------------------------------------------------------

            // Default to index.html if the root directory is requested to prevent directory reading errors
            if (requestedFile.equals("/")) {
                requestedFile = "/index.html";
            }

            // Locate the requested file on the file system
            File fileToServe = new File(docRoot + requestedFile);

            // Check if the file exists and serve it
            // Check if the file exists and serve it
            if (fileToServe.exists()) {
                // Send the base HTTP success header
                textOut.print("HTTP/1.1 200 OK\r\n");

                // ---------------------------------------------------------
                // NEW: MIME Type Detection
                // Tell the browser exactly what kind of file it is receiving
                // ---------------------------------------------------------
                if (requestedFile.endsWith(".html")) {
                    textOut.print("Content-Type: text/html\r\n");
                } else if (requestedFile.endsWith(".pdf")) {
                    textOut.print("Content-Type: application/pdf\r\n");
                } else if (requestedFile.endsWith(".png")) {
                    textOut.print("Content-Type: image/png\r\n");
                } else if (requestedFile.endsWith(".jpg") || requestedFile.endsWith(".jpeg")) {
                    textOut.print("Content-Type: image/jpeg\r\n");
                }
                  else if (requestedFile.endsWith(".mp4")) {
                    textOut.print("Content-Type: video/mp4\r\n");
                }  else {
                    // Default fallback: tells the browser to just download the file
                    textOut.print("Content-Type: application/octet-stream\r\n"); 
                }
                textOut.print("Content-Length: " + fileToServe.length() + "\r\n");

                // The crucial blank line that tells the browser the headers are finished!
                textOut.print("\r\n");
                textOut.flush(); // Flush the headers immediately

                // Send the actual file contents
                Files.copy(fileToServe.toPath(), out);
                // Log the successful access (thread-safe)
                ServerLogger.logAccess("Served " + requestedFile + " successfully.");
            } else {
                // FAIL (404 Not Found)
                textOut.print("HTTP/1.1 404 Not Found\r\n\r\n");
                textOut.println("<h1>Sorry, file not found!</h1>");
                ServerLogger.logError("File missing: " + requestedFile);
            }

        } catch (Exception e) {
            ServerLogger.logError("Server crashed while handling a client.");
        } finally {
            // Close the socket to free up system resources
            try { clientSocket.close(); } catch (Exception e) {}
        }
    }
}
