package tp4;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Pi {
    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        Master master = new Master();
        int totalCount = 50000;

        for (int nbexperiences = 1; nbexperiences <= 3; nbexperiences++) {
            for (int numWorkers = 1; numWorkers <= 8; numWorkers++) {
                for (int evaluation = 1; evaluation <= 1000; evaluation++) {
                    master.doRun(totalCount, numWorkers, "src/main/java/tp4/results.txt");
                }
            }

            totalCount += 50000;
        }
    }
}
