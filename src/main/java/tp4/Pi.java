package tp4;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Pi {
    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        try (FileWriter writer = new FileWriter("./processus.csv")) {
            writer.write("NumWorkers,Evaluation,Time\n");
            for (int numWorkers = 1; numWorkers <= 8; numWorkers++) {
                for (int evaluation = 1; evaluation <= 10; evaluation++) {
                    long startTime = System.nanoTime();
                    new Master().doRun(50000, numWorkers);
                    long endTime = System.nanoTime();
                    long duration = (endTime - startTime) / 1000000; // Convert to milliseconds
                    writer.write(numWorkers + "," + evaluation + "," + duration + "\n");
                }
            }
        }
    }
}