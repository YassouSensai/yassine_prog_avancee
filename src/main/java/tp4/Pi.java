package tp4;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Pi {
    public static void pi(String filename) throws InterruptedException, ExecutionException, IOException {
        int maxThreads = 6; // Choisir un nombre max de threads
        Master master = new Master(maxThreads);

        int[] totalCounts = {16000000, 160000000};
        int[] numWorkers = {1, 2, 3, 4, 5, 6, 7, 8};

        for (int totalCount : totalCounts) {
            for (int workers : numWorkers) {
                for (int evaluation = 1; evaluation <= 10; evaluation++) {
                    master.doRun(totalCount, workers, filename);
                }
            }
        }

        master.shutdown(); // Arrêter les threads proprement
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        pi("src/main/java/tp4/results.txt");
    }
}
