package it.polimi.ingsw.galaxytrucker.ui.gui.shipBoardControllers;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.FlightPhaseController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBoardController;
import it.polimi.ingsw.galaxytrucker.ui.gui.flightBoardControllers.FlightBoardControllerL2;
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

public class FlightPhaseControllerL2 implements FlightPhaseController {
    private Stage controlledStage;

    @FXML private Label playerNameLabel;
    @FXML private Label playerColorLabel;
    @FXML private Label playerCreditsLabel;
    @FXML private Label lostComponentsLabel;

    @FXML private Pane errorPane;
    @FXML private Label errorLabel;

    @FXML private Rectangle gameStateBackground;
    @FXML private Label gameStateLabel;

    @FXML private GridPane myGridPane;

    @FXML private Button player1ShipButton;
    @FXML private Button player2ShipButton;
    @FXML private Button player3ShipButton;
    @FXML private Button pickCardButton;
    @FXML private Button destroyButton;
    @FXML private Button flightBoardButton;

    @FXML private Pane pickedCardArea;
    @FXML private Button pickedCardButton;

    @FXML private Button diceButton;
    @FXML private Pane dice1Pane;
    @FXML private Pane dice2Pane;
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
        setPickedCardImage("9002");
    }

    public void setDice(int dice1result, int dice2result) {
        setDiceImage(dice1Button, dice1result);
        setDiceImage(dice2Button, dice2result);
    }

    public void initializeCurrentCard(){
        int imageID = GuiInterface.getInstance().getView().getCurrentCard();
        setPickedCardImage(String.valueOf(imageID));
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

    public void initializeGameInfo() {
        this.playerNickname = GuiInterface.getInstance().getView().getNickname();
        this.playerColor = GuiInterface.getInstance().getView().getColor();
        this.lostComponents = GuiInterface.getInstance().getView().getLostComponents(playerNickname);
        this.credits = GuiInterface.getInstance().getView().getCredits(playerNickname);
        gameStateLabel.setText(GuiInterface.getInstance().getView().getGameState());
        playerNameLabel.setText(playerNickname);
        playerColorLabel.setText(Color.convertColorIntoEmoji(playerColor));
        lostComponentsLabel.setText(String.valueOf(lostComponents));
        playerCreditsLabel.setText(String.valueOf(credits));
        turnPlayerLabel.setText(GuiInterface.getInstance().getView().getTurnPlayer());
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
                if(component != null){
                    setImageOnGrid(component.getImageID(), component.getOrientation(), j, i);
                    if(component.getBatteries() > 0){
                        addBatteries(i,j, component.getBatteries());
                    }else if(component.getCrew()>0){
                        addCrewMembers(i,j);
                    }else if(component.isPurpleAlien()){
                        addAlien(i,j,true);
                    }else if(component.isBrownAlien()){
                        addAlien(i,j,false);
                    }
                }
            }
        }
    }

    public void setImageOnGrid(String imageID, Orientation orientation, int column, int row) {
        if (imageID.equals("000") || imageID.equals("003")) {
            return;
        }

        Image image = componentImageMap.get(imageID);

        double cellSize = 90;

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

            destroyButton.setDisable(false);
        });
    }


    @FXML
    private void setupOtherPlayerButton(Button button) {
        button.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/shipBoardL2.fxml"));
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
                e.printStackTrace();
            }
        });
    }

    public void addCrewMembers(int row, int column) {
        Platform.runLater(() -> {
            // Trova la cella corretta
            for (Node node : myGridPane.getChildren()) {
                Integer col = GridPane.getColumnIndex(node);
                Integer rw = GridPane.getRowIndex(node);
                if (col == null) col = 0;
                if (rw == null) rw = 0;

                if (col == column && rw == row && node instanceof StackPane cell) {
                    for (Node child : cell.getChildren()) {
                        if (child instanceof GridPane overlay && overlay.getId() != null &&
                                overlay.getId().equals("overlay-" + column + "-" + row)) {

                            overlay.add(getCrewMemberImageView(overlay), 0, 0); // top-left
                            overlay.add(getCrewMemberImageView(overlay), 1, 0); // top-right

                            return;
                        }
                    }
                }
            }
        });
    }

    public void addBatteries(int row, int column, int batteries) {
        Platform.runLater(() -> {
            // Trova la cella corretta
            for (Node node : myGridPane.getChildren()) {
                Integer col = GridPane.getColumnIndex(node);
                Integer rw = GridPane.getRowIndex(node);
                if (col == null) col = 0;
                if (rw == null) rw = 0;

                if (col == column && rw == row && node instanceof StackPane cell) {
                    for (Node child : cell.getChildren()) {
                        if (child instanceof GridPane overlay && overlay.getId() != null &&
                                overlay.getId().equals("overlay-" + column + "-" + row)) {

                            overlay.add(getBatteryImageView(overlay), 0, 0);
                            overlay.add(getBatteryImageView(overlay), 1, 0);
                            if(batteries == 3){
                                overlay.add(getBatteryImageView(overlay), 0, 1);
                            }

                            return;
                        }
                    }
                }
            }
        });
    }

    public void addAlien(int row, int column, boolean isPurple) {
        Platform.runLater(() -> {
            // Trova la cella corretta
            for (Node node : myGridPane.getChildren()) {
                Integer col = GridPane.getColumnIndex(node);
                Integer rw = GridPane.getRowIndex(node);
                if (col == null) col = 0;
                if (rw == null) rw = 0;

                if (col == column && rw == row && node instanceof StackPane cell) {
                    for (Node child : cell.getChildren()) {
                        if (child instanceof GridPane overlay && overlay.getId() != null &&
                                overlay.getId().equals("overlay-" + column + "-" + row)) {

                            overlay.add(getAlienImageView(overlay, isPurple), 0, 0);
                            return;
                        }
                    }
                }
            }
        });
    }

    public ImageView getCrewMemberImageView(GridPane overlay){
        Image crewMember = new Image(getClass().getResource("/it/polimi/ingsw/galaxytrucker/images/pieces/crewMember.png").toExternalForm());

        ImageView crewMemberImageView = new ImageView(crewMember);
        crewMemberImageView.setFitWidth(overlay.getPrefWidth() / 2);
        crewMemberImageView.setFitHeight(overlay.getPrefHeight() / 2);
        crewMemberImageView.setPreserveRatio(true);
        crewMemberImageView.setId("crew");

        return crewMemberImageView;
    }

    public ImageView getBatteryImageView(GridPane overlay){
        Image battery = new Image(getClass().getResource("/it/polimi/ingsw/galaxytrucker/images/pieces/battery.png").toExternalForm());

        ImageView batteryImageView = new ImageView(battery);
        batteryImageView.setFitWidth(overlay.getPrefWidth() / 2);
        batteryImageView.setFitHeight(overlay.getPrefHeight() / 2);
        batteryImageView.setPreserveRatio(true);
        batteryImageView.setId("crew");

        return batteryImageView;
    }

    public ImageView getAlienImageView(GridPane overlay, boolean isPurple){
        Image alien;
        if (isPurple) {
            alien = new Image(getClass().getResource("/it/polimi/ingsw/galaxytrucker/images/pieces/purpleAlien.png").toExternalForm());
        }
        else {
            alien = new Image(getClass().getResource("/it/polimi/ingsw/galaxytrucker/images/pieces/brownAlien.png").toExternalForm());
        }

        ImageView alienImageView = new ImageView(alien);
        alienImageView.setFitWidth(overlay.getPrefWidth() / 2);
        alienImageView.setFitHeight(overlay.getPrefHeight() / 2);
        alienImageView.setPreserveRatio(true);
        alienImageView.setId("crew");

        return alienImageView;
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
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/flightBoardL2.fxml"));
                Parent root = fxmlLoader.load();

                FlightBoardControllerL2 controller = fxmlLoader.getController();
                controller.setServer(this.server);
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.playerColor);
                GuiInterface.getInstance().setFlightBoardController(controller);

                controller.setControlledStage(controlledStage);
                controlledStage.setScene(new Scene(root, 1210, 740));
                controlledStage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Errore nel caricamento del FlightBoard: " + e.getMessage());
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
            GuiInterface.getInstance().getView().updateRollDice();
            int result1 = GuiInterface.getInstance().getView().dice1result();
            int result2 = GuiInterface.getInstance().getView().dice2result();
            setDice(result1, result2);
        });
    }

    public void setHitShipButton(Button button) {
        this.hitShipButton = button;
    }

    public void setPlanetLandingButton(Button button) {
        this.planetLandingButton = button;
    }

    public void setCrewLandingButton(Button button) {
        this.crewLandingButton = button;
    }

    public void setDefeatEnemyButton(Button button) {
        this.defeatEnemyButton = button;
    }

    public void setLoadGoodsButton(Button button) {
        this.loadGoodsButton = button;
    }

    public void setSwitchGoodsButton(Button button) {
        this.switchGoodsButton = button;
    }

    public void setUseBatteriesButton(Button button) {
        this.useBatteriesButton = button;
    }

    public void setFlyButton(Button button) {
        this.flyButton = button;
    }

    public void setSkipButton(Button button) {
        this.skipButton = button;
    }

    public void setQuitButton(Button button) {
        this.quitButton = button;
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
                    lostComponents++;
                    lostComponentsLabel.setText(String.valueOf(lostComponents));
                });
            }
        });
    }

    @Override
    public void updateCrewChange(String nickname, int x, int y, int change) throws Exception {

    }

    @Override
    public void updateBatteries(String nickname, int x, int y, int change) throws Exception {

    }

    @Override
    public void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) throws Exception {

    }

    @Override
    public void updateLoadedGood(String nickname, int x, int y, Color good) throws Exception {

    }

    @Override
    public void updateRemovedGoods(String nickname, int x, int y, Color good, int numberGoods) throws Exception {

    }

    @Override
    public void updateCardPicking() throws Exception {
        Platform.runLater(() -> {
            showGamePhase("CARD PICKING");
        });
    }

    @Override
    public void updateNextTurn(String nickname) throws Exception {
        Platform.runLater(() -> {
            turnPlayerLabel.setText(nickname);
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

    }
}
