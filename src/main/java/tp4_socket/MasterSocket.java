package tp4_socket;

import java.io.*;
import java.net.*;

/**
 * Master is a client. It makes requests to numWorkers.
 */
public class MasterSocket {
	static int maxServer = 12; // 8
	static final int[] tab_port = {25545, 25546, 25547, 25548, 25549, 25550, 25551, 25552, 25553, 25554, 25555, 25556};
	static final int initial_port = 25545;
	static String[] tab_total_workers = new String[maxServer];
	static final String[] ip = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1","127.0.0.1","127.0.0.1","127.0.0.1","127.0.0.1","127.0.0.1","127.0.0.1","127.0.0.1"};
	static BufferedReader[] reader = new BufferedReader[maxServer];
	static PrintWriter[] writer = new PrintWriter[maxServer];
	static Socket[] sockets = new Socket[maxServer];


	public static void main(String[] args) throws Exception {

		// MC parameters
		long totalCount = 64000000000L; // Indique que c'est de type long 'L'
		long total = 0;
		double pi;

		String filename = "./results_fort.txt";

		int numWorkers = maxServer;
		int thread_by_worker = 1;
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		String s;

		System.out.println("#########################################");
		System.out.println("# Computation of PI by MC method        #");
		System.out.println("#########################################");

		System.out.println("\n How many workers for computing PI (< maxServer): ");
		try {
			s = bufferRead.readLine();
			numWorkers = Integer.parseInt(s);
			System.out.println(numWorkers);
		} catch (IOException ioE) {
			ioE.printStackTrace();
		}

		System.out.println("\n How many threads by worker : ");
		try {
			s = bufferRead.readLine();
			thread_by_worker = Integer.parseInt(s);
			System.out.println(thread_by_worker);
		} catch (IOException ioE) {
			ioE.printStackTrace();
		}


		// Bloc pour créer un worker socket
		for (int i = 0; i < numWorkers; i++) {
			try {
				System.out.println("PORT : " + (initial_port + i));
				sockets[i] = new Socket(ip[i], initial_port + i);
				System.out.println("SOCKET = " + sockets[i]);

				reader[i] = new BufferedReader(new InputStreamReader(sockets[i].getInputStream()));
				writer[i] = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sockets[i].getOutputStream())), true);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		String total_count_to_send, thread_by_worker_to_send;
		total_count_to_send = String.valueOf(totalCount/numWorkers);
		thread_by_worker_to_send = String.valueOf(thread_by_worker);


		String message_repeat = "y";

		long stopTime, startTime;

		for (int y = 0; y < 10; y++) {

			total = 0;

			startTime = System.currentTimeMillis();
			// Initialise les worker avec le total_count
			for (int i = 0; i < numWorkers; i++) {
				writer[i].println(total_count_to_send);
			}

			// Donnne le nombre de thread par worker
			for (int i = 0; i < numWorkers; i++) {
				writer[i].println(thread_by_worker_to_send);
			}

			// Reccupere les informations des workers
			for (int i = 0; i < numWorkers; i++) {
				tab_total_workers[i] = reader[i].readLine();
				System.out.println("Client sent: " + tab_total_workers[i]);
			}

			// compute PI with the result of each workers
			for (int i = 0; i < numWorkers; i++) {
				total += Long.parseLong(tab_total_workers[i]);
			}
			pi = 4.0 * total / totalCount ;

			stopTime = System.currentTimeMillis();

			System.out.println("\nPi : " + pi);
			System.out.println("Error: " + (Math.abs((pi - Math.PI)) / Math.PI) + "\n");

			System.out.println("Ntot: " + totalCount );
			System.out.println("Available processors: " + (numWorkers * thread_by_worker));
			System.out.println("Time Duration (ms): " + (stopTime - startTime) + "\n");

			System.out.println((Math.abs((pi - Math.PI)) / Math.PI) + " " + (totalCount ) + " " + (numWorkers * thread_by_worker) + " " + (stopTime - startTime));

			writeFile(filename, pi, totalCount, numWorkers, thread_by_worker, startTime, stopTime);

		}

		for (int i = 0; i < numWorkers; i++) {
			System.out.println("END");
			writer[i].println("END");
			reader[i].close();
			writer[i].close();
			sockets[i].close();
		}
	}

	private static void writeFile(String filename, double pi, long totalCount, int numWorkers, int thread_by_worker, long startTime, long stopTime) throws IOException {
		try {
			FileWriter fileWriter = new FileWriter(filename, true);
			BufferedWriter writer = new BufferedWriter(fileWriter);

			writer.write(String.format("%e", (Math.abs((pi - Math.PI)) / Math.PI)) + " " + (totalCount ) + " " + (numWorkers * thread_by_worker) + " " + (stopTime - startTime));

			writer.newLine();
			writer.close();
			System.out.println("Fichier ecrit");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}