package it.polimi.ingsw.galaxytrucker.gui;

import it.polimi.ingsw.galaxytrucker.network.MainClient;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class Controller {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField ipTextField;

    @FXML
    private Button rmiButton;
    @FXML
    private Button socketButton;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void onSocketButtonClick() {
        try {
            MainClient.startSocketClient(ipTextField.getText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onRMIButtonClick() {
        try {
            MainClient.startSocketClient(ipTextField.getText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}