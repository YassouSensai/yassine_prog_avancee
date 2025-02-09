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
        long startTime = System.currentTimeMillis();

        int iterationsPerThread = totalCount / numWorkers; // Correction importante

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

        double pi = 4.0 * total / totalCount; // Correction du calcul de π

        long stopTime = System.currentTimeMillis();

        System.out.printf("\nValeur approchée: %.10f\n", pi);
        System.out.printf("Erreur: %e\n", Math.abs((pi - Math.PI)) / Math.PI);
        System.out.printf("N total: %d\n", totalCount);
        System.out.printf("Nombre de processus: %d\n", numWorkers);
        System.out.printf("Temps d'exécution: %d ms\n", stopTime - startTime);

        // Écrire les résultats dans un fichier
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(String.format("%e %d %d %d\n", Math.abs((pi - Math.PI)) / Math.PI, totalCount, numWorkers, stopTime - startTime));
            System.out.println("Fichier écrit");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return total;
    }

    public void shutdown() {
        exec.shutdown();
    }
}
