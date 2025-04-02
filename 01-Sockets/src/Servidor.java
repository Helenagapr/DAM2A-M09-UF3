import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    static final int PORT=7777;
    static final String HOST="localhost";

    private ServerSocket serverSocket;
    private Socket clienSocket;
    private boolean end= false;

    public void connecta(){
        serverSocket = null;
        clienSocket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println();
            System.out.println("Servidor en marxa a " + HOST + ":" + PORT);
            System.out.println("Esperant connexions a " + HOST + ":" + PORT);

            clienSocket = serverSocket.accept();
            System.out.println("Client conectat: " + clienSocket.getInetAddress());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void repDades(){
        BufferedReader reader = null;

        try{
            reader = new BufferedReader(new InputStreamReader(clienSocket.getInputStream()));

            String line;
            while((line = reader.readLine()) != null){
                System.out.println("Rebut: " + line);
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            try {
                if(reader != null){
                    reader.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void tanca(){
        try {
            clienSocket.close();
            if(serverSocket != null && !serverSocket.isClosed()){
                serverSocket.close();
                System.out.println("Servidor tancat.");
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.connecta();
        servidor.repDades();
        servidor.tanca();  

    }


}
