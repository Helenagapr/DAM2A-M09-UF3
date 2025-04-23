import java.io.ObjectInputStream;

public class FilServidorXat extends Thread{
    private ObjectInputStream in;
    private static final String MSG_SORTIR = "sortir";
    private String nom;

    public FilServidorXat(ObjectInputStream in, String nom) {
        this.in = in;
        this.nom = nom;
    }

    @Override
    public void run() {
        try {
            String missatge;
            do {
                missatge = (String) in.readObject();
                System.out.println("Rebut: " + missatge);
            } while (!missatge.equalsIgnoreCase(MSG_SORTIR));
            System.out.println("Fil de xat finalitzat.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
