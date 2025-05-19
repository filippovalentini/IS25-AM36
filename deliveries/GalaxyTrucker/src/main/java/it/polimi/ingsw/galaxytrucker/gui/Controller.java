package it.polimi.ingsw.galaxytrucker.gui;

import it.polimi.ingsw.galaxytrucker.network.MainClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;

public class Controller {
    private static Stage controlledStage; //stage of the JavaFX application

    public static void setControlledStage(Stage stage) {
        controlledStage = stage;
    }

    private static void changeScene(Scene scene) {
        controlledStage.setScene(scene);
    }

    @FXML
    private TextField ipTextField;
    @FXML
    private Button rmiButton;
    @FXML
    private Button socketButton;

    @FXML
    protected void onSocketButtonClick() {
        try {
            MainClient.startSocketClient(ipTextField.getText());
            System.out.println("[gui]: client socket started");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onRMIButtonClick() {
        try {
            MainClient.startClientRMI(ipTextField.getText());
            System.out.println("[gui]: client rmi started");
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/it/polimi/ingsw/galaxytrucker/setupgame.fxml"));
            Parent root = fxmlLoader.load();
            Scene secondaScena = new Scene(root, 600, 400);
            controlledStage.setScene(secondaScena);
            controlledStage.show();
        } catch (IOException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}