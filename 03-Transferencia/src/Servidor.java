import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    static final int PORT=9999;
    static final String HOST="localhost";
    private ServerSocket serverSocket;

    public Socket connectar(){
        try{
            System.out.println("Acceptant connexions en -> " + HOST + ":" + PORT);
            serverSocket = new ServerSocket(PORT);
            System.out.println("Esperant connexio...");
            Socket socket = serverSocket.accept();
            System.out.println("Connexio acceptada: " + socket.getInetAddress());

            return socket;
        } catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public void tancarConnexio(Socket socket){
        
        try{
            System.out.println("Tancant connexió amb el client: " + socket.getInetAddress());
            if (socket != null) socket.close();
            if (serverSocket != null) serverSocket.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void enviarFitxers(Socket socket){
        try{
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream sortida = new ObjectOutputStream(socket.getOutputStream());
            
            System.out.println("Esperant el nom del fitxer del client..");
            String nomFitxer = (String) entrada.readObject();

            if (nomFitxer == null || nomFitxer.isEmpty()) {
                System.out.println("Nom del fitxer buit O nul. Sortint...");
                return;
            }

            System.out.println("Nom fitxer rebut: " + nomFitxer);

            Fitxer fitxer = new Fitxer(nomFitxer);
            fitxer.getContingut();

            byte[] contingut = fitxer.getContingutBytes();
            System.out.println("Contingut del fitxer a enviar: " + contingut.length + " bytes");

            sortida.writeObject(fitxer.getContingutBytes());
            sortida.flush();

            System.out.println("Fitxer enviat al client: " + nomFitxer);
            entrada.close();
            sortida.close();
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("Error llegint el fitxer del client: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();

        Socket socket = servidor.connectar();

        servidor.enviarFitxers(socket);
        servidor.tancarConnexio(socket);
    }
}
