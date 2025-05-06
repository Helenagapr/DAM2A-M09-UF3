import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    static final int PORT = 9999;
    static final String HOST = "localhost";
    private ServerSocket serverSocket;

    public Socket connectar() {
        try {
            System.out.println("Acceptant connexions en -> " + HOST + ":" + PORT);
            serverSocket = new ServerSocket(PORT);
            System.out.println("Esperant connexio...");
            Socket socket = serverSocket.accept();
            System.out.println("Connexio acceptada: " + socket.getInetAddress());
            return socket;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void tancarConnexio(Socket socket) {
        try {
            System.out.println("Tancant connexió amb el client: " + socket.getInetAddress());
            if (socket != null) socket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enviarFitxer(Socket socket) {
        ObjectInputStream entrada = null;
        ObjectOutputStream sortida = null;

        try {
            entrada = new ObjectInputStream(socket.getInputStream());
            sortida = new ObjectOutputStream(socket.getOutputStream());

            // Esperar el nombre del archivo
            System.out.println("Esperant el nom del fitxer del client..");
            Object obj = entrada.readObject();

            if (obj == null || !(obj instanceof String)) {
                System.out.println("Error llegint el fitxer del client: " + obj);
                System.out.println("Nom del fitxer buit O nul. Sortint...");
                return;
            }

            String nomFitxer = (String) obj;

            if (nomFitxer.equalsIgnoreCase("sortir")) {
                System.out.println("Nom del fitxer és 'sortir'. Sortint...");
                return;
            }

            System.out.println("Nomfitxer rebut: " + nomFitxer);
            Fitxer fitxer = new Fitxer(nomFitxer);
            byte[] contingut = fitxer.getContingutBytes();

            if (contingut == null) {
                System.out.println("El fitxer no existeix o no es pot llegir: " + nomFitxer);
                sortida.writeObject(new byte[0]);  // Enviar contingut buit
                return;
            }

            System.out.println("Contingut del fitxer a enviar: " + contingut.length + " bytes");
            sortida.writeObject(contingut);
            sortida.flush();
            System.out.println("Fitxer enviat al client: " + nomFitxer);

            // Esperar el comando 'sortir' del cliente
            String sortidaComanda = (String) entrada.readObject();
            if ("sortir".equalsIgnoreCase(sortidaComanda)) {
                System.out.println("Rebuda comanda 'sortir'. Tancant connexió.");
            }

        } catch (Exception e) {
            System.out.println("Error llegint el fitxer del client: " + e.getMessage());
            System.out.println("Nom del fitxer buit O nul. Sortint...");
        } finally {
            try {
                if (entrada != null) entrada.close();
                if (sortida != null) sortida.close();
                if (socket != null) {
                    System.out.println("Tancant connexió amb el client: " + socket.getInetAddress());
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        Socket socket = servidor.connectar();
        if (socket != null) {
            servidor.enviarFitxer(socket);
            servidor.tancarConnexio(socket);
        }
    }
}
