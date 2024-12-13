package tp4;

public class Assignment102 {
    public static void main(String[] args) {
        int nb_proc = 4;
        int n_total = 10000;

        PiMonteCarlo PiVal = new PiMonteCarlo(100000, nb_proc, n_total);
        long startTime = System.currentTimeMillis();
        double value = PiVal.getPi();
        long stopTime = System.currentTimeMillis();

        System.out.println("Approx value: " + value);
        System.out.println("Erreur relative: " + Math.abs(value - Math.PI) / Math.PI);
        System.out.println("Nb processus: " + Runtime.getRuntime().availableProcessors());
        System.out.println("N_total: " + n_total);
        System.out.println("Temps d'execution: " + (stopTime - startTime) + "ms");
    }
}