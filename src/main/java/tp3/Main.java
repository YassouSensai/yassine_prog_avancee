package tp3;

public class Main {
    public static void main(String[] args) {
        BoiteALettre bal = new BoiteALettre();

        Producteur prod1 = new Producteur(bal);

        Facteur cons = new Facteur(bal);

        prod1.start();

        cons.start();
    }
}