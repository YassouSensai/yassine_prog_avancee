package org.example;

import java.awt.*;
import javax.swing.*;

class UneFenetre extends JFrame {
    private final int LARG = 1000, HAUT = 100; // Hauteur réduite pour une ligne
    private final int NB_LIGNES = 20;

    public UneFenetre() {
        super("Le Mobile");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container leconteneur = getContentPane();
        leconteneur.setLayout(new GridLayout(NB_LIGNES, 1)); // 5 lignes

        for (int i = 0; i < NB_LIGNES; i++) {
            UnMobile sonMobile = new UnMobile(LARG, HAUT);
            leconteneur.add(sonMobile);
            new Thread(sonMobile).start(); // Démarre le thread
        }

        setSize(LARG, NB_LIGNES * HAUT); // Ajuste la taille de la fenêtre
        setVisible(true);
    }
}