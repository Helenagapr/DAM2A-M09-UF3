import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {
    static final int PORT=7777;
    static final String HOST="localhost";

    private Socket socket;
    private PrintWriter out;

    public void conecta(){
        socket = null;

        try {
            socket = new Socket(HOST, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println();
            System.out.println("Conectat a servidor en " + HOST + ":" + PORT);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void tanca(){
        try{
            out.close();
            socket.close();
            System.out.println("Client tancat");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void envia(String line){
        out.println(line); 
        System.out.println("Enviat al servidor: " + line); 
    }

    public static void main(String[] args) {
        Client client  =new Client();
        client.conecta();

        client.envia("Prova d'enviament 1");
        client.envia("Prova d'enviament 2");
        client.envia("Adeu!");

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Prem Enter per tancar el client...");
            reader.readLine();
        }catch (IOException e){
            e.printStackTrace();
        }
        
        client.tanca();
    }
}