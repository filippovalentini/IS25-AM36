package it.polimi.ingsw.galaxytrucker.ui.gui;

import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class EndgameController {
    private VirtualServer server;

    @FXML
    private Button quitButton;

    @FXML
    private Button findAnotherGameButton;

    public void setServer(VirtualServer server) {
        this.server = server;
    }

    @FXML
    private void onFindAnotherGameClick(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/selectNetwork.fxml"));
            Parent root = fxmlLoader.load();
            GameSetupController controller = fxmlLoader.getController();
            Stage stage = (Stage) findAnotherGameButton.getScene().getWindow();
            Scene scene = new Scene(root, 1210, 740);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onQuitButtonClick(){
        Platform.exit();
    }
}
