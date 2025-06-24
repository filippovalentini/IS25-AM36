package it.polimi.ingsw.galaxytrucker.ui.gui.shipBoardControllers;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ActionSettingsController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.FlightPhaseController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBoardController;
import it.polimi.ingsw.galaxytrucker.ui.gui.flightBoardControllers.FlightBoardControllerL2;
import it.polimi.ingsw.galaxytrucker.ui.gui.otherControllers.EndgameController;
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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlightPhaseControllerL2 extends ShipBoardGraphics implements FlightPhaseController {
    private Stage controlledStage;

    @FXML private Label playerNameLabel;
    @FXML private Label playerColorLabel;
    @FXML private Label playerCreditsLabel;
    @FXML private Label lostComponentsLabel;
    @FXML private Label statusLabel;

    @FXML private Pane errorPane;
    @FXML private Label errorLabel;

    @FXML private Label gameStateLabel;

    @FXML private GridPane myGridPane;

    @FXML private Button player1ShipButton;
    @FXML private Button player2ShipButton;
    @FXML private Button player3ShipButton;
    @FXML private Button pickCardButton;
    @FXML private Button destroyButton;
    @FXML private Button flightBoardButton;

    @FXML private Button pickedCardButton;

    @FXML private Button diceButton;
    @FXML private Button dice1Button;
    @FXML private Button dice2Button;

    @FXML private Label turnPlayerLabel;

    @FXML private Button hitShipButton;
    @FXML private Button planetLandingButton;
    @FXML private Button crewLandingButton;
    @FXML private Button defeatEnemyButton;
    @FXML private Button loadGoodsButton;
    @FXML private Button switchGoodsButton;
    @FXML private Button useBatteriesButton;
    @FXML private Button flyButton;
    @FXML private Button skipButton;
    @FXML private Button quitButton;

    @FXML private Pane popupContainer;
    private boolean popupOpened = false;

    private int selectedRow = -1;
    private int selectedColumn = -1;
    private ImageView lastSelectedImageView = null;

    private Map<String, Image> componentImageMap = new HashMap<>();
    private Map<String, Image> cardImageMap = new HashMap<>();
    private Map<String, Image> diceImageMap = new HashMap<>();
    int gameID;
    String playerNickname;
    Color playerColor;
    int lostComponents;
    int credits;
    VirtualServer server;

    @FXML
    public void initialize() {
        componentImageMap = GuiInterface.getInstance().loadImageMap("components");
        cardImageMap = GuiInterface.getInstance().loadImageMap("cards");
        diceImageMap = GuiInterface.getInstance().loadImageMap("diceFaces");

        initializeDice();
        initializeGameInfo();
        initializeButtons();
        initializeAssembledComponents();
        initializeCurrentCard();

        setupDiceButton();
        setupOtherPlayerButton(player1ShipButton);
        setupOtherPlayerButton(player2ShipButton);
        setupOtherPlayerButton(player3ShipButton);
        setupDestroyButton();
        setupFlightBoardButton();
        setupPickCardButton();
        setupQuitButton();

        popupContainer.setMouseTransparent(true);
        setupSkipButton();
        setupHitShipButton();
        setupPlanetLandingButton();
        setupCrewLandingButton();
        setupUseBatteriesButton();
        setupLoadGoodsButton();
        setupFlyButton();
        setupDefeatEnemyButton();
    }

    public void initializeDice(){
        if(!GuiInterface.getInstance().getView().throwableDice()){
            setDice();
        }
    }

    public void setDice() {
        int result1 = GuiInterface.getInstance().getView().dice1result();
        int result2 = GuiInterface.getInstance().getView().dice2result();
        setDiceImage(dice1Button, result1);
        setDiceImage(dice2Button, result2);
        diceButton.setDisable(true);
    }

    public void invalidDice(){
        diceButton.setDisable(false);
        dice1Button.setGraphic(null);
        dice2Button.setGraphic(null);
        dice1Button.setStyle("-fx-background-color: #87CEFA;");
        dice2Button.setStyle("-fx-background-color: #87CEFA;");
    }

    public void initializeCurrentCard(){
        Integer imageID = GuiInterface.getInstance().getView().getCurrentCard();
        if (imageID != null) {
            setPickedCardImage(String.valueOf(imageID));
        }else{
            setPickedCardImage("9002");
        }
    }

    public void setDiceImage(Button diceButton, int result){
        Image image = diceImageMap.get(String.valueOf(result));
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(diceButton.getPrefWidth());
        imageView.setFitHeight(diceButton.getPrefHeight());

        diceButton.setGraphic(imageView);
        diceButton.setStyle("-fx-padding: 0; -fx-background-color: transparent;");
    }

    public void setPickedCardImage(String imageID) {
        Image image = cardImageMap.get(imageID);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(pickedCardButton.getPrefWidth());
        imageView.setFitHeight(pickedCardButton.getPrefHeight());

        pickedCardButton.setGraphic(imageView);
        pickedCardButton.setStyle("-fx-padding: 0; -fx-background-color: transparent;");
    }

    public void showError(String message) {
        errorLabel.setText(message);
        fadeInThenOut(errorPane);
    }

    public void showGamePhase(String message){
        gameStateLabel.setText(message);
    }

    private void fadeInThenOut(Pane pane) {
        pane.setOpacity(1.0);

        PauseTransition wait = new PauseTransition(Duration.seconds(3));
        wait.setOnFinished(event -> {
            FadeTransition fade = new FadeTransition(Duration.seconds(1.5), pane);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.play();
        });
        wait.play();
    }

    public void initializeGameInfo() {
        this.playerNickname = GuiInterface.getInstance().getView().getNickname();
        this.playerColor = GuiInterface.getInstance().getView().getColor();
        this.lostComponents = GuiInterface.getInstance().getView().getLostComponents(playerNickname);
        this.credits = GuiInterface.getInstance().getView().getCredits(playerNickname);
        gameStateLabel.setText(GuiInterface.getInstance().getView().getGameState());
        playerNameLabel.setText(playerNickname);
        playerColorLabel.setText("██");
        playerColorLabel.setStyle(Color.convertColorIntoStyle(playerColor));
        lostComponentsLabel.setText(String.valueOf(lostComponents));
        playerCreditsLabel.setText(String.valueOf(credits));
        turnPlayerLabel.setText(GuiInterface.getInstance().getView().getTurnPlayer());
        if(GuiInterface.getInstance().getView().hasAbandoned(playerNickname)){
            statusLabel.setText("ABANDONED");
            statusLabel.setStyle("-fx-text-fill: red;");
        }else{
            statusLabel.setText("IN THE GAME");
            statusLabel.setStyle("-fx-text-fill: green;");
        }
    }

    public void initializeButtons(){
        destroyButton.setDisable(true);

        List<String> otherPlayerNicknames = GuiInterface.getInstance().getView().getOtherPlayerNicknames();
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

    public void initializeAssembledComponents(){
        List<List<ViewComponent>> assembledComponents = GuiInterface.getInstance().getView().getAssembledComponents(this.playerNickname);
        for(int i = 0; i < assembledComponents.size(); i++){
            for(int j = 0; j < assembledComponents.get(i).size(); j++){
                ViewComponent component = assembledComponents.get(i).get(j);
                setComponentOnGrid(component, i, j);
            }
        }
    }

    @Override
    public void setImageOnGrid(String imageID, Orientation orientation, int column, int row) {
        if (imageID.equals("000") || imageID.equals("003")) {
            return;
        }

        Image image = componentImageMap.get(imageID);

        double cellSize = 90;

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(cellSize);
        imageView.setFitHeight(cellSize);
        imageView.setPreserveRatio(true);

        switch (orientation) {
            case WEST -> imageView.setRotate(270);
            case SOUTH -> imageView.setRotate(180);
            case EAST -> imageView.setRotate(90);
        }

        Button button = new Button();
        button.setPrefSize(cellSize, cellSize);
        button.setMinSize(cellSize, cellSize);
        button.setMaxSize(cellSize, cellSize);
        button.setStyle("-fx-padding: 0; -fx-background-color: transparent; -fx-border-color: transparent;");
        button.setGraphic(imageView);

        GridPane overlay = new GridPane();
        overlay.setPrefSize(cellSize, cellSize);
        overlay.setMouseTransparent(true);
        overlay.setPickOnBounds(false);
        overlay.setId("overlay-" + column + "-" + row);
        overlay.setHgap(2);
        overlay.setVgap(2);

        for (int i = 0; i < 2; i++) {
            overlay.getColumnConstraints().add(new ColumnConstraints(cellSize / 2));
            overlay.getRowConstraints().add(new RowConstraints(cellSize / 2));
        }

        StackPane cell = new StackPane(button, overlay);
        cell.setStyle("-fx-border-color: transparent;");
        myGridPane.add(cell, column, row);

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

            destroyButton.setDisable(false);
        });
    }

    public void disableActionButtons(boolean disable){
        flyButton.setDisable(disable);
        useBatteriesButton.setDisable(disable);
        defeatEnemyButton.setDisable(disable);
        hitShipButton.setDisable(disable);
        loadGoodsButton.setDisable(disable);
        switchGoodsButton.setDisable(disable);
        crewLandingButton.setDisable(disable);
        planetLandingButton.setDisable(disable);
        skipButton.setDisable(disable);
    }


    @FXML
    private void setupOtherPlayerButton(Button button) {
        button.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipBoardL2.fxml"));
                ShipBoardController controller = new ShipBoardControllerL2(button.getText());
                loader.setController(controller);

                Parent root = loader.load();

                controller.setServer(this.server);
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.playerColor);
                GuiInterface.getInstance().setShipBoardController(controller);

                controller.setControlledStage(controlledStage);
                controlledStage.setScene(new Scene(root, 1210, 740));
                controlledStage.show();

            } catch (IOException e) {
                showError(e.getMessage());
            }
        });
    }

    @FXML
    private void setupDestroyButton() {
        destroyButton.setOnAction(event -> {
            try{
                server.destroyComponent(this.gameID, this.playerNickname, selectedRow, selectedColumn);
            }
            catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }

    @FXML
    private void setupFlightBoardButton() {
        flightBoardButton.setOnAction(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/flightBoardL2.fxml"));
                Parent root = fxmlLoader.load();

                FlightBoardControllerL2 controller = fxmlLoader.getController();
                controller.setServer(this.server);
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.playerColor);
                GuiInterface.getInstance().setFlightBoardController(controller);

                controller.setControlledStage(controlledStage);
                controlledStage.setScene(new Scene(root, 1210, 740));
                controlledStage.show();

            } catch (IOException e) {
                showError(e.getMessage());
            }
        });
    }


    // Setters for buttons (excluding dice and pickedCard buttons)

    @FXML
    public void setupPickCardButton() {
        pickCardButton.setOnAction(event -> {
            try{
                server.pickNextCard(this.gameID, this.playerNickname);
            }
            catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }

    @FXML
    public void setupDiceButton() {
        diceButton.setOnAction(event -> {
            try{
                GuiInterface.getInstance().getView().updateRollDice();
                setDice();
            }
            catch (Exception e) { showError(e.getMessage()); }
        });
    }

    public void showPopup(String fxml) {
        try {
            String resourcePath = "/it/polimi/ingsw/galaxytrucker/fxml/actionSettings/" + fxml;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
            Parent popupContent = loader.load();

            ActionSettingsController controller = loader.getController();
            controller.setServer(this.server);
            controller.setPlayerInfo(this.gameID, this.playerNickname);
            controller.setOnConfirm(() -> {
                GuiInterface.getInstance().getView().updateThrowableDice();
                invalidDice();
                hidePopup();
            });

            popupContainer.getChildren().clear();
            popupContainer.getChildren().add(popupContent);
            popupContainer.setVisible(true);
            popupContainer.setMouseTransparent(false);

            popupOpened = true;
            disableActionButtons(true);

        } catch (IOException e) {
            showError(e.getMessage());
        }
    }

    public void hidePopup() {
        popupContainer.setVisible(false);
        popupContainer.getChildren().clear();
        popupContainer.setMouseTransparent(true);

        popupOpened = false;
        disableActionButtons(false);
    }

    public void setupHitShipButton() {
        hitShipButton.setOnAction(event -> {
            if(!diceButton.isDisable()){
                showError("Throw the dice first");
                return;
            }
            if(!popupOpened) {
                showPopup("hitShipSettings.fxml");
                hitShipButton.setDisable(false);
            }else{
                hidePopup();
            }
        });
    }

    public void setupPlanetLandingButton() {
        planetLandingButton.setOnAction(event -> {
            if(!popupOpened) {
                showPopup("planetLandingSettings.fxml");
                planetLandingButton.setDisable(false);
            }else{
                hidePopup();
            }
        });
    }

    public void setupCrewLandingButton() {
        crewLandingButton.setOnAction(event -> {
            if(!popupOpened) {
                showPopup("crewLandingSettings.fxml");
                crewLandingButton.setDisable(false);
            }else{
                hidePopup();
            }
        });
    }

    public void setupDefeatEnemyButton() {
        defeatEnemyButton.setOnAction(event -> {
            if(!popupOpened) {
                showPopup("defeatEnemySettings.fxml");
                defeatEnemyButton.setDisable(false);
            }else{
                hidePopup();
            }
        });
    }

    public void setupLoadGoodsButton() {
        loadGoodsButton.setOnAction(event -> {
            if(!popupOpened) {
                showPopup("loadGoodsSettings.fxml");
                loadGoodsButton.setDisable(false);
            }else{
                hidePopup();
            }
        });
    }

    public void setupUseBatteriesButton() {
        useBatteriesButton.setOnAction(event -> {
            if(!popupOpened) {
                showPopup("useBatteriesSettings.fxml");
                useBatteriesButton.setDisable(false);
            }else{
                hidePopup();
            }
        });
    }

    public void setupFlyButton() {
        flyButton.setOnAction(event -> {
            if(!popupOpened) {
                showPopup("flySettings.fxml");
                flyButton.setDisable(false);
            }else{
                hidePopup();
            }
        });
    }

    public void setupSkipButton() {
        skipButton.setOnAction(event -> {
            try{
                server.skip(this.gameID, this.playerNickname);
            }
            catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }

    public void setupQuitButton() {
        quitButton.setOnAction(event -> {
            try{
                server.quitGame(this.gameID, this.playerNickname);
            }
            catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }

    @Override
    public void setControlledStage(Stage stage) {
        this.controlledStage = stage;
    }

    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }

    @Override
    public void setPlayerInfo(int gameID, String playerNickname, Color color) {
        this.gameID = gameID;
        this.playerNickname = playerNickname;
        this.playerColor = color;
    }

    @Override
    public void notifyError(String error) throws Exception {
        Platform.runLater(() -> {
            showError(error);
        });
    }

    @Override
    public void notifyGamePhase(String gamePhase) throws Exception {
        Platform.runLater(() -> {
            showGamePhase(gamePhase);
        });
    }

    @Override
    public void updateShipRepair(String nickname) throws Exception {
        Platform.runLater(() -> {
            showGamePhase("SHIP REPAIR (player " + nickname + ")");
        });
    }

    @Override
    public void updateDestroyedComponent(String nickname, int x, int y) throws Exception {
        Platform.runLater(() -> {
            if(nickname.equals(this.playerNickname)) {
                Platform.runLater(() -> {
                    removeComponentFromGrid(x, y);
                    selectedRow = -1;
                    selectedColumn = -1;
                    lostComponents++;
                    lostComponentsLabel.setText(String.valueOf(lostComponents));
                });
            }
        });
    }

    @Override
    public void updateComponentChange(String nickname, int x, int y) throws Exception {
        Platform.runLater(() -> {
            if(nickname.equals(this.playerNickname)) {
                removeComponentFromGrid(x, y);
                ViewComponent component = GuiInterface.getInstance().getView().getAssembledComponents(nickname).get(x).get(y);
                setComponentOnGrid(component, x , y);
            }
        });
    }

    @Override
    public void updateCardPicking() throws Exception {
        Platform.runLater(() -> {
            showGamePhase("CARD PICKING");
            setPickedCardImage("9002");
        });
    }

    @Override
    public void updateNextTurn(String nickname) throws Exception {
        Platform.runLater(() -> {
            turnPlayerLabel.setText(nickname);
            invalidDice();
        });
    }

    @Override
    public void updateCardSolving(int imageID) throws Exception {
        Platform.runLater(() -> {
            showGamePhase("CARD SOLVING");
            setPickedCardImage(String.valueOf(imageID));
        });
    }

    @Override
    public void updatePlayerQuit(String nickname) throws Exception {
        Platform.runLater(() -> {
            if(nickname.equals(this.playerNickname)) {
                statusLabel.setText("ABANDONED");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });
    }

    @Override
    public void updatePlayerCredits(String nickname, int change) throws Exception {
        Platform.runLater(() -> {
            if(nickname.equals(this.playerNickname)) {
                this.credits+=change;
                playerCreditsLabel.setText(String.valueOf(credits));
            }
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
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.playerColor);

                controller.setControlledStage(controlledStage);
                controlledStage.setScene(new Scene(root, 1210, 740));
                controlledStage.show();

            } catch (IOException e) {
                showError(e.getMessage());
            }
        });
    }
}
