package ch.hearc.tp3.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import ch.hearc.tp3.server.ClientHandler;
import ch.hearc.tp3.server.views_controllers.ServerViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Server extends Application
{
    private Stage primaryStage;
    private ServerViewController controller;
    private static int clientNumber;
    private static ArrayList<Socket> clientListSocket;
    private static ArrayList<ClientHandler> clientHandlers;
    
    public final static String serverViewController = "views_controllers/ServerView.fxml";

    public static void main(String[] args)
    {
        //launch(args);
        clientListSocket = new ArrayList<>();
        clientHandlers = new ArrayList<>();
        try(ServerSocket serverSocket = new ServerSocket(50885))
        {
            System.out.println("SERVER - My workers are ready");
            
            while(true)
            {
                //The socket is closed in the handler
                Socket socket = serverSocket.accept();
                clientListSocket.add(socket);
                ClientHandler newClient = new ClientHandler(socket, "toto", clientListSocket, clientHandlers);
                
                clientHandlers.add(newClient);
                
                Thread thread = new Thread(newClient);
                //Thread thread = new Thread(new ClientHandler(socket, clientName + clientNumber));
                thread.start();
                
                clientNumber++;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage _primaryStage) throws Exception
    {
        this.primaryStage = _primaryStage;
        setView(serverViewController);
    }

    public void setView(String _viewController)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass()
                    .getResource(_viewController));

            AnchorPane root = (AnchorPane) loader.load();
            Scene scene = new Scene(root);
            
            controller = loader.getController();
            controller.setMainApp(this);
            controller.setLabel("toto a la plage");

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        } 
    }
}
