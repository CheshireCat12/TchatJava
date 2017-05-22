package ch.hearc.tp3.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable
{
    private String clientName;
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private ArrayList<Socket> clientList;
    private ArrayList<ClientHandler> clientHandlers;

    public ClientHandler(Socket socket, String clientName, ArrayList<Socket> clientList, ArrayList<ClientHandler> clientHandlers)
    {
        this.clientName = clientName;
        this.socket = socket;
        this.clientList = clientList;
        
        this.clientHandlers = clientHandlers;
        
        try
        {
            this.output = new PrintWriter(socket.getOutputStream(), true);
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    @Override
    public void run()
    {
        /*BufferedOutputStream outputStream = null;
        PrintWriter out = null;
        BufferedReader in = null;*/
        try
        {
            System.out.println("I'm " + this.clientName);
            
            
            //StringBuffer messa = new StringBuffer();
            //String messages = new String();

            output.println("From server : Connexion réussi");
            //output.flush();
            //out.close();

            while(socket.getInputStream().read() != -1)
            {
                if(input.ready()) { 
                    //messa.append(input.readLine());
                    String messages = input.readLine();

                    sendToAll(messages);
                }    
                try
                {
                    Thread.sleep(200);
                } catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                System.out.println("close");
                clientList.remove(socket);
                input.close();
                output.close();
                socket.close();
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public void sendToAll(String messages)
    {
        
        for(ClientHandler client : clientHandlers)
        {
            System.out.println(messages);
            
            client.output.println(messages);
            //client.output.flush();
        }

    }
}
