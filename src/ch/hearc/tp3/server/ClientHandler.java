package ch.hearc.tp3.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable
{

    private Socket socket;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ArrayList<ClientHandler> clientHandlers;

    public ClientHandler(Socket socket, 
            ArrayList<ClientHandler> clientHandlers)
    {
 
        this.socket = socket;

        this.clientHandlers = clientHandlers;

        try
        {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void run()
    {
        try
        {
            System.out.println("New connection Opened");

            outputStream.writeObject("From server : Connexion réussi");
            outputStream.flush();

            Object newObj;

            while (true)
            {
                newObj = inputStream.readObject();
                sendToAll(newObj);
            }

        } catch (IOException | ClassNotFoundException e)
        {
            System.out.println("Fermeture du socket avec de la VIOLENCE!!! ^^");
        } finally
        {
            try
            {
                clientHandlers.remove(socket);
                outputStream.close();
                inputStream.close();
                socket.close();
                System.out.println("Socket closed" );
            } catch (IOException e)
            {
                System.out.println("Erreur lors de la fermeture des sockets.");
            }
        }
    }

    public void sendToAll(Object data)
    {
        for (ClientHandler client : clientHandlers)
        {
            try
            {
                client.outputStream.writeObject(data);
            } catch (IOException e)
            {
                System.out.println("Erreur lors du renvoie des objects.");
            }
        }

    }
}
