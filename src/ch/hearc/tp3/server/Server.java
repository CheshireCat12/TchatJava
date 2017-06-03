package ch.hearc.tp3.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import ch.hearc.tp3.server.ClientHandler;

public class Server
{
    private static ArrayList<ClientHandler> clientHandlers;
    
    public final static String serverViewController = "views_controllers/ServerView.fxml";

    public static void main(String[] args)
    {
        clientHandlers = new ArrayList<>();
        try(ServerSocket serverSocket = new ServerSocket(50885))
        {
            System.out.println("SERVER - My workers are ready");
            
            while(true)
            {
                //The socket is closed in the handler
                Socket socket = serverSocket.accept();
                ClientHandler newClient = new ClientHandler(socket, clientHandlers);
                
                clientHandlers.add(newClient);
                
                Thread thread = new Thread(newClient);
                thread.start();
            }
        } catch (IOException e)
        {
            System.out.println("Fermeture");
           // e.printStackTrace();
        }
    }

    
}
