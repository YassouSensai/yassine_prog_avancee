package tp4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


class PiMonteCarlo {
    AtomicInteger nAtomSuccess;
    int nThrows;
    double value;

    int nb_proc;
    int n_total;


    class MonteCarlo implements Runnable {
        @Override
        public void run() {
            double x = Math.random();
            double y = Math.random();
            if (x * x + y * y <= 1)
                nAtomSuccess.incrementAndGet();
        }
    }
    public PiMonteCarlo(int i, int nb_proc, int n_total) {
        this.nAtomSuccess = new AtomicInteger(0);
        this.nThrows = i;
        this.value = 0;

        this.nb_proc = nb_proc;
        this.n_total = n_total;
    }
    public double getPi() {
        ExecutorService executor = Executors.newWorkStealingPool(this.nb_proc);
        for (int i = 1; i <= nThrows; i++) {
            Runnable worker = new MonteCarlo();
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        value = 4.0 * nAtomSuccess.get() / nThrows;
        return value;
    }
}