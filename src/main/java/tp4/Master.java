package tp4;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Creates workers to run the Monte Carlo simulation
 * and aggregates the results.
 */
public class Master {
    public Master(){}

    public long doRun(int totalCount, int numWorkers, String filename) throws InterruptedException, ExecutionException {
        boolean asFilename = !filename.isEmpty();

        long startTime = System.currentTimeMillis();

        // Create a collection of tasks
        List<Callable<Long>> tasks = new ArrayList<Callable<Long>>();
        for (int i = 0; i < numWorkers; ++i) {
            tasks.add(new Worker(totalCount));
        }

        // Run them and receive a collection of Futures
        ExecutorService exec = Executors.newFixedThreadPool(numWorkers);
        List<Future<Long>> results = exec.invokeAll(tasks);
        long total = 0;

        // Assemble the results.
        for (Future<Long> f : results) {
            // Call to get() is an implicit barrier.  This will block
            // until result from corresponding worker is ready.
            total += f.get();
        }
        double pi = 4.0 * total / totalCount / numWorkers;

        long stopTime = System.currentTimeMillis();

        System.out.println("\nValeur approché: " + pi);
        System.out.println("Erreur: " + String.format("%e", (Math.abs((pi - Math.PI)) / Math.PI)));

        System.out.println("N total: " + totalCount * numWorkers);
        System.out.println("Nombre process: " + numWorkers);
        System.out.println("Temps d'execution: " + (stopTime - startTime) + "ms");

        if (asFilename) {
            try {
                // Code tiré d'openclassroom
                // Création d'un fileWriter pour écrire dans un fichier
                FileWriter fileWriter = new FileWriter(filename, true);

                // Création d'un bufferedWriter qui utilise le fileWriter
                BufferedWriter writer = new BufferedWriter(fileWriter);

                // ajout d'un texte à notre fichier
                writer.write(String.format("%e", (Math.abs((pi - Math.PI)) / Math.PI)) + " " + (totalCount * numWorkers) + " " + numWorkers + " " + (stopTime - startTime));

                // Retour à la ligne
                writer.newLine();
                writer.close();
                System.out.println("Fichier ecrit");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        exec.shutdown();
        return total;
    }
}
