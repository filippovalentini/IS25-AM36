package it.polimi.ingsw.galaxytrucker.ui.gui;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBoardController;
import it.polimi.ingsw.galaxytrucker.ui.view.ViewComponent;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestController {
    @FXML private Label playerNameLabel;
    @FXML private Label playerColorLabel;
    @FXML private Label notificationLabel;
    @FXML private Pane notificationPane;
    @FXML private Label errorLabel;
    @FXML private Pane errorPane;
    @FXML private Label gameStateLabel;
    @FXML private GridPane myGridPane;
    @FXML private Button player1ShipButton;
    @FXML private Button player2ShipButton;
    @FXML private Button player3ShipButton;
    @FXML private Button crewButton;
    @FXML private Button batteriesButton;
    @FXML private Button destroyButton;
    @FXML private Button flightBoardButton;
    @FXML private Button brownAlienButton;
    @FXML private Button purpleAlienButton;
    @FXML private Label lostComponentsLabel;

    private int selectedRow = -1;
    private int selectedColumn = -1;
    private ImageView lastSelectedImageView = null;

    private Map<String, Image> componentImageMap = new HashMap<>();
    int gameID;
    String playerNickname;
    Color playerColor;
    int lostComponents;

    @FXML
    public void initialize() {
        new GuiInterface();
        componentImageMap = GuiInterface.getInstance().loadImageMap("components");

        initializeGameInfo();
        initializeButtons();
        initializeAssembledComponents();

        setupDestroyButton();
        setupBrownAlienButton();
        setupPurpleAlienButton();
        setupCrewButton();
    }

    public void initializeGameInfo() {
        this.playerNickname = "fil";
        this.playerColor = Color.BLUE;
        this.lostComponents = 2;
        gameStateLabel.setText("SHIP CONTROL");
        playerNameLabel.setText(playerNickname);
        playerColorLabel.setText(Color.convertColorIntoEmoji(playerColor));
        lostComponentsLabel.setText(String.valueOf(lostComponents));
    }

    public void initializeButtons(){
        setActionButtons(true);

        List<String> otherPlayerNicknames = Arrays.asList("fil", "mike");
        int numberOtherPlayers = otherPlayerNicknames.size();
        player1ShipButton.setDisable(false);
        player1ShipButton.setText(otherPlayerNicknames.get(0));
        if(numberOtherPlayers == 1){
            player2ShipButton.setDisable(true);
            player2ShipButton.setText("no player");
            player3ShipButton.setDisable(true);
            player3ShipButton.setText("no player");
        }
        if(numberOtherPlayers == 2){
            player2ShipButton.setDisable(false);
            player2ShipButton.setText(otherPlayerNicknames.get(1));
            player3ShipButton.setDisable(true);
            player3ShipButton.setText("no player");
        }
        if(numberOtherPlayers == 3){
            player2ShipButton.setDisable(false);
            player2ShipButton.setText(otherPlayerNicknames.get(1));
            player3ShipButton.setDisable(false);
            player3ShipButton.setText(otherPlayerNicknames.get(2));
        }
    }

    public void setActionButtons(boolean disabled){
        destroyButton.setDisable(disabled);
        crewButton.setDisable(disabled);
        batteriesButton.setDisable(disabled);
        purpleAlienButton.setDisable(disabled);
        brownAlienButton.setDisable(disabled);
    }

    public void initializeAssembledComponents(){
        setImageOnGrid("301", Orientation.NORTH, 3, 2);
        setImageOnGrid("302", Orientation.NORTH, 3, 3);
        setImageOnGrid("303", Orientation.NORTH, 3, 4);
    }

    public void setImageOnGrid(String imageID, Orientation orientation, int column, int row) {
        if (imageID.equals("000") || imageID.equals("003")) {
            return;
        }

        Image image = componentImageMap.get(imageID);

        double cellSize = 110;

        // Crea ImageView del componente
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(cellSize);
        imageView.setFitHeight(cellSize);
        imageView.setPreserveRatio(true);

        // Applica rotazione
        switch (orientation) {
            case WEST -> imageView.setRotate(270);
            case SOUTH -> imageView.setRotate(180);
            case EAST -> imageView.setRotate(90);
        }

        // Crea Button trasparente con ImageView
        Button button = new Button();
        button.setPrefSize(cellSize, cellSize);
        button.setMinSize(cellSize, cellSize);
        button.setMaxSize(cellSize, cellSize);
        button.setStyle("-fx-padding: 0; -fx-background-color: transparent; -fx-border-color: transparent;");
        button.setGraphic(imageView);

        // Crea un GridPane 2x2 per overlay di sticker (faccine, alieni, ecc)
        GridPane overlay = new GridPane();
        overlay.setPrefSize(cellSize, cellSize);
        overlay.setMouseTransparent(true); // Lascia passare i click
        overlay.setPickOnBounds(false);
        overlay.setId("overlay-" + column + "-" + row); // utile per ritrovarlo
        overlay.setHgap(2);
        overlay.setVgap(2);

        for (int i = 0; i < 2; i++) {
            overlay.getColumnConstraints().add(new ColumnConstraints(cellSize / 2));
            overlay.getRowConstraints().add(new RowConstraints(cellSize / 2));
        }

        // StackPane con Button + Overlay
        StackPane cell = new StackPane(button, overlay);
        cell.setStyle("-fx-border-color: transparent;");
        myGridPane.add(cell, column, row);

        // Salvataggio per selezione
        button.setOnAction(event -> {
            if (lastSelectedImageView != null) {
                lastSelectedImageView.setEffect(null);
            }

            Glow glow = new Glow();
            glow.setLevel(0.8);
            imageView.setEffect(glow);

            selectedColumn = column;
            selectedRow = row;
            lastSelectedImageView = imageView;

            setActionButtons(false);
        });
    }

    public void showError(String message) {
        errorLabel.setText(message);
        fadeInThenOut(errorPane);
    }

    private void fadeInThenOut(Pane pane) {
        pane.setOpacity(1.0);

        // Timer: attende 3 secondi, poi parte il fade out
        PauseTransition wait = new PauseTransition(Duration.seconds(3));
        wait.setOnFinished(event -> {
            FadeTransition fade = new FadeTransition(Duration.seconds(1.5), pane);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.play();
        });
        wait.play();
    }


    public void addTwoPieces(int row, int column) {
        Platform.runLater(() -> {
            // Trova la cella corretta
            for (Node node : myGridPane.getChildren()) {
                Integer col = GridPane.getColumnIndex(node);
                Integer rw = GridPane.getRowIndex(node);
                if (col == null) col = 0;
                if (rw == null) rw = 0;

                if (col == column && rw == row && node instanceof StackPane cell) {
                    // Cerca overlay dentro la cella
                    for (Node child : cell.getChildren()) {
                        if (child instanceof GridPane overlay && overlay.getId() != null &&
                                overlay.getId().equals("overlay-" + column + "-" + row)) {

                            // Crea 2 ImageView con le faccine
                            Image face = new Image(getClass().getResource("/it/polimi/ingsw/galaxytrucker/images/pieces/brownAlien.png").toExternalForm());

                            ImageView face1 = new ImageView(face);
                            face1.setFitWidth(overlay.getPrefWidth() / 2);
                            face1.setFitHeight(overlay.getPrefHeight() / 2);
                            face1.setPreserveRatio(true);
                            face1.setId("crew");

                            ImageView face2 = new ImageView(face);
                            face2.setFitWidth(overlay.getPrefWidth() / 2);
                            face2.setFitHeight(overlay.getPrefHeight() / 2);
                            face2.setPreserveRatio(true);
                            face2.setId("crew");

                            // Posiziona le due faccine in (0,0) e (0,1)
                            overlay.add(face1, 0, 0); // top-left
                            overlay.add(face2, 1, 0); // top-right

                            return;
                        }
                    }
                }
            }
        });
    }

    @FXML
    private void setupCrewButton() {
        crewButton.setOnAction(event -> {
            addTwoPieces(selectedRow, selectedColumn);
        });
    }



    @FXML
    private void setupDestroyButton() {
        destroyButton.setOnAction(event -> {
            Platform.runLater(() -> {
                if(playerNickname.equals(this.playerNickname)) {
                    Platform.runLater(() -> {
                        for (Node node : myGridPane.getChildren()) {
                            Integer colIndex = GridPane.getColumnIndex(node);
                            Integer rowIndex = GridPane.getRowIndex(node);
                            if (colIndex == null) colIndex = 0;
                            if (rowIndex == null) rowIndex = 0;

                            if (colIndex == selectedColumn && rowIndex == selectedRow) {
                                myGridPane.getChildren().remove(node);
                                break;
                            }
                        }

                        selectedRow = -1;
                        selectedColumn = -1;

                        setActionButtons(true);

                        lostComponents++;
                        lostComponentsLabel.setText(String.valueOf(lostComponents));
                    });
                }
            });
        });
    }


    private void setupBrownAlienButton() {
        brownAlienButton.setOnAction(event -> {
            try{

            }
            catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }

    @FXML
    private void setupPurpleAlienButton() {
        purpleAlienButton.setOnAction(event -> {
            try{

            }
            catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }


}
