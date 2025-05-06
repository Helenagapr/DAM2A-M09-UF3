import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Fitxer {
    private String nom;
    private byte[] contingut;

    public Fitxer(String nom) {
        this.nom = nom;
    }

    public byte[] getContingutBytes() {
        File fitxer = new File(nom);
        if (!fitxer.exists() || !fitxer.isFile()) {
            return null;
        }
        try {
            return Files.readAllBytes(fitxer.toPath());
        } catch (IOException e) {
            return null;
        }
    }
}

