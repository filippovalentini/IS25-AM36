package it.polimi.ingsw.galaxytrucker.ui.gui.shipBoardControllers;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBoardController;
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
/** * ShipBoardControllerL1 is the controller for the ship board in Level 1.
 */
public class ShipBoardControllerL1 extends ShipBoardGraphics implements ShipBoardController {
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

    /**
     * Constructor for ShipBoardControllerL1.
     * @param otherPlayerNickname
     */
    public ShipBoardControllerL1(String otherPlayerNickname) {
        this.shipBoardPlayerNickname = otherPlayerNickname;
        this.shipBoardcolor = GuiInterface.getInstance().getView().getCurrentPlayers().get(otherPlayerNickname);
    }

    @FXML
    private void initialize() {
        playerNicknameLabel.setText(shipBoardPlayerNickname);
        playerColorLabel.setText("██");
        playerColorLabel.setStyle(Color.convertColorIntoStyle(shipBoardcolor));
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
    /**
     * Initializes the assembled components on the ship board grid.
     * It retrieves the assembled components from the view and places them on the grid.
     */
    public void initializeAssembledComponents() {
        List<List<ViewComponent>> assembledComponents = GuiInterface.getInstance().getView().getAssembledComponents(this.shipBoardPlayerNickname);
        for(int i = 0; i < assembledComponents.size(); i++){
            for(int j = 0; j < assembledComponents.get(i).size(); j++){
                ViewComponent component = assembledComponents.get(i).get(j);
                setComponentOnGrid(component, i, j);
            }
        }
    }

    /**
     * Sets a component on the grid at the specified position.
     * @param message
     */
    public void showGameState(String message){
        if (gameStateLabel != null) {
            gameStateLabel.setText(message);
        }
    }

    /**
     * Sets a component on the grid at the specified position.
     * @param imageID
     * @param orientation
     * @param col
     * @param row
     */
    @Override
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
        overlay.setId("overlay-" + col + "-" + row);
        overlay.setHgap(2);
        overlay.setVgap(2);

        for (int i = 0; i < 2; i++) {
            overlay.getColumnConstraints().add(new ColumnConstraints(cellSize / 2));
            overlay.getRowConstraints().add(new RowConstraints(cellSize / 2));
        }

        StackPane cell = new StackPane(button, overlay);
        cell.setStyle("-fx-border-color: transparent;");
        myGridPane.add(cell, col, row);
    }

    /**
     * Removes a component from the grid at the specified position.
     */
    public void setupBackButton() {
        backButton.setOnAction(event -> {
            if(gameStateLabel.getText().equals("ASSEMBLING PHASE")){
                goBackToShipBuilding();
            }
            else if(gameStateLabel.getText().equals("SHIP CONTROL")){
                goBackToShipControl();
            }
            else if(gameStateLabel.getText().equals("CARD PICKING") || gameStateLabel.getText().equals("CARD SOLVING")) {
                goBackToFlightPhase();
            }
        });
    }

    /**
     * Navigates back to the flight phase screen.
     */
    public void goBackToFlightPhase(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/flightPhaseL1.fxml"));
            Parent root = fxmlLoader.load();

            FlightPhaseControllerL1 controller = fxmlLoader.getController();
            controller.setServer(this.server);
            controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);
            GuiInterface.getInstance().setFlightPhaseController(controller);

            controller.setControlledStage(controlledStage);
            controlledStage.setScene(new Scene(root, 1210, 740));
            controlledStage.show();

        } catch (IOException e) {
            showError(e.getMessage());
        }
    }

    /**
     * Navigates back to the ship building screen.
     */
    public void goBackToShipBuilding(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipBuildingL1.fxml"));
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
            showError(e.getMessage());
        }
    }

    /**
     * Navigates back to the ship control screen.
     */
    public void goBackToShipControl(){
        try { // Load the FXML file for the ship control screen
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipControlL1.fxml"));
            Parent root = fxmlLoader.load();

            ShipControlControllerL1 controller = fxmlLoader.getController(); // Get the controller for the ship control screen
            controller.setServer(this.server); // Set the server for the controller
            controller.setPlayerInfo(this.gameID, this.playerNickname, this.color); // Set the player info for the controller
            GuiInterface.getInstance().setShipControlController(controller); // Update the global interface with the controller

            controller.setControlledStage(controlledStage); // Set the controlled stage for the controller
            Scene scene = new Scene(root, 1210, 740); // Create a new scene with the loaded root
            controlledStage.setScene(scene); // Set the scene to the controlled stage
            controlledStage.show(); // Show the controlled stage

        } catch (IOException e) { // Handle any IO exceptions that may occur during the loading of the FXML file
            showError(e.getMessage());
        }
    }

    /**
     * Displays an error message on the screen.
     * @param message
     */
    public void showError(String message) {
        Platform.runLater(() -> { // Update the error label and background with the provided message
            errorLabel.setText(message); // Set the error message text
            errorLabel.setVisible(true); // Make the error label visible
            errorBackground.setVisible(true); // Make the error background visible

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

    /**
     * Sets the stage to be controlled by this controller.
     * @param stage the stage to be controlled
     */
    @Override
    public void setControlledStage(Stage stage) {
        controlledStage = stage;
    }

    /**
     * Sets the server to be used for communication.
     * @param server the server to be used for communication
     */
    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }

    /**
     * Sets the player information for this controller.
     * @param gameID          the ID of the game
     * @param playerNickname  the nickname of the player
     * @param color           the color associated with the player
     */
    @Override
    public void setPlayerInfo(int gameID, String playerNickname, Color color) {
        this.gameID = gameID;
        this.playerNickname = playerNickname;
        this.color = color;
    }

    /**
     * Updates the reserved component on the ship board.
     * @param nickname the nickname of the player who picked or released the component
     * @param imageID the ID of the component image
     * @param released true if the component was released, false if it was picked
     * @throws Exception
     */
    @Override
    public void updateReservedComponent(String nickname, int imageID, boolean released) throws Exception {}

    /**
     * Updates the assembled component on the ship board.
     * @param nickname the nickname of the player who assembled the component
     * @param imageID the ID of the component image
     * @param orientation the orientation of the component
     * @param x the x-coordinate of the component on the ship board
     * @param y the y-coordinate of the component on the ship board
     * @throws Exception
     */
    @Override
    public void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws Exception {
        if(!nickname.equals(shipBoardPlayerNickname)){
            return;
        }
        Platform.runLater(() -> {
            setImageOnGrid(String.valueOf(imageID), orientation, y, x);
        });
    }

    /**
     * Updates the ship control screen.
     * @throws Exception
     */
    @Override
    public void updateShipControl() throws Exception {
        Platform.runLater(() -> {
            try { // Load the FXML file for the ship control screen
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipControlL1.fxml"));
                Parent root = loader.load(); // Load the root node from the FXML file

                ShipControlControllerL1 controller = loader.getController(); // Get the controller for the ship control screen
                controller.setServer(this.server); // Set the server for the controller
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);  // Set the player info for the controller
                GuiInterface.getInstance().setShipControlController(controller); // Update the global interface with the controller

                controller.setControlledStage(controlledStage); // Set the controlled stage for the controller
                controlledStage.setScene(new Scene(root, 1210, 740)); // Create a new scene with the loaded root
                controlledStage.show(); // Show the controlled stage

            } catch (IOException e) { // Handle any IO exceptions that may occur during the loading of the FXML file
                showError(e.getMessage());
            }
        });
    }

    /**
     * Updates the ship repair screen.
     * @param nickname the nickname of the player who needs to repair their ship
     * @throws Exception
     */
    @Override
    public void updateShipRepair(String nickname) throws Exception {
        Platform.runLater(() -> {
            showGameState("SHIP REPAIR (player " + nickname + ")");
        });
    }

    /**
     * Updates the destroyed component on the ship board.
     * @param nickname the nickname of the player whose component was destroyed
     * @param x the x-coordinate of the destroyed component
     * @param y the y-coordinate of the destroyed component
     * @throws Exception
     */
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

    /**
     * Updates the component change on the ship board.
     * @param nickname the nickname of the player whose component has changed
     * @param x the x-coordinate of the changed component
     * @param y the y-coordinate of the changed component
     * @throws Exception
     */
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

    /**
     * Updates the card picking phase on the ship board.
     * @throws Exception
     */
    @Override
    public void updateCardPicking() throws Exception {
        Platform.runLater(() -> {
            showGameState("CARD PICKING");
        });
    }

    /**
     * Updates the card solving phase on the ship board.
     * @param imageID the ID of the image representing the card
     * @throws Exception
     */
    @Override
    public void updateCardSolving(int imageID) throws Exception {
        Platform.runLater(() -> {
            showGameState("CARD SOLVING");
        });
    }

    /**
     * Updates the player quit status on the ship board.
     * @param nickname the nickname of the player who quit
     * @throws Exception
     */
    @Override
    public void updatePlayerQuit(String nickname) throws Exception {
        Platform.runLater(() -> {
            if(nickname.equals(this.shipBoardPlayerNickname)) {
                statusLabel.setText("ABANDONED");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        });
    }

    /**
     * Updates the player's credits on the ship board.
     * @param nickname the nickname of the player whose credits are being updated
     * @param change the amount to change the player's credits by (can be positive or negative)
     * @throws Exception
     */
    @Override
    public void updatePlayerCredits(String nickname, int change) throws Exception {
        Platform.runLater(() -> {
            if(nickname.equals(shipBoardPlayerNickname)){
                this.credits += change;
                playerCreditsLabel.setText(String.valueOf(credits));
            }
        });
    }

    /**
     * Updates the end game screen.
     * @throws Exception
     */
    @Override
    public void updateEndGame() throws Exception {
        Platform.runLater(() -> { // This method is called to update the end game screen
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/endgame.fxml"));
                Parent root = loader.load(); // Load the FXML file for the end game screen

                EndgameController controller = loader.getController(); // Get the controller for the end game screen
                controller.setServer(this.server); // Set the server for the controller
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.color); // Set the player info for the controller

                controller.setControlledStage(controlledStage); // Set the controlled stage for the controller
                controlledStage.setScene(new Scene(root, 1210, 740)); // Create a new scene with the loaded root
                controlledStage.show(); // Show the controlled stage

            } catch (IOException e) {
                showError(e.getMessage());
            }
        });
    }

    /**
     * Displays an error message on the GUI.
     * @param error the error message to be displayed
     */
    @Override
    public void notifyError(String error) {
        Platform.runLater(() -> showError(error));
    }

    /**
     * Displays the current game phase on the GUI.
     * @param gamePhase the new game phase to be displayed
     */
    @Override
    public void notifyGamePhase(String gamePhase) {
        Platform.runLater(() -> {
            showGameState(gamePhase);
        });
    }
}