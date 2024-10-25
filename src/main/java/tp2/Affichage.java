package tp2; /**
 *
 */

import java.io.*;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.BufferedReader;
import java.lang.String;
import java.util.concurrent.Semaphore;

class Exclusion {};

public class Affichage extends Thread {
	String texte;

	static Exclusion exclusionMutuelle = new Exclusion();

	static semaphoreBinaire sem = new semaphoreBinaire(1);

	public Affichage(String txt) {
		texte = txt;
	}

	public void run() {

		sem.syncWait();
		System.out.println("J'entre en section critique");

		for (int i = 0; i < texte.length(); i++) {
			System.out.print(texte.charAt(i));

			try {
				sleep(100);
			} catch (InterruptedException e) {
			}
		}

		System.out.println("Je sors de section critique");
		sem.syncSignal();

	}
}