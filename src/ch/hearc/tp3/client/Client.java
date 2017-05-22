package ch.hearc.tp3.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ch.hearc.tp3.client.views_controllers.ClientViewController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Client extends Application
{
    private Stage primaryStage;
    private ClientViewController controller;
    public final static String serverViewController = "views_controllers/ClientView.fxml";

    public static void main(String[] args)
    {
        launch(args);
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
            controller.setMainClient(this);

            primaryStage.setScene(scene);
            primaryStage.show();
            
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    controller.getClientServices().closeConnection();
                    System.out.println("Stage is closing");
                }
            }); 
            
        } catch (IOException e)
        {
            e.printStackTrace();
        } 
    }
}
