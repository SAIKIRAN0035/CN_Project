//Saikiran Reddy Yarava
//999903621
import java.io.*;
import java.net.*;

public class WebClient {
    public static void main(String[] args) {
        // server address as localhost
        String serverAddress = "127.0.0.1";
        // standard server port 8080
        int serverPort = 8080;
        String filename = "index.html";

        client(serverAddress, serverPort, filename);
    }

    private static void client(String serverAddress, int serverPort, String filename) {
        try {
            // passing the serverport to socket object
            Socket clientSocket = new Socket(serverAddress, serverPort);
            PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Send a simple HTTP GET request
            String request = "GET /" + filename + " HTTP/1.1\r\nHost: " + serverAddress + "\r\n\r\n";
            outToServer.println(request);

            // Receive and print the response
            String responseLine;
            String statusCode = ""; // Variable to store the status code

        while ((responseLine = inFromServer.readLine()) != null && !responseLine.isEmpty()) {
            System.out.println(responseLine);

            // Extract and display the status code
            if (responseLine.startsWith("HTTP/1.1")) {
                String[] statusLineParts = responseLine.split("\\s");
                if (statusLineParts.length > 1) {
                    statusCode = statusLineParts[1];
                    System.out.println("Status Code: " + statusCode);
                }
            }
        }

        // Continue reading and printing the rest of the response
        while ((responseLine = inFromServer.readLine()) != null) {
            System.out.println(responseLine);
        }

        // Now you can use the 'statusCode' variable as needed.

        // Closing socket connection
        clientSocket.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
}
}