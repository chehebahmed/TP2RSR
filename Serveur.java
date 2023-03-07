import java.io.*;
import java.net.*;
import java.util.HashMap;

public class Serveur {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        HashMap<String, String> clients = new HashMap<>(); // hashmap to store client info

        try {
            serverSocket = new ServerSocket(1286); // create server socket
            System.out.println("Server started.");

            while (true) {
                Socket clientSocket = serverSocket.accept(); // accept client connection
                System.out.println("Client connected from " + clientSocket.getInetAddress().getHostName());

                // create new thread for client connection
                Thread thread = new Thread(new Utilisateur(clientSocket, clients));
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port 1286.");
            System.exit(-1);
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}

class Utilisateur implements Runnable {
    private Socket clientSocket;
    private HashMap<String, String> clients;

    public Utilisateur(Socket socket, HashMap<String, String> clients) {
        this.clientSocket = socket;
        this.clients = clients;
    }

    public void run() {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // ask for login credentials
            out.println("Enter your ID:");
            String id = in.readLine();
            out.println("Enter your password:");
            String pass = in.readLine();

            // check if client exists in hashmap
            if (!clients.containsKey(id)) {
                clients.put(id, pass);
                out.println("New client added.");
            } else {
                // check if password is correct
                if (!pass.equals(clients.get(id))) {
                    out.println("Incorrect password.");
                    clientSocket.close();
                    return;
                }
            }
           
            // receive matrix from client
            

            out.println("Enter your first matrix (each element separated by a space, each row separated by a semicolon):");
            String matrix1String = in.readLine();
            out.println("Enter your second matrix (each element separated by a space, each row separated by a semicolon):");
            String matrix2String = in.readLine();

            // parse matrices
            String[] rows1 = matrix1String.split(";");
            String[] rows2 = matrix2String.split(";");
            int[][] matrix1 = new int[rows1.length][rows1.length];
            int[][] matrix2 = new int[rows2.length][rows2.length];
            int[][] result = new int[rows1.length][rows1.length];

            for (int i = 0; i < rows1.length; i++) {
                String[] elements1 = rows1[i].split(" ");
                String[] elements2 = rows2[i].split(" ");
                for (int j = 0; j < elements1.length; j++) {
                    matrix1[i][j] = Integer.parseInt(elements1[j]);
                    matrix2[i][j] = Integer.parseInt(elements2[j]);
                }
            }
         
            // calculate result matrix
            for (int i = 0; i < rows1.length; i++) {
                for (int j = 0; j < rows1.length; j++) {
                    result[i][j] = matrix1[i][j] + matrix2[i][j];
                }
            }
            // send result matrix to client
            out.println("Result matrix:");
            for (int i = 0; i < rows1.length; i++) {
                for (int j = 0; j < rows1.length; j++) {
                    out.print(result[i][j] + " ");
                }
                out.println();
            }
           
            // close connection
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}    