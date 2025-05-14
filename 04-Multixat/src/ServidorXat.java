import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class ServidorXat {
    static final int PORT = 9999;
    static final String HOST = "localhost";
    static final String MSG_SORTIR = "sortir";
    Hashtable<String, GestorClients> clients = new Hashtable<>();
    private boolean sortir = false;

    public void servidorAEscoltar(){
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Servidor iniciat a " + HOST + ":" + PORT);

            while(true){
                Socket cliSocket = serverSocket.accept();
                System.out.println("Client connectat: " + cliSocket.getInetAddress());

                GestorClients gestorClients = new GestorClients(cliSocket, this);
                gestorClients.start();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void finalitzarXat(){
        enviarMissatgeGrup(MSG_SORTIR);
        clients.clear();
        System.out.println("Tancant tots els clients.");
        System.exit(0);
    }

    public void afegirClient(GestorClients gestorClients){
        clients.put(gestorClients.getNom(), gestorClients);
        System.out.println(gestorClients.getNom() + " connectat.");
        System.out.println("DEBUG: multicast Entra: " + gestorClients.getNom());
        String msg = Missatge.getMissatgeGrup("Entra: " + gestorClients.getNom()); 
        enviarMissatgeGrup(msg);
    }

    public void eliminarClient(String nom){
        if(clients.containsKey(nom)){
            clients.remove(nom);
        }
    }

    public void enviarMissatgeGrup(String msg){
        for(GestorClients gestor: clients.values()){
            gestor.enviarMissatge("Servidor", msg);
        }
    }

    public void enviarMissatgePersonal(String nomDestinatari, String nomRemitent, String msg){        
        GestorClients client = clients.get(nomDestinatari); 
        if(client != null){

            client.enviarMissatge(nomRemitent, msg);
        }
        
    }

    public static void main(String[] args) {
        ServidorXat servidorXat = new ServidorXat();

        servidorXat.servidorAEscoltar();
    }
}
