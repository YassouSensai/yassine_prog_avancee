package tp4;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Pi {
    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        Master master = new Master();
        int[] totalCounts = {12000, 120000, 1200000};
        int[] numWorkers = {1, 2, 3, 4, 5, 6, 7, 8};

        for (int totalCount : totalCounts) {
            for (int workers : numWorkers) {
                for (int evaluation = 1; evaluation <= 1000; evaluation++) {
                    master.doRun(totalCount, workers, "src/main/java/tp4/results.txt");
                }
            }
        }
    }
}
