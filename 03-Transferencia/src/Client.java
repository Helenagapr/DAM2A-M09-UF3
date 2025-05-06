import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    static final String DIR_ARRIBADA = "/tmp";
    static final int PORT = 9999;
    static final String HOST = "localhost";
    static ObjectOutputStream sortida;
    static ObjectInputStream entrada;
    private Socket socket;

    public void connectar() {
        try {
            System.out.println("Connectant a -> " + HOST + ":" + PORT);
            socket = new Socket(HOST, PORT);
            System.out.println("Connexio acceptada: " + socket.getInetAddress());
            sortida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rebreFitxers() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Nom del fitxer a rebre ('sortir' per sortir): ");
            String nomFitxer = scanner.nextLine();

            if (nomFitxer.equalsIgnoreCase("sortir")) {
                System.out.println("Sortint...");
                return;
            }

            sortida.writeObject(nomFitxer);
            sortida.flush();

            System.out.print("Nom del fitxer a guardar: ");
            String nomGuardar = scanner.nextLine();
            if (nomGuardar.isEmpty()) {
                nomGuardar = DIR_ARRIBADA + "/" + new File(nomFitxer).getName();
            }

            byte[] contingut = (byte[]) entrada.readObject();
            if (contingut.length == 0) {
                System.out.println("No s'ha rebut contingut del fitxer.");
            } else {
                FileOutputStream fos = new FileOutputStream(nomGuardar);
                fos.write(contingut);
                fos.close();
                System.out.println("Fitxer rebut i guardat com: " + nomGuardar);
            }

            // Esperar explícitamente el "sortir"
            while (true) {
                System.out.print("Escriu 'sortir' per tancar la connexió: ");
                String ordre = scanner.nextLine();
                if (ordre.equalsIgnoreCase("sortir")) {
                    System.out.println("Sortint...");
                    break;
                }
            }

            // Enviar "sortir" al servidor
            sortida.writeObject("sortir");
            sortida.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tancarConnexio() {
        try {
            if (entrada != null) entrada.close();
            if (sortida != null) sortida.close();
            if (socket != null) socket.close();
            System.out.println("Connexió tancada.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.connectar();
        client.rebreFitxers();
        client.tancarConnexio();
    }
}
