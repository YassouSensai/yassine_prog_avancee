package tp4_socket;

import java.io.*;
import java.net.*;
import java.util.Random;

import tp4.*;


public class WorkerSocket {
    static int port = 25545; // default port
    private static boolean isRunning = true;

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && !args[0].isEmpty()) port = Integer.parseInt(args[0]);
        System.out.println(port);
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);
        Socket socket = serverSocket.accept();

        BufferedReader bRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pWrite = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

        String str;
        while (isRunning) {
            str = bRead.readLine(); // read message from Master
            if (!"END".equals(str)) {
                System.out.println("Server receives totalCount = " + str);
                int totalCount = Integer.parseInt(str);

                // Compute Monte Carlo
                int insideCircle = computeMonteCarlo(totalCount);

                // Send result to Master
                pWrite.println(insideCircle);
            } else {
                isRunning = false;
            }
        }
        bRead.close();
        pWrite.close();
        socket.close();
        serverSocket.close();
    }

    // Code que j'ai ajouté
    private static int computeMonteCarlo(int totalCount) {
        Random random = new Random();
        int insideCircle = 0;
        for (int i = 0; i < totalCount; i++) {
            double x = random.nextDouble();
            double y = random.nextDouble();
            if (x * x + y * y <= 1) {
                insideCircle++;
            }
        }
        return insideCircle;
    }
}