import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientXat {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public void connecta() throws IOException {
        socket = new Socket("localhost", 9999);
        System.out.println("Client connectat a localhost:9999");
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        System.out.println("Flux d'entrada i sortida creat.");
    }

    public void enviarMissatge(String msg) throws IOException {
        System.out.println("Enviant missatge: " + msg);
        out.writeObject(msg);
    }

    public void tancarClient() throws IOException {
        System.out.println("Tancant client...");
        if (socket != null) socket.close();
        if (out != null) out.close();
        if (in != null) in.close();
        System.out.println("Client tancat.");
    }

    public static void main(String[] args) {
        try {
            ClientXat client = new ClientXat();
            client.connecta();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Missatge ('sortir' per tancar): ");
            System.out.println("Fil de lectura iniciat");


            String resposta = (String) client.in.readObject();
            System.out.println("Rebut: " + resposta);
            
            String nom = scanner.nextLine();
            client.enviarMissatge(nom);

            
            FilLectorCX fil = new FilLectorCX(client.in);
            fil.start();
            
            String missatge;
            do {
                System.out.print("Missatge ('sortir' per tancar): ");
                missatge = scanner.nextLine();
                client.enviarMissatge(missatge);

            } while (!missatge.equalsIgnoreCase("sortir"));

            fil.join();
            scanner.close();
            client.tancarClient();
            System.out.println("El servidor ha tancat la connexió.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
