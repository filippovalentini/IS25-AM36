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
/** * ShipBoardControllerL2 is the controller for the ship board in Level 2 of the game.
 */
public class ShipBoardControllerL2 extends ShipBoardGraphics implements ShipBoardController {
    private Stage controlledStage;

    @FXML
    private Label errorLabel;
    @FXML
    private Label lostComponentsLabel;
    @FXML
    private Label playerCreditsLabel;
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
    private GridPane reservedGridPane;
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
     * Constructor for ShipBoardControllerL2.
     * @param otherPlayerNickname
     */
    public ShipBoardControllerL2(String otherPlayerNickname) {
        this.shipBoardPlayerNickname = otherPlayerNickname;
        this.shipBoardcolor = GuiInterface.getInstance().getView().getCurrentPlayers().get(otherPlayerNickname);
    }

    @FXML
    private void initialize() {
        playerNicknameLabel.setText(shipBoardPlayerNickname);
        playerColorLabel.setText("██");
        playerColorLabel.setStyle(Color.convertColorIntoStyle(shipBoardcolor));
        credits = GuiInterface.getInstance().getView().getCredits(shipBoardPlayerNickname);
        lostComponents = GuiInterface.getInstance().getView().getLostComponents(shipBoardPlayerNickname);
        playerCreditsLabel.setText(String.valueOf(credits));
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
        initializeReservedComponents();
    }

    /**
     * Displays the current game state on the ship board.
     * @param message
     */
    public void showGameState(String message){
        gameStateLabel.setText(message);
    }

    /**
     * Initializes the assembled components on the ship board.
     */
    public void initializeAssembledComponents() {
        List<List<ViewComponent>> assembledComponents = GuiInterface.getInstance().getView().getAssembledComponents(this.shipBoardPlayerNickname);
        for(int i = 0; i < assembledComponents.size(); i++){ // Iterate through each row of components
            for(int j = 0; j < assembledComponents.get(i).size(); j++){ // Iterate through each component in the row
                ViewComponent component = assembledComponents.get(i).get(j); // Get the component at the current position
                setComponentOnGrid(component, i, j);  // Set the component on the grid
            }
        }
    }

    /**
     * Initializes the reserved components on the ship board.
     */
    public void initializeReservedComponents() {
        List<ViewComponent> reservedComponents = GuiInterface.getInstance().getView().getReservedComponents(this.shipBoardPlayerNickname);
        if(!reservedComponents.isEmpty()){ // Check if there are reserved components
            setReservedComponent(componentImageMap.get(String.valueOf(reservedComponents.get(0).getImageID())), 0);
            if(reservedComponents.size() > 1){ // If there are two reserved components
                setReservedComponent(componentImageMap.get(String.valueOf(reservedComponents.get(1).getImageID())), 1);
            }else{ // If there is only one reserved component, set the second one to a default image
                setReservedComponent(componentImageMap.get("3"), 1);
            }
        }else{ // If there are no reserved components, set both reserved slots to a default image
            setReservedComponent(componentImageMap.get("3"), 0);
            setReservedComponent(componentImageMap.get("3"), 1);
        }
    }

    /**
     * Sets a component on the grid at the specified position.
     * @param image
     * @param position
     */
    public void setReservedComponent(Image image, int position) {
        ImageView imageView = new ImageView(image); // Create an ImageView with the provided image

        double cellHeight = reservedGridPane.getRowConstraints().get(position).getPrefHeight(); // Get the preferred height of the row at the specified position

        imageView.setFitWidth(cellHeight); // Set the width of the ImageView to the cell height
        imageView.setFitHeight(cellHeight); // Set the height of the ImageView to the cell height
    imageView.setPreserveRatio(false); // Preserve the aspect ratio of the image

        reservedGridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == position && GridPane.getColumnIndex(node) == 0); // Remove any existing node at the specified position in the reserved grid pane
        reservedGridPane.add(imageView, 0, position); // Add the new ImageView to the reserved grid pane at the specified position
    }

    /**
     * Sets a component on the grid at the specified coordinates.
     * @param imageID
     * @param orientation
     * @param col
     * @param row
     */
    @Override
    public void setImageOnGrid(String imageID, Orientation orientation, int col, int row) {
        if (imageID.equals("000") || imageID.equals("003")) { // If the imageID is "000" or "003", do not set an image
            return;
        }

        Image image = componentImageMap.get(imageID); // Get the image corresponding to the imageID from the componentImageMap

        double cellSize = 110; // Define the size of each cell in the grid

        ImageView imageView = new ImageView(image); // Create an ImageView with the specified image
        imageView.setFitWidth(cellSize); // Set the width of the ImageView to the cell size
        imageView.setFitHeight(cellSize); // Set the height of the ImageView to the cell size
        imageView.setPreserveRatio(true); // Preserve the aspect ratio of the image

        switch (orientation) { // Set the rotation of the ImageView based on the orientation
            case WEST -> imageView.setRotate(270);
            case SOUTH -> imageView.setRotate(180);
            case EAST -> imageView.setRotate(90);
        }

        Button button = new Button(); // Create a button to hold the ImageView
        button.setPrefSize(cellSize, cellSize);
        button.setMinSize(cellSize, cellSize);
        button.setMaxSize(cellSize, cellSize);
        button.setStyle("-fx-padding: 0; -fx-background-color: transparent; -fx-border-color: transparent;");
        button.setGraphic(imageView);

        GridPane overlay = new GridPane(); // Create a GridPane to overlay on the button
        overlay.setPrefSize(cellSize, cellSize);
        overlay.setMouseTransparent(true);
        overlay.setPickOnBounds(false);
        overlay.setId("overlay-" + col + "-" + row);
        overlay.setHgap(2);
        overlay.setVgap(2);

        for (int i = 0; i < 2; i++) { // Add two rows and two columns to the overlay
            overlay.getColumnConstraints().add(new ColumnConstraints(cellSize / 2));
            overlay.getRowConstraints().add(new RowConstraints(cellSize / 2));
        }

        StackPane cell = new StackPane(button, overlay);
        cell.setStyle("-fx-border-color: transparent;");
        myGridPane.add(cell, col, row);
    }

    /**
     * Sets a component on the grid at the specified coordinates.
     * @param position
     * @return
     */
    public Image getReservedComponentImage(int position) {
        for (Node node : reservedGridPane.getChildren()) { // Iterate through the nodes in the reserved grid pane
            Integer columnIndex = GridPane.getColumnIndex(node); // Get the column index of the node
            Integer rowIndex = GridPane.getRowIndex(node); // Get the row index of the node

            if (columnIndex == null) columnIndex = 0; // If the column index is null, set it to 0
            if (rowIndex == null) rowIndex = 0; // If the row index is null, set it to 0

            if (columnIndex == 0 && rowIndex == position && node instanceof ImageView) { // If the node is in the first column and at the specified row position, and it is an ImageView
                return ((ImageView) node).getImage(); // Return the image of the ImageView
            }
        }
        return null;
    }

    /**
     * Sets up the back button to navigate to the appropriate screen based on the current game state.
     */
    public void setupBackButton() {
        backButton.setOnAction(event -> { // Set an action for the back button
            if(gameStateLabel.getText().equals("ASSEMBLING PHASE")){ // If the game state is "ASSEMBLING PHASE"
                goBackToShipBuilding();
            }
            else if(gameStateLabel.getText().equals("SHIP CONTROL")){ // If the game state is "SHIP CONTROL"
                goBackToShipControl();
            }
            else if(gameStateLabel.getText().equals("CARD PICKING") || gameStateLabel.getText().equals("CARD SOLVING")){
                goBackToFlightPhase();
            }
        });
    }
    /**
     * Navigates back to the ship building screen.
     */
    public void goBackToShipBuilding(){
        try { // Load the ship building screen FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipBuildingL2.fxml"));
            Parent root = fxmlLoader.load(); // Load the root node from the FXML file

            ShipBuildingControllerL2 controller = fxmlLoader.getController(); // Get the controller from the FXML loader
            controller.setServer(this.server);  // Set the server in the controller
            controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);     // Set the server and player information in the controller
            GuiInterface.getInstance().setShipBuildingController(controller); // Set the ship building controller in the GUI interface

            controller.setControlledStage(controlledStage); // Set the controlled stage in the controller
            Scene scene = new Scene(root, 1210, 740);  // Create a new scene with the loaded root node and specified dimensions
            controlledStage.setScene(scene); // Set the scene in the controlled stage
            controlledStage.show(); // Show the controlled stage

        } catch (IOException e) {
            showError(e.getMessage());
        }
    }

    /**
     * Navigates back to the ship control screen.
     */
    public void goBackToShipControl(){
        try { // Load the ship control screen FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipControlL2.fxml"));
            Parent root = fxmlLoader.load(); // Load the root node from the FXML file

            ShipControlControllerL2 controller = fxmlLoader.getController(); // Get the controller from the FXML loader
            controller.setServer(this.server); // Set the server in the controller
            controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);   // Set the player information in the controller
            GuiInterface.getInstance().setShipControlController(controller); // Set the ship control controller in the GUI interface

            controller.setControlledStage(controlledStage); // Set the controlled stage in the controller
            Scene scene = new Scene(root, 1210, 740); // Create a new scene with the loaded root node and specified dimensions
            controlledStage.setScene(scene); // Set the scene in the controlled stage
            controlledStage.show(); // Show the controlled stage

        } catch (IOException e) {
            showError(e.getMessage());
        }
    }

    /**
     * Navigates back to the flight phase screen.
     */
    public void goBackToFlightPhase(){
        try { // Load the flight phase screen FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/flightPhaseL2.fxml"));
            Parent root = fxmlLoader.load(); // Load the root node from the FXML file

            FlightPhaseControllerL2 controller = fxmlLoader.getController(); // Get the controller from the FXML loader
            controller.setServer(this.server); // Set the server in the controller
            controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);  // Set the player information in the controller
            GuiInterface.getInstance().setFlightPhaseController(controller); // Set the flight phase controller in the GUI interface

            controller.setControlledStage(controlledStage); // Set the controlled stage in the controller
            controlledStage.setScene(new Scene(root, 1210, 740)); // Create a new scene with the loaded root node and specified dimensions
            controlledStage.show(); // Show the controlled stage

        } catch (IOException e) {
            showError(e.getMessage());
        }
    }

    /**
     * Displays an error message on the ship board.
     * @param message
     */
    public void showError(String message) {
        Platform.runLater(() -> { // Run the error display on the JavaFX Application Thread
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
     * Updates the reserved component based on the player's action.
     * @param nickname the nickname of the player who picked or released the component
     * @param imageID the ID of the component image
     * @param released true if the component was released, false if it was picked
     * @throws Exception
     */
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
        Platform.runLater(() -> { // Run the update on the JavaFX Application Thread
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipControlL2.fxml"));
                Parent root = loader.load(); // Load the root node from the FXML file

                ShipControlControllerL2 controller = loader.getController(); // Get the controller from the FXML loader
                controller.setServer(this.server); // Set the server in the controller
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);  // Set the player information in the controller
                GuiInterface.getInstance().setShipControlController(controller); // Set the ship control controller in the GUI interface

                controller.setControlledStage(controlledStage); // Set the controlled stage in the controller
                controlledStage.setScene(new Scene(root, 1210, 740)); // Create a new scene with the loaded root node and specified dimensions
                controlledStage.show(); // Show the controlled stage

            } catch (IOException e) {
                showError(e.getMessage());
            }
        });
    }

    /**
     * Updates the ship repair screen for a player.
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
     * Updates the ship board when a component is destroyed.
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
     * Updates the ship board when a component is changed.
     * @param nickname the nickname of the player whose component was changed
     * @param x the x-coordinate of the component
     * @param y the y-coordinate of the component
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
     * Updates the ship board when a card is being picked.
     * @throws Exception
     */
    @Override
    public void updateCardPicking() throws Exception {
        Platform.runLater(() -> {
            showGameState("CARD PICKING");
        });
    }

    /**
     * Updates the ship board when a card is being solved.
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
     * Updates the ship board when a player quits the game.
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
                showError(e.getMessage());
            }
        });
    }
    /**
     * Notifies the view about an error.
     * @param error the error message to be displayed
     */
    @Override
    public void notifyError(String error) {
        Platform.runLater(() -> showError(error));
    }

    //notifies the view about a change in the game phase

    /**
     * Notifies the view about a change in the game phase.
     * @param gamePhase the new game phase to be displayed
     */
    @Override
    public void notifyGamePhase(String gamePhase) {
        Platform.runLater(() -> {
            showGameState(gamePhase);
        });
    }
}
