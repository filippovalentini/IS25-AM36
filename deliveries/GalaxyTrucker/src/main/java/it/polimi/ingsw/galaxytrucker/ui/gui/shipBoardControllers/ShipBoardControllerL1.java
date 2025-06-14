package it.polimi.ingsw.galaxytrucker.ui.gui.shipBoardControllers;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
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
    private Label playerCreditsLabel;
    @FXML
    private Label lostComponentsLabel;
    @FXML
    private Label statusLabel;
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

    private final String shipBoardPlayerNickname;
    private final Color shipBoardcolor;

    private int gameID;
    private String playerNickname;
    private Color color;
    private int credits;
    private int lostComponents;
    private VirtualServer server;
    private Map<String, Image> componentImageMap = new HashMap<>();

    public ShipBoardControllerL1(String otherPlayerNickname) {
        this.shipBoardPlayerNickname = otherPlayerNickname;
        this.shipBoardcolor = GuiInterface.getInstance().getView().getCurrentPlayers().get(otherPlayerNickname);
    }

    @FXML
    private void initialize() {
        playerNicknameLabel.setText(shipBoardPlayerNickname);
        playerColorLabel.setText(Color.convertColorIntoEmoji(shipBoardcolor));
        credits = GuiInterface.getInstance().getView().getCredits(shipBoardPlayerNickname);
        playerCreditsLabel.setText(String.valueOf(credits));
        lostComponents = GuiInterface.getInstance().getView().getLostComponents(shipBoardPlayerNickname);
        lostComponentsLabel.setText(String.valueOf(lostComponents));
        if(GuiInterface.getInstance().getView().hasAbandoned(shipBoardPlayerNickname)){
            statusLabel.setText("ABANDONED");
            statusLabel.setStyle("-fx-text-fill: red;");
        }else{
            statusLabel.setText("IN THE GAME");
            statusLabel.setStyle("-fx-text-fill: green;");
        }
        componentImageMap = GuiInterface.getInstance().loadImageMap("components");
        showGameState(GuiInterface.getInstance().getView().getGameState());
        setupBackButton();
        initializeAssembledComponents();
    }

    public void initializeAssembledComponents() {
        List<List<ViewComponent>> assembledComponents = GuiInterface.getInstance().getView().getAssembledComponents(this.shipBoardPlayerNickname);
        for(int i = 0; i < assembledComponents.size(); i++){
            for(int j = 0; j < assembledComponents.get(i).size(); j++){
                ViewComponent component = assembledComponents.get(i).get(j);
                setComponentOnGrid(component, i, j);
            }
        }
    }

    public void setComponentOnGrid(ViewComponent component, int row, int column){
        if(component != null){
            setImageOnGrid(component.getImageID(), component.getOrientation(), column, row);
            if(component.getBatteries() > 0){
                addBatteries(row,column, component.getBatteries());
            }else if(component.getCrew()>0){
                addCrewMembers(row,column);
            }
        }
    }

    public void removeComponentFromGrid(int row, int column){
        for (Node node : myGridPane.getChildren()) {
            Integer colIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);
            if (colIndex == null) colIndex = 0;
            if (rowIndex == null) rowIndex = 0;

            if (colIndex == column && rowIndex == row) {
                myGridPane.getChildren().remove(node);
                break;
            }
        }
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

    public void showGameState(String message){
        if (gameStateLabel != null) {
            gameStateLabel.setText(message);
        }
    }

    public void setImageOnGrid(String imageID, Orientation orientation, int col, int row) {
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
        overlay.setId("overlay-" + col + "-" + row); // utile per ritrovarlo
        overlay.setHgap(2);
        overlay.setVgap(2);

        for (int i = 0; i < 2; i++) {
            overlay.getColumnConstraints().add(new ColumnConstraints(cellSize / 2));
            overlay.getRowConstraints().add(new RowConstraints(cellSize / 2));
        }

        // StackPane con Button + Overlay
        StackPane cell = new StackPane(button, overlay);
        cell.setStyle("-fx-border-color: transparent;");
        myGridPane.add(cell, col, row);
    }

    public void setupBackButton() {
        backButton.setOnAction(event -> {
            if(gameStateLabel.getText().equals("ASSEMBLING PHASE")){
                goBackToShipBuilding();
            }
            else if(gameStateLabel.getText().equals("SHIP CONTROL")){
                goBackToShipControl();
            }
        });
    }

    public void goBackToShipBuilding(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/shipBuildingL1.fxml"));
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
    }

    public void goBackToShipControl(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/shipControlL1.fxml"));
            Parent root = fxmlLoader.load();

            ShipControlControllerL1 controller = fxmlLoader.getController();
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/shipControlL1.fxml"));
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
    public void updateShipRepair(String nickname) throws Exception {
        Platform.runLater(() -> {
            showGameState("SHIP REPAIR (player " + nickname + ")");
        });
    }

    @Override
    public void updateDestroyedComponent(String nickname, int x, int y) throws Exception {
        Platform.runLater(() -> {
            if(nickname.equals(this.shipBoardPlayerNickname)) {
                Platform.runLater(() -> {
                    removeComponentFromGrid(x, y);
                    lostComponents++;
                    lostComponentsLabel.setText(String.valueOf(lostComponents));
                });
            }
        });
    }

    @Override
    public void updateComponentChange(String nickname, int x, int y) throws Exception {
        Platform.runLater(() -> {
            if(nickname.equals(this.shipBoardPlayerNickname)) {
                removeComponentFromGrid(x, y);
                ViewComponent component = GuiInterface.getInstance().getView().getAssembledComponents(nickname).get(x).get(y);
                setComponentOnGrid(component, x , y);
            }
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
    public void updatePlayerQuit(String nickname) throws Exception {
        Platform.runLater(() -> {
            if(nickname.equals(this.shipBoardPlayerNickname)) {
                statusLabel.setText("ABANDONED");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });
    }

    @Override
    public void updatePlayerCredits(String nickname, int change) throws Exception {
        Platform.runLater(() -> {
            if(nickname.equals(shipBoardPlayerNickname)){
                this.credits += change;
                playerCreditsLabel.setText(String.valueOf(credits));
            }
        });
    }

    @Override
    public void updateEndGame() throws Exception {

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