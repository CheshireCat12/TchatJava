package ch.hearc.tp3.client.views_controllers;

import java.io.File;
import java.util.Optional;

import ch.hearc.tp3.client.Client;
import ch.hearc.tp3.client.services.ClientServices;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.Pair;

public class ClientViewController
{
    protected Client client;
    private ClientServices clientServices;

    public ClientServices getClientServices()
    {
        return clientServices;
    }

    @FXML
    private TextField tf_pseudo;

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
    private MenuItem menuSendFile;

    @FXML
    private void handlerBtnConnection(ActionEvent event)
    {

        if (clientServices.connection())
        {
            btn_send.setDisable(false);
            tf_message.setDisable(false);
            ta_message.setDisable(false);

            menuSendFile.setDisable(false);

            clientServices.start();
        } else
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur, Connexion");
            alert.setContentText(
                    "Ooops, la connexion au serveur n'a pas marcher!");

            alert.showAndWait();
        }

    }

    @FXML
    private void handlerSendFile(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir le fichier a envoyer");
        File file = fileChooser.showOpenDialog(client.getPrimaryStage());

        if (file != null)
        {
            clientServices.sendFile(file);
        }
    }

    @FXML
    private void initialize()
    {
        clientServices = new ClientServices();

        lbl_isConnected.textProperty().bind(clientServices.connectionMessage);
        ta_message.textProperty().bind(clientServices.message);
    }

    @FXML
    private void handlerCloseWindow()
    {
        clientServices.closeConnection();
    }

    @FXML
    private void handlerBtnSend(ActionEvent event)
    {
        clientServices.sendMessage(tf_pseudo.getText(), tf_message.getText());
    }

    @FXML
    private void handlerFileDirectory(ActionEvent event)
    {
        TextInputDialog dialog = new TextInputDialog(
                clientServices.getPathFile());
        dialog.setTitle("Emplacement de téléchargement du fichier");
        dialog.setHeaderText(
                "Veuillez entrer l'emplacement où les fichiers seront téléchargés SVP.\nAttention le dossier doit exister !");

        Optional<String> result = dialog.showAndWait();
        // The Java 8 way to get the response value (with lambda expression).
        result.ifPresent(name -> clientServices.setPathFile(name));
    }

    @FXML
    private void handlerOptionConnection(ActionEvent event)
    {
        Dialog dialog = new Dialog<>();
        dialog.setTitle("Option de connexion");
        dialog.setHeaderText("Entrez les options de connexion.");

        // Set the button types.
        ButtonType btn_changeOption = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btn_changeOption,
                ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField IpAddress = new TextField(clientServices.getServeurAdress());
        IpAddress.setPromptText("Adresse IP :");
        
        TextField PortAddress = new TextField(clientServices.getPortAddress());
        PortAddress.setPromptText("Numéro de port");

        gridPane.add(new Label("Adresse du serveur :"), 0, 0);
        gridPane.add(IpAddress, 1, 0);
        gridPane.add(new Label("Port du serveur :"), 0, 1);
        gridPane.add(PortAddress, 1, 1);

        dialog.getDialogPane().setContent(gridPane);

        // Request focus on the username field by default.
        Platform.runLater(() -> IpAddress.requestFocus());

        // Convert the result to a username-password-pair when the login button
        // is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btn_changeOption)
            {
                return new Pair<>(IpAddress.getText(), PortAddress.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        if (result.isPresent()){
            //clientServices.setPortAddress(result.get);
            //System.out.println("Your name: " + Pair.getKey());
        }
        result.ifPresent(pair -> {
            clientServices.setPortAddress(pair.getValue());
            clientServices.setServeurAdress(pair.getKey());
        });
    }

    public void setMainClient(Client client)
    {
        this.client = client;
    }

}
