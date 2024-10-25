package org.example;

import java.awt.*;
import javax.swing.*;

class UnMobile extends JPanel implements Runnable
{
    int saLargeur, saHauteur, sonDebDessin;
    final int sonPas = 20, sonTemps=100, sonCote=40;

    static semaphore sem = new semaphore(1);

    UnMobile(int telleLargeur, int telleHauteur)
    {
        super();
        saLargeur = telleLargeur;
        saHauteur = telleHauteur;
        setSize(telleLargeur, telleHauteur);
    }

    public void run() {
        while (true){
            for (sonDebDessin=0; sonDebDessin < saLargeur/3; sonDebDessin+= sonPas) {
                repaint();
                try{Thread.sleep(sonTemps);}
                catch (InterruptedException telleExcp)
                {telleExcp.printStackTrace();}
            }
            sem.syncWait();
            for (sonDebDessin=saLargeur/3; sonDebDessin < 2*(saLargeur/3); sonDebDessin+= sonPas) {
                repaint();
                try{Thread.sleep(sonTemps);}
                catch (InterruptedException telleExcp)
                {telleExcp.printStackTrace();}
            }
            sem.syncSignal();
            for (sonDebDessin=2*(saLargeur/3); sonDebDessin < saLargeur; sonDebDessin+= sonPas) {
                repaint();
                try{Thread.sleep(sonTemps);}
                catch (InterruptedException telleExcp)
                {telleExcp.printStackTrace();}
            }
            sem.syncWait();


            for (sonDebDessin=saLargeur - sonPas; sonDebDessin >= 2*(saLargeur/3); sonDebDessin-= saLargeur/3 + sonPas) {
                repaint();
                try{Thread.sleep(sonTemps);}
                catch (InterruptedException telleExcp)
                {telleExcp.printStackTrace();}
            }
            sem.syncSignal();
            for (sonDebDessin=saLargeur - sonPas; sonDebDessin >= saLargeur/3; sonDebDessin-= saLargeur/3 + sonPas) {
                repaint();
                try{Thread.sleep(sonTemps);}
                catch (InterruptedException telleExcp)
                {telleExcp.printStackTrace();}
            }
            sem.syncWait();
            for (sonDebDessin=saLargeur - sonPas; sonDebDessin >= 0; sonDebDessin-= saLargeur/3 + sonPas) {
                repaint();
                try{Thread.sleep(sonTemps);}
                catch (InterruptedException telleExcp)
                {telleExcp.printStackTrace();}
            }
            sem.syncSignal();



        }


    }


    public void paintComponent(Graphics telCG)
    {
        super.paintComponent(telCG);
        telCG.fillRect(sonDebDessin, saHauteur/2, sonCote, sonCote);
    }
}