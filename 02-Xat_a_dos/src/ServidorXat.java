import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServidorXat {
    static final int PORT=9999;
    static final String HOST="localhost";
    static final String MSG_SORTIR="sortir";
    private ServerSocket serverSocket;


    public void iniciarServidor(){
        serverSocket = null;

        try{
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciat a " + HOST + ":" + PORT);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void pararServidor(){
        try{
            if(serverSocket != null && !serverSocket.isClosed()){
                serverSocket.close();
                System.out.println("Servidor aturat");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getNom(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        out.writeObject("Escriu el teu nom:");
        return (String) in.readObject();
    }

    public static void main(String[] args) {

        try{
            ServidorXat servidorXat = new ServidorXat();
            servidorXat.iniciarServidor();
        
            Socket clientSocket = servidorXat.serverSocket.accept();
            System.out.println("Client connectat: " + clientSocket.getInetAddress());

            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            
            String nomClient = servidorXat.getNom(in, out);
            System.out.println("Nom rebut: " + nomClient);
            System.out.println("Fil de xat creat.");
            
            FilServidorXat fil = new FilServidorXat(in, nomClient);
            fil.start();
            System.out.println("Fil de " + nomClient + " iniciat");

            Scanner scanner = new Scanner(System.in);
            String missatge;
            do {
                System.out.print("Missatge ('sortir' per tancar): ");
                missatge = scanner.nextLine();
                out.writeObject(missatge);
            } while (!missatge.equalsIgnoreCase(MSG_SORTIR));

            fil.join();
            clientSocket.close();
            scanner.close();
            servidorXat.pararServidor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        

    }
}
