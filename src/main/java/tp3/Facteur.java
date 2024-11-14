package tp3;

/**
 *
 */
public class Facteur extends Thread {

    /**
     *
     */
    private BoiteALettre boiteALettre;

    /**
     * Default constructor
     */
    public Facteur(BoiteALettre bal) {
        boiteALettre = bal;
    }


    /**
     *
     */
    public void run() {

        try {
            while (true) {
                Thread.sleep(1000);

                Character lettreRecupere = boiteALettre.retirer();

                if (lettreRecupere == null) {
                    System.out.println("Aucune lettre disponible pour récupération.");
                    continue;
                }

                if (lettreRecupere == '*') {
                    System.out.println("Fin du traitement, le consommateur arrête.");
                    break;
                }

                System.out.println("J'ai récupéré la lettre : " + lettreRecupere + " | Nombre dans la file : " + boiteALettre.getQueueSize());

            }
        } catch (InterruptedException e) {
            System.out.println("Interruption lors de la récupération de la lettre.");
        }
    }

}