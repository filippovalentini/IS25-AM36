package it.polimi.ingsw.galaxytrucker.ui.gui;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.GuiController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBoardController;
import it.polimi.ingsw.galaxytrucker.ui.view.ViewComponent;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

public class ShipBoardControllerL2 implements ShipBoardController {
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
    private GridPane reservedGridPane;
    @FXML
    private Button backButton;

    private final String shipBoardPlayerNickname;
    private final Color shipBoardcolor;

    private int gameID;
    private String playerNickname;
    private Color color;
    private VirtualServer server;
    private Map<String, Image> componentImageMap = new HashMap<>();

    public ShipBoardControllerL2(String otherPlayerNickname) {
        this.shipBoardPlayerNickname = otherPlayerNickname;
        this.shipBoardcolor = GuiInterface.getInstance().getView().getCurrentPlayers().get(otherPlayerNickname);
    }

    @FXML
    private void initialize() {
        playerNicknameLabel.setText(shipBoardPlayerNickname);
        playerColorLabel.setText(Color.convertColorIntoEmoji(shipBoardcolor));
        componentImageMap = GuiInterface.getInstance().loadImageMap("components");
        showGameState(GuiInterface.getInstance().getView().getGameState());
        setupBackButton();
        initializeAssembledComponents();
        initializeReservedComponents();
    }

    public void showGameState(String message){
        gameStateLabel.setText(message);
    }

    public void initializeAssembledComponents() {
        List<List<ViewComponent>> assembledComponents = GuiInterface.getInstance().getView().getAssembledComponents(this.shipBoardPlayerNickname);
        for(int i = 0; i < assembledComponents.size(); i++){
            for(int j = 0; j < assembledComponents.get(i).size(); j++){
                ViewComponent component = assembledComponents.get(i).get(j);
                if(component != null){
                    setImageOnGrid(component.getImageID(), component.getOrientation(), j, i);
                }
            }
        }
    }

    public void initializeReservedComponents() {
        List<ViewComponent> reservedComponents = GuiInterface.getInstance().getView().getReservedComponents(this.shipBoardPlayerNickname);
        if(!reservedComponents.isEmpty()){
            setReservedComponent(componentImageMap.get(String.valueOf(reservedComponents.get(0).getImageID())), 0);
            if(reservedComponents.size() > 1){
                setReservedComponent(componentImageMap.get(String.valueOf(reservedComponents.get(1).getImageID())), 1);
            }else{
                setReservedComponent(componentImageMap.get("3"), 1);
            }
        }else{
            setReservedComponent(componentImageMap.get("3"), 0);
            setReservedComponent(componentImageMap.get("3"), 1);
        }
    }

    public void setReservedComponent(Image image, int position) {
        ImageView imageView = new ImageView(image);

        double cellWidth = reservedGridPane.getColumnConstraints().get(0).getPrefWidth();
        double cellHeight = reservedGridPane.getRowConstraints().get(position).getPrefHeight();

        imageView.setFitWidth(cellHeight);
        imageView.setFitHeight(cellHeight);
        imageView.setPreserveRatio(false);

        reservedGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == position && GridPane.getColumnIndex(node) == 0);
        reservedGridPane.add(imageView, 0, position);
    }

    public void setImageOnGrid(String imageID, Orientation orientation, int col, int row) {
        if(imageID.equals("000") || imageID.equals("003")){
            return;
        }

        Image image = componentImageMap.get(imageID);
        ImageView imageView = new ImageView(image);

        double cellWidth = myGridPane.getColumnConstraints().get(col).getPrefWidth();
        double cellHeight = myGridPane.getRowConstraints().get(row).getPrefHeight();

        imageView.setFitWidth(cellWidth);
        imageView.setFitHeight(cellHeight);
        imageView.setPreserveRatio(false);
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
    }

    public Image getReservedComponentImage(int position) {
        for (Node node : reservedGridPane.getChildren()) {
            Integer columnIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);

            if (columnIndex == null) columnIndex = 0;
            if (rowIndex == null) rowIndex = 0;

            if (columnIndex == 0 && rowIndex == position && node instanceof ImageView) {
                return ((ImageView) node).getImage();
            }
        }
        return null;
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/shipBuildingL2.fxml"));
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
            System.err.println("Errore nel caricamento della Shipboard: " + e.getMessage());
        }
    }

    public void goBackToShipControl(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/shipControlL2.fxml"));
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
    public void updateReservedComponent(String nickname, int imageID, boolean released) throws Exception {
        if(!nickname.equals(shipBoardPlayerNickname)){
            return;
        }
        Platform.runLater(() -> {
            if(released){
                if(getReservedComponentImage(0).equals(componentImageMap.get("3"))){
                    setReservedComponent(componentImageMap.get(String.valueOf(imageID)), 0);
                }
                else{
                    setReservedComponent(componentImageMap.get(String.valueOf(imageID)), 1);
                }
            }
            else{
                if(getReservedComponentImage(1).equals(componentImageMap.get(String.valueOf(imageID)))){
                    setReservedComponent(componentImageMap.get("3"), 1);
                }
                else{
                    setReservedComponent(getReservedComponentImage(1), 0);
                    setReservedComponent(componentImageMap.get("3"), 1);
                }
            }
        });
    }

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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/shipControlL2.fxml"));
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
    public void notifyError(String error) {
        Platform.runLater(() -> showError(error));
    }

    //notifies the view about a change in the game phase
    @Override
    public void notifyGamePhase(String gamePhase) {
        Platform.runLater(() -> {
            showGameState(gamePhase);
        });
    }
}
