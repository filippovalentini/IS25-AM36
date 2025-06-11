package it.polimi.ingsw.galaxytrucker.ui.gui.otherControllers;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.GuiController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBuildingController;
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
    private Stage controlledStage;

    private VirtualServer server;
    private int gameID;
    private String playerNickname;
    private Color color;

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


    public void addPlayer(String nickname, Color color) {
        Platform.runLater(() -> {
            playerList.getItems().add(nickname + " " + Color.convertColorIntoEmoji(color));
        });
    }

    public void startTimer(boolean firstFlight) {
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
                        switchToAssemblingPhase(firstFlight);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }));
            timeline.setCycleCount(3);
            timeline.play();
        });
    }

    public void switchToAssemblingPhase(boolean firstFlight) throws IOException {
        ShipBuildingController controller;
        Parent root;
        FXMLLoader fxmlLoader;
        if (firstFlight) {
            fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/shipBuildingL1.fxml"));
        }else{
            fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/shipBuildingL2.fxml"));
        }
        root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        GuiInterface.getInstance().setShipBuildingController(controller);
        controller.setServer(this.server);
        controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);
        controller.setControlledStage(controlledStage);
        Scene scene = new Scene(root, 1210, 740);
        controlledStage.setScene(scene);
        controlledStage.show();
    }


    @Override
    public void setControlledStage(Stage stage) {
        this.controlledStage = stage;
    }

    public void setServer(VirtualServer server) {
        this.server = server;
    }


    //invoked to set the players information needed for method invocation on server
    @Override
    public void setPlayerInfo(int gameID, String playerNickname, Color color){
        this.playerNickname = playerNickname;
        this.color = color;
        this.gameID = gameID;
    }

    @Override
    public void notifyError(String error) {}

    @Override
    public void notifyGamePhase(String gamePhase) throws Exception {

    }

}