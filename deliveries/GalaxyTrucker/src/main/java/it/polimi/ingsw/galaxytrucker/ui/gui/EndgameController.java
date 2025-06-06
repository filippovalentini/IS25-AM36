package it.polimi.ingsw.galaxytrucker.ui.gui;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.GuiController;
import it.polimi.ingsw.galaxytrucker.ui.view.ViewPlayer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EndgameController implements GuiController {
    private VirtualServer server;
    private int gameID;
    private String playerNickname;
    private Color color;

    @FXML
    private Button quitButton;

    @FXML
    private Button findAnotherGameButton;

    @FXML
    private ListView<String> playerList;

    @FXML
    private void initialize() {
        setCurrentPlayers();
    }

    @FXML
    private void setCurrentPlayers() {
        List<String> finalRankingList = GuiInterface.getInstance().getView().getFinalRanking();
        Platform.runLater(() -> {
            playerList.getItems().setAll(finalRankingList);
        });
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

    @Override
    public void setPlayerInfo(int gameID, String playerNickname, Color color) {
        this.playerNickname = playerNickname;
        this.color = color;
        this.gameID = gameID;
    }

    public void setServer(VirtualServer server) {
        this.server = server;
    }

    @Override
    public void notifyError(String error) {}

    @Override
    public void notifyGamePhase(String gamePhase) throws Exception {

    }
}
