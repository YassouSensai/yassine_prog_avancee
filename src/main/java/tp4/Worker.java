package tp4;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class Worker implements Callable<Long> {
    private final int numIterations;

    public Worker(int numIterations) {
        this.numIterations = numIterations;
    }

    @Override
    public Long call() {
        long circleCount = 0;
        for (int j = 0; j < numIterations; j++) {
            double x = ThreadLocalRandom.current().nextDouble();
            double y = ThreadLocalRandom.current().nextDouble();
            if ((x * x + y * y) < 1) {
                circleCount++;
            }
        }
        return circleCount;
    }
}
