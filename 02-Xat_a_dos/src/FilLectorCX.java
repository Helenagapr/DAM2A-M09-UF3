import java.io.ObjectInputStream;

public class FilLectorCX extends Thread {
    private ObjectInputStream in;
    private static final String MSG_SORTIR = "sortir";

    public FilLectorCX(ObjectInputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            String missatge;
            do {
                missatge = (String) in.readObject();
                System.out.println("Rebut: " + missatge);
            } while (!missatge.equalsIgnoreCase(MSG_SORTIR));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
