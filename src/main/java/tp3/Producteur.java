package tp3;

import java.io.*;
import java.util.*;

/**
 * Classe Producteur
 */
public class Producteur extends Thread {


    /**
     *
     */
    private char lettreAPoster;
    private BoiteALettre boiteALettre;

    private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ*".toCharArray();

    /**
     * Default constructor
     */
    public Producteur(BoiteALettre bal) {
        boiteALettre = bal;

    }

    /**
     *
     */
    public void run() {

        try {

            for (char lettre : alphabet) {
                Thread.sleep(200);
                lettreAPoster = lettre;


                while (true) {
                    if (!boiteALettre.deposer(lettreAPoster)) {
                        System.out.println("Echec de dépôt de la lettre : " + lettreAPoster);
                    } else {
                        System.out.println("J'ai déposé la lettre " + lettreAPoster + " || Etat de la file => " + boiteALettre.getQueueSize());
                        break;
                    }
                }
            }

        } catch (InterruptedException e) {
            System.out.println("Interruption lors du dépôt de la lettre.");
        }
    }

}