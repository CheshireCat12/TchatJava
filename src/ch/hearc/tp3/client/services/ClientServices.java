package ch.hearc.tp3.client.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ClientServices extends Service<String>
{
    public SimpleStringProperty isConnected = new SimpleStringProperty();
    public SimpleStringProperty message = new SimpleStringProperty();
    
    private PrintWriter out;
    private StringBuffer str_message = new StringBuffer();
    private Socket socket;
    private BufferedReader in;

    @Override
    protected Task<String> createTask()
    {
        return new Task<String>()
        {
            @Override
            protected String call() throws Exception
            {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());
                while(true)
                {
                    if(!socket.isClosed())
                    {
                        if(in.ready())
                        {
                            
                            str_message.append(in.readLine().toString() + "\n");
                            message.setValue(str_message.toString());
                        } 
                    }
                    Thread.sleep(200);
                }
            }

        };
    }

    public boolean connection(String serverAddress, String portAddress)
    {
        boolean connexion = false;
        try
        {
            socket = new Socket(serverAddress, Integer.parseInt(portAddress));

            isConnected.setValue("Connecté");
            
            connexion = true;
        } catch (NumberFormatException | IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connexion;
    }
    
    public void sendMessage(String message)
    {
        System.out.println("from service " + message);
        out.println(message);
        
        out.flush();
    }

    public void closeConnection()
    {
        try
        {
            System.out.println("close socket");
            in.close();
            out.close();
            //out.close();
            socket.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
