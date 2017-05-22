package ch.hearc.tp3.server.views_controllers;


import ch.hearc.tp3.server.Server;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ServerViewController
{
    protected Server server;
    
    @FXML
    private Label lbl_server;

    public void setMainApp(Server server)
    {
        this.server = server;
    }
    
    @FXML
    private void initialize()
    {
        
    }
    
    public void setLabel(String infoServer)
    {
        this.lbl_server.setText(infoServer);
    }
}
