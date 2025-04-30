import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Fitxer {
    private String nom;
    private byte[] contingut;

    public Fitxer(String nom){
        this.nom = nom;
    }

    public void getContingut(){
        File fitxer = new File(nom);
        if (!fitxer.exists() || !fitxer.isFile()) {
            System.out.println("Fitxer no trobat: " + nom);
            contingut = new byte[0];
            return;
        }

        try {
            contingut = Files.readAllBytes(fitxer.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            contingut = new byte[0];
        }
    }

    public byte[] getContingutBytes() {
        return contingut;
    }

}
