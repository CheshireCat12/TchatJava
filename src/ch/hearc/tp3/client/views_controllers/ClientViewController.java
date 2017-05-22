package ch.hearc.tp3.client.views_controllers;



import ch.hearc.tp3.client.Client;
import ch.hearc.tp3.client.services.ClientServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ClientViewController
{
    protected Client client;
    private ClientServices clientServices;

    public ClientServices getClientServices()
    {
        return clientServices;
    }

    @FXML
    private TextField tf_serverAddress;

    @FXML
    private TextField tf_serverPort;

    @FXML
    private TextField tf_message;

    @FXML
    private Button btn_connection;

    @FXML
    private Button btn_send;

    @FXML
    private TextArea ta_message;
    
    @FXML
    private Label lbl_isConnected;

    @FXML
    private void handlerBtnConnection(ActionEvent event)
    {
        
        if(clientServices.connection(tf_serverAddress.getText(), tf_serverPort.getText()))
        {
            btn_send.setDisable(false);
            tf_message.setDisable(false);
            ta_message.setDisable(false);
            
            clientServices.start();
        }
        else
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur, Connexion");
            alert.setContentText("Ooops, la connexion au serveur n'a pas marcher!");

            alert.showAndWait();
        }
        
    }

    @FXML
    private void initialize()
    {
        clientServices = new ClientServices();
        
        //tf_message.textProperty().bind(clientServices.toto);
        lbl_isConnected.textProperty().bind(clientServices.isConnected);
        ta_message.textProperty().bind(clientServices.message);
        //tf_message.textProperty().bindBidirectional(clientServices.toto);
    }

    @FXML
    private void handlerCloseWindow()
    {
        // TODO Auto-generated method stub
        clientServices.closeConnection();
    }
    
    @FXML
    private void handlerBtnSend(ActionEvent event)
    {
        System.out.println(tf_message.getText());
        clientServices.sendMessage(tf_message.getText());
    }

    public void setMainClient(Client client)
    {
        this.client = client;
    }

}
