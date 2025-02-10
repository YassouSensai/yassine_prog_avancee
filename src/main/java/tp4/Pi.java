package tp4;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Pi {
    public static void pi(String filename) throws InterruptedException, ExecutionException, IOException {
        int maxThreads = Runtime.getRuntime().availableProcessors(); // Utiliser tous les cœurs dispo
        Master master = new Master(maxThreads);

        int[] totalCounts = {16_000_000, 160_000_000, 1_600_000_000}; // Tailles de problème
        int[] numWorkers = {11, 12}; // Nombre de threads testés

        for (int totalCount : totalCounts) {
            for (int workers : numWorkers) {
                for (int evaluation = 1; evaluation <= 10; evaluation++) {
                    master.doRun(totalCount, workers, filename);
                }
            }
        }

        master.shutdown(); // Arrêter les threads après usage
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        pi("src/main/java/tp4/results.txt");
    }
}
