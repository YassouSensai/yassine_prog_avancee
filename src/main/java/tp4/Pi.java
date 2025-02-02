package tp4;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Pi {

    public static void pi(String filename) throws InterruptedException, ExecutionException, IOException {
        Master master = new Master();
        int[] totalCounts = {50000};
        int[] numWorkers = {1, 3, 2, 4, 5, 6};

        for (int totalCount : totalCounts) {
            for (int workers : numWorkers) {
                for (int evaluation = 1; evaluation <= 10000; evaluation++) {
                    master.doRun(totalCount, workers, filename);
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        pi("src/main/java/tp4/results.txt");
    }
}