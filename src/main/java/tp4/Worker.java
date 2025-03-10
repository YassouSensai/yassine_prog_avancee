package tp4;

import java.util.Random;
import java.util.concurrent.Callable;


/**
 * Task for running the Monte Carlo simulation.
 */
class Worker implements Callable<Long> {
    private int numIterations;

    public Worker(long num) {
        this.numIterations = num;
    }

    @Override
    public Long call() {
        int circleCount = 0;
        Random prng = new Random();
        for (int j = 0; j < numIterations; j++) {
            double x = prng.nextDouble();
            double y = prng.nextDouble();
            if ((x * x + y * y) < 1) ++circleCount;
        }
        return circleCount;
    }
}
