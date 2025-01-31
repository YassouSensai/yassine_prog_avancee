package tp4;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

class Master {
    // Cette méthode effectue le calcul parallèle et enregistre les résultats dans un fichier CSV.
    public long doRun(int totalCount, int numWorkers) throws InterruptedException, ExecutionException, IOException {
        long startTime = System.currentTimeMillis();

        // Création des tâches pour chaque worker
        List<Callable<Long>> tasks = new ArrayList<>();
        int baseCount = totalCount / numWorkers;
        int remainder = totalCount % numWorkers;

        // Assignation du travail entre les workers
        for (int i = 0; i < numWorkers; i++) {
            int taskSize = baseCount + (i < remainder ? 1 : 0);
            tasks.add(new Worker(taskSize)); // Crée une nouvelle tâche pour chaque worker
        }

        // Exécution des tâches dans un pool de threads
        ExecutorService exec = Executors.newFixedThreadPool(numWorkers);
        List<Future<Long>> results = exec.invokeAll(tasks);
        long total = 0;

        // Collecte des résultats de chaque worker
        for (Future<Long> f : results) {
            total += f.get();
        }

        // Calcul de Pi
        double pi = 4.0 * total / totalCount;

        long stopTime = System.currentTimeMillis();
        long duration = stopTime - startTime;

        // Enregistrement des résultats dans le fichier CSV sans calculer le speedup
        writeResultsToFile(totalCount, numWorkers, pi, duration);

        exec.shutdown();
        return duration;
    }

    // Cette méthode effectue une estimation de Pi de manière séquentielle
    private long measureSequentialTime(int totalCount) {
        int nAtomSuccess = 0;
        Random random = new Random();
        long startTime = System.currentTimeMillis();

        // Calcul séquentiel de Pi
        for (int i = 0; i < totalCount; i++) {
            double x = random.nextDouble();
            double y = random.nextDouble();
            if (x * x + y * y <= 1) {
                nAtomSuccess++;
            }
        }

        long stopTime = System.currentTimeMillis();
        long duration = stopTime - startTime;

        double pi = 4.0 * nAtomSuccess / totalCount;
        System.out.printf("Pi séquentiel: %.5f | Durée séquentielle: %d ms%n", pi, duration);

        return duration;
    }

    private void writeResultsToFile(int totalCount, int numWorkers, double pi, long duration) throws IOException {
        String filePath = "src/main/java/tp4/results.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            if (new java.io.File(filePath).length() == 0) {
                writer.write("Ntot,NbProc,Error,Time\n"); // Enregistrement sans le SpeedUp
            }
            System.out.println((totalCount + "," + numWorkers + "," + String.format("%.5e", Math.abs(Math.PI - pi)) + "," + duration + "\n"));
            writer.write(totalCount + "," + numWorkers + "," + String.format("%.5e", Math.abs(Math.PI - pi)) + "," + duration + "\n");        }
    }
}
