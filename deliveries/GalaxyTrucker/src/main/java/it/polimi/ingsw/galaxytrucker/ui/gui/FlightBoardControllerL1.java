package it.polimi.ingsw.galaxytrucker.ui.gui;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.FlightBoardController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.GuiController;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlightBoardControllerL1 implements FlightBoardController {
    @FXML private Label start;
    @FXML private Label pos0, pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9;
    @FXML private Label pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17;
    @FXML private Button backButton;
    @FXML private Rectangle errorBackground;
    @FXML private Label errorLabel;

    int gameID;
    private String playerNickname;
    Color color;
    private List<Label> targetLabels;
    private VirtualServer server;
    private Map<Color, Integer> colorCellMap;
    private Map<String, Color> playerColorMap;


    @FXML
    public void initialize() {
        setupBackButton();

        targetLabels = List.of(
                pos0, pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8,
                pos9, pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17
        );

        start.setOnDragDetected(event -> {
            Dragboard db = start.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("🚀");
            db.setContent(content);

            InputStream imgStream = getClass().getResourceAsStream("/it/polimi/ingsw/galaxytrucker/images/spaceShip.png");
            Image rocketImage = new Image(imgStream);
            db.setDragView(rocketImage, rocketImage.getWidth() / 2, rocketImage.getHeight() / 2);

            event.consume();
        });

        for (Label label : targetLabels) {
            enableDropOn(label);
        }

        colorCellMap = GuiInterface.getInstance().getView().getColorCellMap();
        playerColorMap = GuiInterface.getInstance().getView().getPlayerColorMap();
        this.playerNickname = GuiInterface.getInstance().getView().getNickname();
        this.color = GuiInterface.getInstance().getView().getColor();

        initializeFlightBoardFromMap();
    }

    public void showError(String message) {
        Platform.runLater(() -> {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
            errorBackground.setVisible(true);

            // Fade in
            FadeTransition fadeInLabel = new FadeTransition(Duration.millis(300), errorLabel);
            fadeInLabel.setFromValue(0.0);
            fadeInLabel.setToValue(1.0);

            FadeTransition fadeInRect = new FadeTransition(Duration.millis(300), errorBackground);
            fadeInRect.setFromValue(0.0);
            fadeInRect.setToValue(1.0);

            fadeInLabel.play();
            fadeInRect.play();

            // Wait 3 seconds, then fade out
            fadeInLabel.setOnFinished(event -> {
                PauseTransition wait = new PauseTransition(Duration.seconds(3));
                wait.setOnFinished(e -> {
                    FadeTransition fadeOutLabel = new FadeTransition(Duration.millis(600), errorLabel);
                    fadeOutLabel.setFromValue(1.0);
                    fadeOutLabel.setToValue(0.0);

                    FadeTransition fadeOutRect = new FadeTransition(Duration.millis(600), errorBackground);
                    fadeOutRect.setFromValue(1.0);
                    fadeOutRect.setToValue(0.0);

                    fadeOutLabel.setOnFinished(ev -> {
                        errorLabel.setVisible(false);
                        errorBackground.setVisible(false);
                    });

                    fadeOutLabel.play();
                    fadeOutRect.play();
                });
                wait.play();
            });
        });
    }

    public void initializeFlightBoardFromMap() {
        for (Label label : targetLabels) {
            label.setText(""); // Pulisce le posizioni
        }

        boolean playerAlreadyPlaced = false;

        for (Map.Entry<Color, Integer> entry : colorCellMap.entrySet()) {
            Color playerColor = entry.getKey();
            Integer position = entry.getValue();

            if (position != null && position >= 0 && position < targetLabels.size()) {
                Label targetLabel = targetLabels.get(position);
                targetLabel.setText(Color.convertColorIntoEmoji(playerColor));

                if (playerColor.equals(this.color)) {
                    playerAlreadyPlaced = true;
                }
            }
        }

        // Se il giocatore non ha ancora piazzato, mostra 🚀 nella start
        if (!playerAlreadyPlaced) {
            start.setText("🚀");
        } else {
            start.setText("");
            start.setOnDragDetected(null); // disattiva drag
        }
    }

    private void enableDropOn(Label label) {
        label.setOnDragOver(event -> {
            if (event.getGestureSource() != label && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        label.setOnDragDropped(event -> {
            try {
                int pos = targetLabels.indexOf(label);
                server.setPosition(this.gameID, this.playerNickname, pos);
            } catch (Exception e) {
                showError(e.getMessage());
            }
            event.setDropCompleted(true);
            event.consume();
        });

        label.setOnDragEntered(event -> {
            if (event.getGestureSource() != label && event.getDragboard().hasString()) {
                label.setStyle("-fx-border-color: white; -fx-border-width: 2px;");
            }
        });

        label.setOnDragExited(event -> label.setStyle(""));
    }

    public void setupBackButton() {
        backButton.setOnAction(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/shipBuildingL1.fxml"));
                Parent root = fxmlLoader.load();

                ShipBuildingControllerL1 controller = fxmlLoader.getController();
                controller.setServer(this.server);
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);
                GuiInterface.getInstance().setShipBuildingController(controller);

                Stage stage = (Stage) backButton.getScene().getWindow();
                Scene scene = new Scene(root, 1210, 740);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Errore nel caricamento della Shipboard: " + e.getMessage());
            }
        });
    }

    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }

    @Override
    public void setPlayerInfo(int gameID, String playerNickname, Color color){
        this.playerNickname = playerNickname;
        this.color = color;
        this.gameID = gameID;
    }

    @Override
    public void notifyError(String errorMessage) {
        Platform.runLater(() -> {
            showError(errorMessage);

            if (start.getText().isEmpty()) {
                start.setText("🚀");
            }
        });
    }

    @Override
    public void updatePickedDeck(List<Integer> deckIDs) {}

    @Override
    public void updateReleasedDeck() {}

    @Override
    public void updateFinishAssembling(String nickname, int position) {
        Platform.runLater(() -> {
            Color playerColor = playerColorMap.get(nickname);
            if (playerColor == null || position < 0 || position >= targetLabels.size()) return;

            String emoji = Color.convertColorIntoEmoji(playerColor);
            Label targetLabel = targetLabels.get(position);
            targetLabel.setText(emoji);

            if(playerNickname.equals(nickname)){
                start.setText("");
                start.setOnDragDetected(null);
            }
        });
    }

    @Override
    public void updateStartNewCycle() {}

    @Override
    public void updateFinishedCycle() {}

}