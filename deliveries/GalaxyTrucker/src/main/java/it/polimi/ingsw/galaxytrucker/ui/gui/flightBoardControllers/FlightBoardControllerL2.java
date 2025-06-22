package it.polimi.ingsw.galaxytrucker.ui.gui.flightBoardControllers;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.otherControllers.EndgameController;
import it.polimi.ingsw.galaxytrucker.ui.gui.shipBoardControllers.FlightPhaseControllerL2;
import it.polimi.ingsw.galaxytrucker.ui.gui.shipBoardControllers.ShipBuildingControllerL2;
import it.polimi.ingsw.galaxytrucker.ui.gui.shipBoardControllers.ShipControlControllerL2;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.FlightBoardController;
import it.polimi.ingsw.galaxytrucker.ui.view.View;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

public class FlightBoardControllerL2 implements FlightBoardController {
    private Stage controlledStage;

    @FXML private Label start;
    @FXML private Label pos0, pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9;
    @FXML private Label pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17;
    @FXML private Label pos18, pos19, pos20, pos21, pos22, pos23;
    @FXML private List<Label> targetLabels;
    @FXML private Button backButton;
    @FXML private Rectangle errorBackground;
    @FXML private Label errorLabel;
    @FXML private Rectangle gameStateBackground;
    @FXML private Label gameStateLabel;
    @FXML private Button deck1Button;
    @FXML private Button deck2Button;
    @FXML private Button deck3Button;
    @FXML private Button releaseDeckButton;
    @FXML private Pane deckPreviewPane;
    @FXML private ImageView deckCard1;
    @FXML private ImageView deckCard2;
    @FXML private ImageView deckCard3;
    @FXML private Button hourglass1Button;
    @FXML private Button hourglass2Button;


    private int gameID;
    private String playerNickname;
    private Color color;
    private VirtualServer server;
    private Map<Color, Integer> colorCellMap;
    private Map<String, Color> playerColorMap;
    private Map<String, Image> cardImageMap = new HashMap<>();

    @FXML
    public void initialize() {
        setupBackButton();
        setupDeck1Button();
        setupDeck2Button();
        setupDeck3Button();
        setupReleaseDeckButton();
        setUpHourglass1Button();
        setUpHourglass2Button();

        cardImageMap = GuiInterface.getInstance().loadImageMap("cards");

        targetLabels = List.of(
                pos0, pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8,
                pos9, pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17,
                pos18, pos19, pos20, pos21, pos22, pos23
        );

        start.setOnDragDetected(event -> {
            Dragboard db = start.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("🚀");
            db.setContent(content);

            InputStream imgStream = getClass().getResourceAsStream("/it/polimi/ingsw/galaxytrucker/images/cardboard/spaceShip.png");
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
        showGameState(GuiInterface.getInstance().getView().getGameState());

        initializeHourglass();
        initializeFlightBoardFromMap();
    }

    public void showGameState(String message){
        gameStateLabel.setText(message);
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

    public void initializeHourglass(){
        View view = GuiInterface.getInstance().getView();
        if(view.isHourglassRunning() && view.getHourglassPosition()==1){
            hourglassRotation(hourglass1Button);
            hourglass2Button.setDisable(true);
            hourglass1Button.setDisable(false);
        }
        else if(view.isHourglassRunning() && view.getHourglassPosition()==2){
            hourglassRotation(hourglass2Button);
            hourglass2Button.setDisable(false);
            hourglass1Button.setDisable(true);
        }
        else if(!view.isHourglassRunning() && view.getHourglassPosition()==1){
            stopHourglass(hourglass1Button, false);
            hourglass2Button.setDisable(true);
            hourglass1Button.setDisable(false);
        }
        else if(!view.isHourglassRunning() && view.getHourglassPosition()==2){
            stopHourglass(hourglass2Button, true);
            hourglass2Button.setDisable(false);
            hourglass1Button.setDisable(true);
        }
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
                setPosition(playerColor, position);

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
            deck1Button.setDisable(true);
            deck2Button.setDisable(true);
            deck3Button.setDisable(true);
            releaseDeckButton.setDisable(true);
        }
    }

    public void setPosition(Color color, int cell) {
        Label targetLabel = targetLabels.get(cell);
        targetLabel.setText(Color.convertColorIntoEmoji(color));
        colorCellMap.put(color, cell);
    }

    public void freePosition(Color color) {
        int cell = colorCellMap.get(color);
        Label targetLabel = targetLabels.get(cell);
        targetLabel.setText("");
        colorCellMap.remove(color);
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
            if(gameStateLabel.getText().equals("ASSEMBLING PHASE")){
                goBackToShipBuilding();
            }
            else if(gameStateLabel.getText().equals("SHIP CONTROL")){
                goBackToShipControl();
            }
            else if(gameStateLabel.getText().equals("CARD PICKING") || gameStateLabel.getText().equals("CARD SOLVING")){
                goBackToFlightPhase();
            }
            else  if (start.getText().isEmpty()){
                showError("Patience, hero");
            }
        });
    }

    public void goBackToShipBuilding(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipBuildingL2.fxml"));
            Parent root = fxmlLoader.load();

            ShipBuildingControllerL2 controller = fxmlLoader.getController();
            controller.setServer(this.server);
            controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);
            GuiInterface.getInstance().setShipBuildingController(controller);

            controller.setControlledStage(controlledStage);
            Scene scene = new Scene(root, 1210, 740);
            controlledStage.setScene(scene);
            controlledStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goBackToShipControl(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipControlL2.fxml"));
            Parent root = fxmlLoader.load();

            ShipControlControllerL2 controller = fxmlLoader.getController();
            controller.setServer(this.server);
            controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);
            GuiInterface.getInstance().setShipControlController(controller);

            controller.setControlledStage(controlledStage);
            Scene scene = new Scene(root, 1210, 740);
            controlledStage.setScene(scene);
            controlledStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore nel caricamento della Shipboard: " + e.getMessage());
        }
    }

    public void goBackToFlightPhase(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/flightPhaseL2.fxml"));
            Parent root = fxmlLoader.load();

            FlightPhaseControllerL2 controller = fxmlLoader.getController();
            controller.setServer(this.server);
            controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);
            GuiInterface.getInstance().setFlightPhaseController(controller);

            controller.setControlledStage(controlledStage);
            controlledStage.setScene(new Scene(root, 1210, 740));
            controlledStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore nel caricamento del FlightBoard: " + e.getMessage());
        }
    }

    public void setupDeck1Button() {
        deck1Button.setOnAction(event -> {
            try{
                server.pickDeck(this.gameID, this.playerNickname, 1);
            }
            catch(Exception e){
                showError(e.getMessage());
            }
        });
    }

    public void setupDeck2Button() {
        deck2Button.setOnAction(event -> {
            try{
                server.pickDeck(this.gameID, this.playerNickname, 2);
            }
            catch(Exception e){
                showError(e.getMessage());
            }
        });
    }

    public void setupDeck3Button() {
        deck3Button.setOnAction(event -> {
            try{
                server.pickDeck(this.gameID, this.playerNickname, 3);
            }
            catch(Exception e){
                showError(e.getMessage());
            }
        });
    }

    public void setupReleaseDeckButton() {
        releaseDeckButton.setOnAction(event -> {
            try{
                server.releaseDeck(this.gameID, this.playerNickname);
            }
            catch(Exception e){
                showError(e.getMessage());
            }
        });
    }

    public void setUpHourglass1Button() {
        hourglass1Button.setOnAction(event -> {
            try{
                server.startNewCycle(this.gameID, this.playerNickname);
            }
            catch(Exception e){
                showError(e.getMessage());
            }
        });
    }

    public void setUpHourglass2Button() {
        hourglass2Button.setOnAction(event -> {
            try{
                server.startNewCycle(this.gameID, this.playerNickname);
            }
            catch(Exception e){
                showError(e.getMessage());
            }
        });
    }

    private void hourglassRotation(Button hourglassButton) {
        // Recupera l'immagine dalla mappa
        Image image = cardImageMap.get("1000");
        if (image == null) {
            showError("Immagine ID 1000 non trovata.");
            return;
        }

        // Crea ImageView e la adatta al bottone
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(hourglassButton.getPrefWidth());
        imageView.setFitHeight(hourglassButton.getPrefHeight());
        imageView.setPreserveRatio(true);

        // Applica tinta rossa
        ColorAdjust redTint = new ColorAdjust();
        redTint.setHue(0.05);
        redTint.setSaturation(1.0);
        redTint.setBrightness(0.6);
        imageView.setEffect(redTint);

        // Rotazione infinita
        javafx.animation.RotateTransition rotate = new javafx.animation.RotateTransition();
        rotate.setNode(imageView);
        rotate.setDuration(Duration.seconds(2));
        rotate.setByAngle(360);
        rotate.setCycleCount(javafx.animation.Animation.INDEFINITE);
        rotate.setInterpolator(javafx.animation.Interpolator.LINEAR);
        rotate.play();

        // Inserisce l'imageView nel bottone
        hourglassButton.setGraphic(imageView);
    }


    private void stopHourglass(Button hourglassButton, boolean lastCycle) {
        // 1. Recupera l’immagine con ID "1000" dalla mappa
        Image image = cardImageMap.get("1000");
        if (image == null) {
            showError("Immagine con ID 1000 non trovata.");
            return;
        }

        // 2. Crea una ImageView e la adatta al bottone
        ImageView hourglass = new ImageView(image);
        hourglass.setFitWidth(hourglassButton.getPrefWidth());
        hourglass.setFitHeight(hourglassButton.getPrefHeight());
        hourglass.setPreserveRatio(true);
        hourglass.setSmooth(true);

        // 3. Applica una tinta
        ColorAdjust tint = new ColorAdjust();
        if (!lastCycle) {
            tint.setHue(0.33);
            tint.setSaturation(1.0);
            tint.setBrightness(0.6);
        }else{
            tint.setHue(-0.55);
            tint.setSaturation(1.0);
            tint.setBrightness(0.6);
        }

        hourglass.setEffect(tint);

        // 4. Imposta la grafica del bottone
        hourglassButton.setGraphic(hourglass);
        hourglassButton.setStyle("-fx-background-color: transparent;");
    }

    private void clearHourglassButton(Button hourglassButton) {
        hourglassButton.setGraphic(null); // Rimuove l’immagine/graphic
        hourglassButton.setStyle("-fx-background-color: transparent;"); // Sfondo trasparente
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
    public void setPlayerInfo(int gameID, String playerNickname, Color color){
        this.playerNickname = playerNickname;
        this.color = color;
        this.gameID = gameID;
    }

    @Override
    public void notifyError(String errorMessage) {
        Platform.runLater(() -> {
            showError(errorMessage);
        });
    }

    @Override
    public void updatePickedDeck(List<Integer> deckIDs) {
        Platform.runLater(() -> {
            if (deckIDs == null || deckIDs.size() < 3) {
                showError("Numero di carte non valido.");
                return;
            }

            try {
                // Converti gli ID in stringhe, se le chiavi nella mappa sono basate sugli ID come stringa
                String key1 = String.valueOf(deckIDs.get(0));
                String key2 = String.valueOf(deckIDs.get(1));
                String key3 = String.valueOf(deckIDs.get(2));

                Image img1 = cardImageMap.get(key1);
                Image img2 = cardImageMap.get(key2);
                Image img3 = cardImageMap.get(key3);

                // Adatta le immagini alla ImageView
                if (img1 != null) {
                    deckCard1.setImage(img1);
                    deckCard1.setPreserveRatio(true);
                    deckCard1.setFitWidth(deckCard1.getFitWidth());
                    deckCard1.setFitHeight(deckCard1.getFitHeight());
                }

                if (img2 != null) {
                    deckCard2.setImage(img2);
                    deckCard2.setPreserveRatio(true);
                    deckCard2.setFitWidth(deckCard2.getFitWidth());
                    deckCard2.setFitHeight(deckCard2.getFitHeight());
                }

                if (img3 != null) {
                    deckCard3.setImage(img3);
                    deckCard3.setPreserveRatio(true);
                    deckCard3.setFitWidth(deckCard3.getFitWidth());
                    deckCard3.setFitHeight(deckCard3.getFitHeight());
                }

                deckPreviewPane.setVisible(true);

                deck1Button.setDisable(true);
                deck2Button.setDisable(true);
                deck3Button.setDisable(true);
                backButton.setDisable(true);

            } catch (Exception e) {
                showError("Errore nel caricamento delle immagini delle carte.");
            }
        });
    }

    @Override
    public void updateReleasedDeck() {
        Platform.runLater(() -> {
            deckCard1.setImage(null);
            deckCard2.setImage(null);
            deckCard3.setImage(null);
            deckPreviewPane.setVisible(false);

            deck1Button.setDisable(false);
            deck2Button.setDisable(false);
            deck3Button.setDisable(false);
            backButton.setDisable(false);
        });
    }

    @Override
    public void updateFinishAssembling(String nickname, int position) {
        Platform.runLater(() -> {
            Color playerColor = playerColorMap.get(nickname);
            if (playerColor == null || position < 0 || position >= targetLabels.size()) return;

            String emoji = Color.convertColorIntoEmoji(playerColor);
            Label targetLabel = targetLabels.get(position);
            targetLabel.setText(emoji);

            if(playerNickname.equals(nickname)){
                deck1Button.setDisable(true);
                deck2Button.setDisable(true);
                deck3Button.setDisable(true);
                releaseDeckButton.setDisable(true);
                start.setText("");
                start.setOnDragDetected(null);
            }
        });
    }

    @Override
    public void updateStartNewCycle() {
        Platform.runLater(() -> {
            if(hourglass2Button.isDisable()){
                hourglass2Button.setDisable(false);
                hourglassRotation(hourglass2Button);
                hourglass1Button.setDisable(true);
                clearHourglassButton(hourglass1Button);
            }
        });
    }

    @Override
    public void updateFinishedCycle() {
        Platform.runLater(() -> {
            if(hourglass2Button.isDisable()){
                clearHourglassButton(hourglass1Button);
                stopHourglass(hourglass1Button, false);
            }else{
                clearHourglassButton(hourglass2Button);
                stopHourglass(hourglass2Button, true);
            }
        });
    }

    @Override
    public void updateShipControl() throws Exception {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipControlL2.fxml"));
                Parent root = loader.load();

                ShipControlControllerL2 controller = loader.getController();
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
    public void updateShipRepair(String nickname) throws Exception {
        Platform.runLater(() -> {
            showGameState("SHIP REPAIR (player " + nickname + ")");
        });
    }

    @Override
    public void updateCardPicking() throws Exception {
        Platform.runLater(() -> {
            showGameState("CARD PICKING");
        });
    }

    @Override
    public void updateCardSolving(int imageID) throws Exception {
        Platform.runLater(() -> {
            showGameState("CARD SOLVING");
        });
    }

    @Override
    public void updatePlayerPosition(String nickname, int cell) throws Exception {
        Platform.runLater(() -> {
            Color playerColor = playerColorMap.get(nickname);
            freePosition(playerColor);
            setPosition(playerColor, cell);
        });
    }

    @Override
    public void updateEndGame() throws Exception {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/endgame.fxml"));
                Parent root = loader.load();

                EndgameController controller = loader.getController();
                controller.setServer(this.server);
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);

                controller.setControlledStage(controlledStage);
                controlledStage.setScene(new Scene(root, 1210, 740));
                controlledStage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Errore nel caricamento del FlightBoard: " + e.getMessage());
            }
        });
    }

    //notifies the view about a change in the game phase
    @Override
    public void notifyGamePhase(String gamePhase) {
        Platform.runLater(() -> {
            showGameState(gamePhase);
        });
    }
}