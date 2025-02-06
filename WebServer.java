//Saikiran Reddy Yarava
//999903621
import java.io.*;
import java.net.*;

public class WebServer {
    public static void main(String[] args) {
        new Thread(WebServer::serverThread).start();
    }
    
    private static void serverThread() {
        int serverPort = 8080;
        try {
            // passing the serverport to socket object
            ServerSocket serverSocket = new ServerSocket(serverPort);
            // Prompting the user when server is ready to receive requests
            System.out.println("Server is ready to receive");

            while (true) {
                Socket connectionSocket = serverSocket.accept();
                System.out.println("Connection from " + connectionSocket.getInetAddress());
                
                // starttime is stored when the server receives the request
                long startTime = System.currentTimeMillis();

                // Create a new thread to handle the request
                new Thread(() -> handleRequest(connectionSocket, startTime)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest(Socket connectionSocket, long startTime) {
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            // Read the HTTP request message
            String requestLine = inFromClient.readLine();

            // Check if the request is null or empty
            if (requestLine == null || requestLine.isEmpty()) {
                // Handle the case where the request is invalid
                System.out.println("Request is invalid as no request is being made...!");
                return;
            }

            System.out.println("Request Line: " + requestLine);

            String[] requestTokens = requestLine.split("\\s");

            // Check if requestTokens has the expected length
            if (requestTokens.length < 2) {
                // Handle the case where the request is incomplete or malformed
                System.out.println("Invalid request format");
                return;
            }

            String filename = requestTokens[1];
            System.out.println("Filename: " + filename);

            try (FileInputStream fileInputStream = new FileInputStream(filename.substring(1))) {
                // Send HTTP response headers
                outToClient.writeBytes("HTTP/1.1 200 OK\r\n\r\n");

                // Send the file content
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outToClient.write(buffer, 0, bytesRead);
                }
                
                // endtime is stored after the server sends the complete response
                long endTime = System.currentTimeMillis();
                long rtt = endTime - startTime;
                System.out.println("Round Trip Time (RTT): " + rtt + " ms");
                
                
            } catch (IOException e) {
                // File not found, send 404 response
                outToClient.writeBytes("HTTP/1.1 404 Not Found\r\n\r\n");
                outToClient.writeBytes("<html><head></head><body><h1>404 Not Found</h1></body></html>\r\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // closing socket connection
                connectionSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
