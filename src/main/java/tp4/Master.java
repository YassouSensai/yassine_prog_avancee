package tp4;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

class Master {
    public long doRun(int totalCount, int numWorkers) throws InterruptedException, ExecutionException, IOException {
        long t1 = measureSequentialTime(totalCount);

        long startTime = System.currentTimeMillis();

        List<Callable<Long>> tasks = new ArrayList<>();
        for (int i = 0; i < numWorkers; ++i) {
            tasks.add(new Worker(totalCount / numWorkers));
        }

        ExecutorService exec = Executors.newFixedThreadPool(numWorkers);
        List<Future<Long>> results = exec.invokeAll(tasks);
        long total = 0;

        for (Future<Long> f : results) {
            total += f.get();
        }
        double pi = 4.0 * total / totalCount;

        long stopTime = System.currentTimeMillis();
        long duration = stopTime - startTime;

        double sp = duration > 0 ? (double) t1 / duration : 0;

        System.out.println("\nPi : " + pi);
        System.out.println("Erreur : " + (Math.abs((pi - Math.PI)) / Math.PI) + "\n");

        System.out.println("Ntot : " + totalCount);
        System.out.println("Nombre de processeurs disponibles : " + numWorkers);
        System.out.println("Durée (ms) : " + duration + "\n");

        writeResultsToFile(totalCount, numWorkers, pi, duration, sp);

        exec.shutdown();
        return total;
    }

    private long measureSequentialTime(int totalCount) {
        long startTime = System.currentTimeMillis();
        int nAtomSuccess = 0;
        Random random = new Random();

        for (int i = 0; i < totalCount; i++) {
            double x = random.nextDouble();
            double y = random.nextDouble();
            if (x * x + y * y <= 1) {
                nAtomSuccess++;
            }
        }

        double pi = 4.0 * nAtomSuccess / totalCount;
        long stopTime = System.currentTimeMillis();
        long duration = stopTime - startTime;

        System.out.println("Pi séquentiel : " + pi);
        System.out.println("Durée séquentielle (ms) : " + duration);

        return duration;
    }

    private void writeResultsToFile(int totalCount, int numWorkers, double pi, long duration, double sp) throws IOException {
        String filePath = "./results.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(totalCount + "," + numWorkers + "," + pi + "," + duration + "," + sp + "\n");
        }
    }
}