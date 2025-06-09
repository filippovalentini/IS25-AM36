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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipBoardControllerL1 implements ShipBoardController {
    private Stage controlledStage;

    @FXML
    private Label errorLabel;
    @FXML
    private Rectangle errorBackground;
    @FXML
    private Rectangle gameStateBackground;
    @FXML
    private Label gameStateLabel;
    @FXML
    private Label playerNicknameLabel;
    @FXML
    private Label playerColorLabel;
    @FXML
    private GridPane myGridPane;
    @FXML
    private Button backButton;

    // Modificato: non più final e inizializzato diversamente
    private String shipBoardPlayerNickname;
    private Color shipBoardcolor;

    private int gameID;
    private String playerNickname;
    private Color color;
    private VirtualServer server;
    private Map<String, Image> componentImageMap = new HashMap<>();

    // Costruttore di default per FXML
    public ShipBoardControllerL1() {
        // Costruttore vuoto per FXML
    }

    // Metodo per impostare il giocatore da visualizzare
    public void setShipBoardPlayer(String otherPlayerNickname) {
        this.shipBoardPlayerNickname = otherPlayerNickname;

        // Ottieni il colore dal GuiInterface
        Map<String, Color> playerColorMap = GuiInterface.getInstance().getView().getCurrentPlayers();
        if (playerColorMap != null && playerColorMap.containsKey(otherPlayerNickname)) {
            this.shipBoardcolor = playerColorMap.get(otherPlayerNickname);
        } else {
            System.err.println("Colore non trovato per il giocatore: " + otherPlayerNickname);
        }

        // Aggiorna l'interfaccia
        updatePlayerInfo();
    }

    @FXML
    private void initialize() {
        System.out.println("Inizializzando ShipBoardController...");

        componentImageMap = GuiInterface.getInstance().loadImageMap("components");
        showGameState(GuiInterface.getInstance().getView().getGameState());
        setupBackButton();

        // Se il giocatore è già impostato, inizializza i componenti
        if (shipBoardPlayerNickname != null) {
            updatePlayerInfo();
            initializeAssembledComponents();
        }
    }

    private void updatePlayerInfo() {
        if (playerNicknameLabel != null && shipBoardPlayerNickname != null) {
            playerNicknameLabel.setText(shipBoardPlayerNickname);
        }
        if (playerColorLabel != null && shipBoardcolor != null) {
            playerColorLabel.setText(Color.convertColorIntoEmoji(shipBoardcolor));
        }
    }

    public void showGameState(String message){
        if (gameStateLabel != null) {
            gameStateLabel.setText(message);
        }
    }

    public void initializeAssembledComponents() {
        if (shipBoardPlayerNickname == null) {
            System.err.println("shipBoardPlayerNickname è null, impossibile inizializzare i componenti");
            return;
        }

        try {
            List<List<ViewComponent>> assembledComponents = GuiInterface.getInstance().getView().getAssembledComponents(this.shipBoardPlayerNickname);

            if (assembledComponents == null) {
                System.err.println("Nessun componente assemblato trovato per: " + shipBoardPlayerNickname);
                return;
            }

            System.out.println("Caricando componenti per " + shipBoardPlayerNickname + ": " + assembledComponents.size() + " righe");

            for(int i = 0; i < assembledComponents.size(); i++){
                for(int j = 0; j < assembledComponents.get(i).size(); j++){
                    ViewComponent component = assembledComponents.get(i).get(j);
                    if(component != null){
                        System.out.println("Posizionando componente " + component.getImageID() + " in posizione (" + j + ", " + i + ")");
                        setImageOnGrid(component.getImageID(), component.getOrientation(), j, i);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Errore nell'inizializzazione dei componenti: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setImageOnGrid(String imageID, Orientation orientation, int col, int row) {
        if(imageID.equals("000") || imageID.equals("003")){
            return;
        }

        try {
            Image image = componentImageMap.get(imageID);
            if (image == null) {
                System.err.println("Immagine non trovata per ID: " + imageID);
                return;
            }

            ImageView imageView = new ImageView(image);

            // Verifica che le constraints esistano
            if (myGridPane.getColumnConstraints().size() <= col || myGridPane.getRowConstraints().size() <= row) {
                System.err.println("Posizione non valida: (" + col + ", " + row + ")");
                return;
            }

            double cellWidth = myGridPane.getColumnConstraints().get(col).getPrefWidth();
            double cellHeight = myGridPane.getRowConstraints().get(row).getPrefHeight();

            imageView.setFitWidth(cellWidth);
            imageView.setFitHeight(cellHeight);
            imageView.setPreserveRatio(false);

            // Applica rotazione
            if(orientation.equals(Orientation.WEST)){
                imageView.setRotate((imageView.getRotate() - 90) % 360);
            }
            else if(orientation.equals(Orientation.SOUTH)){
                imageView.setRotate((imageView.getRotate() - 180) % 360);
            }
            else if(orientation.equals(Orientation.EAST)){
                imageView.setRotate((imageView.getRotate() - 270) % 360);
            }

            myGridPane.add(imageView, col, row);
            System.out.println("Componente " + imageID + " aggiunto alla griglia in posizione (" + col + ", " + row + ")");

        } catch (Exception e) {
            System.err.println("Errore nel posizionamento del componente: " + e.getMessage());
            e.printStackTrace();
        }
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

                controller.setControlledStage(controlledStage);
                Scene scene = new Scene(root, 1210, 740);
                controlledStage.setScene(scene);
                controlledStage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Errore nel caricamento della Shipboard: " + e.getMessage());
            }
        });
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

    @Override
    public void setControlledStage(Stage stage) {
        controlledStage = stage;
    }

    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }

    @Override
    public void setPlayerInfo(int gameID, String playerNickname, Color color) {
        this.gameID = gameID;
        this.playerNickname = playerNickname;
        this.color = color;
    }

    @Override
    public void updateReservedComponent(String nickname, int imageID, boolean released) throws Exception {}

    @Override
    public void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws Exception {
        if(!nickname.equals(shipBoardPlayerNickname)){
            return;
        }
        Platform.runLater(() -> {
            setImageOnGrid(String.valueOf(imageID), orientation, y, x);
        });
    }

    @Override
    public void updateShipControl() throws Exception {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/shipControlL1.fxml"));
                Parent root = loader.load();

                ShipControlControllerL1 controller = loader.getController();
                controller.setServer(this.server);
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);
                GuiInterface.getInstance().setShipControlController(controller);

                controller.setControlledStage(controlledStage);
                controlledStage.setScene(new Scene(root, 1210, 740));
                controlledStage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Errore nel caricamento del FlightBoard: " + e.getMessage());
            }
        });
    }

    @Override
    public void notifyError(String error) {
        Platform.runLater(() -> showError(error));
    }

    @Override
    public void notifyGamePhase(String gamePhase) {
        Platform.runLater(() -> {
            showGameState(gamePhase);
        });
    }
}