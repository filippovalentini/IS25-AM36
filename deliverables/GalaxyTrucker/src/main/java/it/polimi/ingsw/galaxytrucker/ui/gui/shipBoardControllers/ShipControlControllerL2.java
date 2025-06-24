package it.polimi.ingsw.galaxytrucker.ui.gui.shipBoardControllers;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBoardController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipControlController;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShipControlControllerL2 extends  ShipBoardGraphics implements ShipControlController {
    private Stage controlledStage;

    @FXML private Label playerNameLabel;
    @FXML private Label playerColorLabel;
    @FXML private Label errorLabel;
    @FXML private Pane errorPane;
    @FXML private Label gameStateLabel;
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
    VirtualServer server;

    @FXML
    public void initialize() {
        componentImageMap = GuiInterface.getInstance().loadImageMap("components");

        initializeGameInfo();
        initializeButtons();
        initializeAssembledComponents();

        setupOtherPlayerButton(player1ShipButton);
        setupOtherPlayerButton(player2ShipButton);
        setupOtherPlayerButton(player3ShipButton);
        setupDestroyButton();
        setupFlightBoardButton();
        setupBrownAlienButton();
        setupPurpleAlienButton();
        setupCrewButton();
        setupBatteriesButton();
    }

    public void initializeGameInfo() {
        this.playerNickname = GuiInterface.getInstance().getView().getNickname();
        this.playerColor = GuiInterface.getInstance().getView().getColor();
        this.lostComponents = GuiInterface.getInstance().getView().getLostComponents(playerNickname);
        gameStateLabel.setText(GuiInterface.getInstance().getView().getGameState());
        playerNameLabel.setText(playerNickname);
        playerColorLabel.setText("██");
        playerColorLabel.setStyle(Color.convertColorIntoStyle(playerColor));
        lostComponentsLabel.setText(String.valueOf(lostComponents));
    }

    public void initializeButtons(){
        setActionButtons(true);

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

    public void setActionButtons(boolean disabled){
        destroyButton.setDisable(disabled);
        crewButton.setDisable(disabled);
        batteriesButton.setDisable(disabled);
        purpleAlienButton.setDisable(disabled);
        brownAlienButton.setDisable(disabled);
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
                        addCrewMembers(i,j, 2);
                    }else if(component.isPurpleAlien()){
                        addAlien(i,j,true);
                    }else if(component.isBrownAlien()){
                        addAlien(i,j,false);
                    }
                }
            }
        }
    }

    @Override
    public void setImageOnGrid(String imageID, Orientation orientation, int column, int row) {
        if (imageID.equals("000") || imageID.equals("003")) {
            return;
        }

        Image image = componentImageMap.get(imageID);

        double cellSize = 110;

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

            setActionButtons(false);
        });
    }

    public void showError(String message) {
        errorLabel.setText(message);
        fadeInThenOut(errorPane);
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
    private void setupCrewButton() {
        crewButton.setOnAction(event -> {
            try{
                server.addCrew(this.gameID, this.playerNickname, selectedRow, selectedColumn);
            }
            catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }

    @FXML
    private void setupBatteriesButton() {
        batteriesButton.setOnAction(event -> {
            try{
                server.addBatteries(this.gameID, this.playerNickname, selectedRow, selectedColumn);
            }
            catch (Exception e) {
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

    @FXML
    private void setupBrownAlienButton() {
        brownAlienButton.setOnAction(event -> {
            try{
                server.addAlien(this.gameID, this.playerNickname, false, selectedRow, selectedColumn);
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
                server.addAlien(this.gameID, this.playerNickname, true, selectedRow, selectedColumn);
            }
            catch (Exception e) {
                showError(e.getMessage());
            }
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

                    setActionButtons(true);

                    lostComponents++;
                    lostComponentsLabel.setText(String.valueOf(lostComponents));
                });
            }
        });
    }

    @Override
    public void updateCrewChange(String nickname, int x, int y, int change) throws Exception {
        Platform.runLater(() -> {
            if(nickname.equals(this.playerNickname)) {
                addCrewMembers(x, y, 2);

                selectedRow = -1;
                selectedColumn = -1;

                setActionButtons(true);
            }
        });
    }

    @Override
    public void updateBatteries(String nickname, int x, int y, int change) throws Exception {
        Platform.runLater(() -> {
            if(nickname.equals(this.playerNickname)) {
                addBatteries(x, y, change);

                selectedRow = -1;
                selectedColumn = -1;

                setActionButtons(true);
            }
        });
    }

    @Override
    public void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) throws Exception {
        Platform.runLater(() -> {
            if(nickname.equals(this.playerNickname)) {
                addAlien(x, y, isPurple);

                selectedRow = -1;
                selectedColumn = -1;

                setActionButtons(true);
            }
        });
    }

    @Override
    public void updateCardPicking() throws Exception {
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/flightPhaseL2.fxml"));
                Parent root = fxmlLoader.load();

                FlightPhaseControllerL2 controller = fxmlLoader.getController();
                controller.setServer(this.server);
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.playerColor);
                GuiInterface.getInstance().setFlightPhaseController(controller);

                controller.setControlledStage(controlledStage);
                controlledStage.setScene(new Scene(root, 1210, 740));
                controlledStage.show();

            } catch (IOException e) {
                showError(e.getMessage());
            }
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
            gameStateLabel.setText(gamePhase);
        });
    }
}

