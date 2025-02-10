package tp4;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Master {
    private final ExecutorService exec;

    public Master(int maxThreads) {
        this.exec = Executors.newFixedThreadPool(maxThreads);
    }

    public long doRun(int totalCount, int numWorkers, String filename) throws InterruptedException, ExecutionException {
        long startTime = System.nanoTime(); // Utiliser nanoTime pour plus de précision

        int iterationsPerThread = totalCount / numWorkers;

        // Création des tâches
        List<Callable<Long>> tasks = new ArrayList<>();
        for (int i = 0; i < numWorkers; ++i) {
            tasks.add(new Worker(iterationsPerThread));
        }

        // Exécuter les tâches en parallèle
        List<Future<Long>> results = exec.invokeAll(tasks);
        long total = 0;

        // Récupérer les résultats
        for (Future<Long> f : results) {
            total += f.get();
        }

        double pi = 4.0 * total / totalCount;

        long stopTime = System.nanoTime();
        long elapsedTime = (stopTime - startTime) / 1_000_000; // Convertir en millisecondes

        // Affichage des résultats
        System.out.printf("\nPi approximé: %.10f | Erreur: %e | N: %d | Threads: %d | Temps: %d ms\n",
                pi, Math.abs((pi - Math.PI)) / Math.PI, totalCount, numWorkers, elapsedTime);

        // Écrire les résultats dans un fichier
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(String.format("%e %d %d %d\n", Math.abs((pi - Math.PI)) / Math.PI, totalCount, numWorkers, elapsedTime));
            writer.flush(); // S'assurer que les données sont bien écrites
        } catch (IOException e) {
            e.printStackTrace();
        }

        return total;
    }

    public void shutdown() {
        exec.shutdown();
    }
}
