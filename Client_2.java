import java.io.*;
import java.net.*;

public class Client_2 {
    public static void main(String[] args) throws IOException {
        Socket socket = null;

        try {
            socket = new Socket("localhost", 1286); // connect to server
            System.out.println("Connected to server.");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // ask for login credentials
            System.out.println(in.readLine());
            BufferedReader idReader = new BufferedReader(new InputStreamReader(System.in));
            String id = idReader.readLine();
            out.println(id);

            System.out.println(in.readLine());
            BufferedReader passReader = new BufferedReader(new InputStreamReader(System.in));
            String pass = passReader.readLine();
            out.println(pass);

            // receive matrix input from user
            

            String matrix1String = null;
            while (matrix1String == null) {
                System.out.println(in.readLine());
                BufferedReader matrix1Reader = new BufferedReader(new InputStreamReader(System.in));
                matrix1String = matrix1Reader.readLine();
                if (!isValidMatrixString(matrix1String)) {
                    matrix1String = null;
                    System.out.println("Invalid input. Please enter a valid matrix string.");
                } else {
                    out.println(matrix1String);
                }
            }

            String matrix2String = null;
            while (matrix2String == null) {
                System.out.println(in.readLine());
                BufferedReader matrix2Reader = new BufferedReader(new InputStreamReader(System.in));
                matrix2String = matrix2Reader.readLine();
                if (!isValidMatrixString(matrix2String)) {
                    matrix2String = null;
                    System.out.println("Invalid input. Please enter a valid matrix string.");
                } else {
                    out.println(matrix2String);
                }
            }

            System.out.println(in.readLine());
            String resultString;
            while ((resultString = in.readLine()) != null) {
                System.out.println(resultString);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    private static boolean isValidMatrixString(String matrixString) {
        if (matrixString == null || matrixString.isEmpty()) {
            return false;
        }
        String[] rows = matrixString.split(";");
        if (rows.length == 0) {
            return false;
        }
        int numCols = -1;
        for (String row : rows) {
            String[] elements = row.trim().split(" ");
            if (numCols == -1) {
                numCols = elements.length;
            } else if (elements.length != numCols) {
                return false;
            }
            for (String element : elements) {
                try {
                    Integer.parseInt(element);
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        return true;
    }
}
