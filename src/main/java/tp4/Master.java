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
class Master {
    public long doRun(int totalCount, int numWorkers, long t1) throws InterruptedException, ExecutionException, IOException {

        long startTime = System.currentTimeMillis();

        // Collection de tâches
        List<Callable<Long>> tasks = new ArrayList<>();
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
        long duration = stopTime - startTime;

        double sp = (double) t1 / duration;

        System.out.println("\nPi : " + pi);
        System.out.println("Error: " + (Math.abs((pi - Math.PI)) / Math.PI) + "\n");

        System.out.println("Ntot: " + totalCount * numWorkers);
        System.out.println("Available processors: " + numWorkers);
        System.out.println("Time Duration (ms): " + duration + "\n");

        System.out.println((Math.abs((pi - Math.PI)) / Math.PI) + " " + totalCount * numWorkers + " " + numWorkers + " " + duration);

        // Write results to file
        writeResultsToFile(totalCount, numWorkers, pi, duration, sp);

        exec.shutdown();
        return total;
    }

    private void writeResultsToFile(int totalCount, int numWorkers, double pi, long duration, double sp) throws IOException {
        String filePath = "src/main/java/tp4/results.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(totalCount * numWorkers + "," + numWorkers + "," + pi + "," + duration + "ms," + sp + "\n");
        }
    }
}