package tp4;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Master {
    public Master() {}

    public long doRun(int totalCount, int numWorkers, String filename) throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();

        // Create a collection of tasks
        List<Callable<Long>> tasks = new ArrayList<>();
        for (int i = 0; i < numWorkers; ++i) {
            tasks.add(new Worker(totalCount));
        }

        // Run them and receive a collection of Futures
        ExecutorService exec = Executors.newFixedThreadPool(numWorkers);
        try {
            List<Future<Long>> results = exec.invokeAll(tasks);
            long total = 0;

            // Assemble the results
            for (Future<Long> f : results) {
                total += f.get();
            }

            // Calculate pi
            double pi = 4.0 * total / (totalCount * numWorkers);
            double error = Math.abs((pi - Math.PI)) / Math.PI;

            long stopTime = System.currentTimeMillis();

            // Print results
            System.out.println("\nValeur approchée: " + pi);
            System.out.println("Erreur: " + String.format("%e", error));
            System.out.println("N total: " + totalCount * numWorkers);
            System.out.println("Nombre de processus: " + numWorkers);
            System.out.println("Temps d'exécution: " + (stopTime - startTime) + "ms");

            // Write results to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                writer.write(String.format("%e %d %d %d", error, totalCount, numWorkers, (stopTime - startTime)));
                writer.newLine();
                System.out.println("Fichier écrit");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return total;
        } finally {
            exec.shutdown();
        }
    }
}