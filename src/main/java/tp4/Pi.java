package tp4;

import tp4.Master;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Approximates PI using the Monte Carlo method.  Demonstrates
 * use of Callables, Futures, and thread pools.
 *
 * Méthode modifié pour évaluer la scalabilité forte et faible.
 */
public class Pi {
    public static void pi(String filename, boolean scalabilite) throws InterruptedException, ExecutionException, IOException {
        int maxThreads = Runtime.getRuntime().availableProcessors(); // Utiliser tous les cœurs dispo
        Master master = new Master();

        String filename2 = filename;

        int[] totalCounts = {1600000, 16000000, 160000000}; // Tailles de problème
        int[] numWorkers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14}; // Nombre de threads testés

        for (int totalCount : totalCounts) {
            for (int worker : numWorkers) {
                for (int evaluation = 1; evaluation <= 10; evaluation++) {
                    if (scalabilite) {
                        master.doRun(totalCount / worker, worker, filename);
                    } else {
                        filename = filename.replace(".txt", "_" + totalCount + ".txt");
                        master.doRun(totalCount, worker, filename);
                        filename = filename2;
                    }
                }
            }
        }
    }

    /**
     * Méthode qui lance Pi avec les paramètres nécessaires dont
     * le fait qui si on souhaite évaluer une scalabilité forte ou faible
     * et le chemin du fichier dans lequel on va rentrer les résultats.
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        boolean scalabilite = true; // Scalabilité forte si true, scalabilité faible sinon

        if (scalabilite) {
            pi("src/main/java/tp4/resultats/results_fort.txt", scalabilite);
        } else {
            pi("src/main/java/tp4/resultats/results_faible.txt", scalabilite);
        }}
}
