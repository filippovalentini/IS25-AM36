package it.polimi.ingsw.galaxytrucker.ui.gui;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Map;

public class LobbyController implements GuiController {
    private VirtualServer server;

    @FXML
    private ListView<String> playerList;

    @FXML
    private Label timerLabel;

    @FXML
    private Label timerTitle;

    @FXML
    private void initialize() {
        setCurrentPlayers();
        timerLabel.setVisible(false);
        timerTitle.setVisible(false);
    }

    @FXML
    public void setCurrentPlayers() {
        Map<String, Color> currentPlayers = GuiInterface.getInstance().getView().getCurrentPlayers();
        Platform.runLater(() -> {
            playerList.getItems().clear();
            for (String nickname : currentPlayers.keySet()) {
                Color color = currentPlayers.get(nickname);
                playerList.getItems().add(nickname + " " + Color.convertColorIntoEmoji(color));
            }
        });
    }

    public void setServer(VirtualServer server) {
        this.server = server;
    }

    public void addPlayer(String nickname, Color color) {
        Platform.runLater(() -> {
            playerList.getItems().add(nickname + " " + Color.convertColorIntoEmoji(color));
        });
    }

    public void startTimer() {
        Platform.runLater(() -> {
            timerTitle.setVisible(true);
            timerLabel.setVisible(true);

            int[] seconds = {3};
            timerLabel.setText(String.valueOf(seconds[0]));

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                seconds[0]--;
                if (seconds[0] > 0) {
                    timerLabel.setText(String.valueOf(seconds[0]));
                } else {
                    timerTitle.setVisible(false);
                    timerLabel.setVisible(false);
                    try {
                        switchToAssemblingPhase();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }));
            timeline.setCycleCount(3);
            timeline.play();
        });
    }

    public void switchToAssemblingPhase() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/shipBuildingL1.fxml"));
        Parent root = fxmlLoader.load();
        ShipBuildingControllerL1 controller = fxmlLoader.getController();
        GuiInterface.getInstance().setShipBuildingController(controller);
        controller.setServer(this.server);
        Stage stage = (Stage) timerTitle.getScene().getWindow();
        Scene scene = new Scene(root, 1210, 740);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void notifyError(String errorMessage) {
        // Gestione errori se necessaria
    }
}