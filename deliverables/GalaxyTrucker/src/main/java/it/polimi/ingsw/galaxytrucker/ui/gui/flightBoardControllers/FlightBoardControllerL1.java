package it.polimi.ingsw.galaxytrucker.ui.gui.flightBoardControllers;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.otherControllers.EndgameController;
import it.polimi.ingsw.galaxytrucker.ui.gui.shipBoardControllers.FlightPhaseControllerL1;
import it.polimi.ingsw.galaxytrucker.ui.gui.shipBoardControllers.ShipBuildingControllerL1;
import it.polimi.ingsw.galaxytrucker.ui.gui.shipBoardControllers.ShipControlControllerL1;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.FlightBoardController;
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
import javafx.scene.input.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * FlightBoardControllerL1 is the controller for the Level 1 flight board
 */
public class FlightBoardControllerL1 implements FlightBoardController {
    private Stage controlledStage;

    @FXML private Label start;
    @FXML private Label pos0, pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8, pos9;
    @FXML private Label pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17;
    @FXML private Button backButton;
    @FXML private Rectangle errorBackground;
    @FXML private Label errorLabel;
    @FXML private Rectangle gameStateBackground;
    @FXML private Label gameStateLabel;

    int gameID;
    private String playerNickname;
    Color color;
    private List<Label> targetLabels;
    private VirtualServer server;
    private Map<Color, Integer> colorCellMap;
    private Map<String, Color> playerColorMap;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        setupBackButton(); // Set up the back button functionality

        targetLabels = List.of( // Initialize the list of target labels
                pos0, pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8,
                pos9, pos10, pos11, pos12, pos13, pos14, pos15, pos16, pos17
        );

        start.setOnDragDetected(event -> { // Handle drag detection on the start label
            Dragboard db = start.startDragAndDrop(TransferMode.MOVE);   // Start the drag-and-drop operation
            ClipboardContent content = new ClipboardContent(); // Create a clipboard content object
            content.putString("🚀"); // Set the content to be transferred
            db.setContent(content); // Set the content of the dragboard

            InputStream imgStream = getClass().getResourceAsStream("/it/polimi/ingsw/galaxytrucker/images/cardboard/spaceShip.png"); // Load the  image from resources
            Image rocketImage = new Image(imgStream); // Create an Image object from the input stream
            db.setDragView(rocketImage, rocketImage.getWidth() / 2, rocketImage.getHeight() / 2); // Set the drag view to the center of the image

            event.consume(); // Consume the event to indicate it has been handled
        });

        for (Label label : targetLabels) { // Enable drag-and-drop functionality on each target label
            enableDropOn(label);
        }

        colorCellMap = GuiInterface.getInstance().getView().getColorCellMap(); // Get the color-cell mapping from the view
        playerColorMap = GuiInterface.getInstance().getView().getPlayerColorMap(); // Get the player-color mapping from the view
        this.playerNickname = GuiInterface.getInstance().getView().getNickname(); // Get the player's nickname from the view
        this.color = GuiInterface.getInstance().getView().getColor(); // Get the player's color from the view
        showGameState(GuiInterface.getInstance().getView().getGameState()); // Display the current game state

        initializeFlightBoardFromMap(); // Initialize the flight board from the color-cell map
    }

    public void showGameState(String message){  // Method to display the current game state
        gameStateLabel.setText(message); // Set the text of the game state label
    }

    public void showError(String message) { // Method to display an error message
        Platform.runLater(() -> { // Run the following code on the JavaFX Application Thread
            errorLabel.setText(message); // Set the text of the error label
            errorLabel.setVisible(true); // Make the error label visible
            errorBackground.setVisible(true); // Make the error background rectangle visible

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
     * Initializes the flight board from the color-cell map.
     * This method sets the text of each label to an empty string and places the players' positions on the board.
     */
    public void initializeFlightBoardFromMap() {
        for (Label label : targetLabels) {
            label.setText("");
        }

        boolean playerAlreadyPlaced = false;// Flag to check if the current player has already placed their position

        for (Map.Entry<Color, Integer> entry : colorCellMap.entrySet()) { // Iterate through the color-cell map
            Color playerColor = entry.getKey(); // Get the player's color
            Integer position = entry.getValue(); // Get the player's position

            if (position != null && position >= 0 && position < targetLabels.size()) { // Check if the position is valid
                setPosition(playerColor, position); // Set the player's position on the flight board

                if (playerColor.equals(this.color)) { // Check if the current player's color matches the player's color
                    playerAlreadyPlaced = true; // Set the flag to true if the current player has already placed their position
                }
            }
        }
        if (!playerAlreadyPlaced) { // If the current player has not placed their position yet
            start.setText("🚀"); // Set the start label text to a rocket emoji
        } else { // If the current player has already placed their position
            start.setText(""); // Clear the start label text
            start.setOnDragDetected(null); // Remove the drag detection event handler from the start label
        }
    }
    /**
     * Enables drag-and-drop functionality on the specified label.
     * This method allows players to drop their ship position on the target labels.
     *
     * @param label The label to enable drag-and-drop functionality on.
     */
    private void enableDropOn(Label label) {
        label.setOnDragOver(event -> { // Handle drag-over events on the label
            if (event.getGestureSource() != label && event.getDragboard().hasString()) { // Check if the drag source is not the label itself and if the dragboard has a string
                event.acceptTransferModes(TransferMode.MOVE); // Accept the move transfer mode
            }
            event.consume(); // Consume the event to indicate it has been handled
        });

        label.setOnDragDropped(event -> { // Handle drag-dropped events on the label
            try {
                int pos = targetLabels.indexOf(label); // Get the index of the label in the target labels list
                server.setPosition(this.gameID, this.playerNickname, pos); // Notify the server to set the player's position on the flight board
            } catch (Exception e) { // Handle any exceptions that may occur
                showError(e.getMessage()); // Show an error message if an exception occurs
            }
            event.setDropCompleted(true); // Set the drop completed flag to true
            event.consume();
        });

        label.setOnDragEntered(event -> { // Handle drag-entered events on the label
            if (event.getGestureSource() != label && event.getDragboard().hasString()) { // Check if the drag source is not the label itself and if the dragboard has a string
                String currentStyle = label.getStyle(); // Get the current style of the label
                label.setStyle(currentStyle + "; -fx-border-color: white; -fx-border-width: 2px;"); // Add a border to the label to indicate it is a valid drop target
            }
        });

        label.setOnDragExited(event -> { // Handle drag-exited events on the label
            String currentStyle = label.getStyle(); // Get the current style of the label
            String newStyle = currentStyle // Remove the border style from the label
                    .replaceAll("-fx-border-color: white;?", "") // Remove the border color style
                    .replaceAll("-fx-border-width: 2px;?", ""); // Remove the border width style
            label.setStyle(newStyle.trim()); // Set the new style to the label, trimming any extra spaces
        });
    }
    /**
     * Sets the position of a player on the flight board.
     * This method updates the target label for the specified color and cell.
     *
     * @param color The color of the player whose position is being set.
     * @param cell  The cell index where the player's position should be set.
     */
    public void setPosition(Color color, int cell) {
        Label targetLabel = targetLabels.get(cell);
        targetLabel.setText("⬤");
        targetLabel.setStyle(Color.convertColorIntoStyle(color));
        colorCellMap.put(color, cell);
    }
    /**
     * Free the position of a player on the flight board.
     * This method clears the target label for the specified color and removes it from the color-cell map.
     *
     * @param color The color of the player whose position is being freed.
     */
    public void freePosition(Color color) {
        int cell = colorCellMap.get(color);
        Label targetLabel = targetLabels.get(cell);
        targetLabel.setText("");
        colorCellMap.remove(color);
    }
    /**
     * Sets up the back button functionality.
     * This method defines the action to be performed when the back button is clicked,
     * allowing the user to navigate back to the appropriate screen based on the current game state.
     */
    public void setupBackButton() {
        backButton.setOnAction(event -> {
            if(gameStateLabel.getText().equals("ASSEMBLING PHASE")){ // If the game is in the assembling phase, go back to the ship building screen
                goBackToShipBuilding();
            }
            else if(gameStateLabel.getText().equals("SHIP CONTROL")){ // If the game is in the ship control phase, go back to the ship control screen
                goBackToShipControl();
            }
            else if(gameStateLabel.getText().equals("CARD PICKING") || gameStateLabel.getText().equals("CARD SOLVING")){  // If the game is in the card picking or card solving phase, go back to the flight phase
                goBackToFlightPhase();
            }
            else  if (start.getText().isEmpty()){  // If the start label is empty, it means the player has already placed their position
                showError("Patience, hero");
            }
        });
    }
    /**
     * Navigates back to the flight phase screen.
     * This method loads the flight phase FXML file, sets up the controller, and updates the scene.
     */
    public void goBackToFlightPhase(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/flightPhaseL1.fxml")); // Load the flight phase FXML file
            Parent root = fxmlLoader.load(); // Load the root node from the FXML file

            FlightPhaseControllerL1 controller = fxmlLoader.getController(); // Get the controller for the flight phase
            controller.setServer(this.server); // Set the server for the controller
            controller.setPlayerInfo(this.gameID, this.playerNickname, this.color); // Set the player information for the controller
            GuiInterface.getInstance().setFlightPhaseController(controller); // Set the flight phase controller in the GUI interface

            controller.setControlledStage(controlledStage); // Set the controlled stage for the controller
            controlledStage.setScene(new Scene(root, 1210, 740)); // Create a new scene with the loaded root node and set it to the controlled stage
            controlledStage.show(); // Show the controlled stage
        } catch (IOException e) { // Handle any IO exceptions that may occur during the loading of the FXML file
            showError(e.getMessage());
        }
    }
    /**
     * Navigates back to the ship building screen.
     * This method loads the ship building FXML file, sets up the controller, and updates the scene.
     */
    public void goBackToShipBuilding(){
        try { // Load the ship building FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipBuildingL1.fxml"));
            Parent root = fxmlLoader.load();

            ShipBuildingControllerL1 controller = fxmlLoader.getController(); // Get the controller for the ship building screen
            controller.setServer(this.server); // Set the server for the controller
            controller.setPlayerInfo(this.gameID, this.playerNickname, this.color); // Set the player information for the controller
            GuiInterface.getInstance().setShipBuildingController(controller); // Set the ship building controller in the GUI interface

            controller.setControlledStage(controlledStage); // Set the controlled stage for the controller
            Scene scene = new Scene(root, 1210, 740); // Create a new scene with the loaded root node and set it to the controlled stage
            controlledStage.setScene(scene); // Set the scene to the controlled stage
            controlledStage.show(); // Show the controlled stage

        } catch (IOException e) { // Handle any IO exceptions that may occur during the loading of the FXML file
            showError(e.getMessage());
        }
    }
    /**
     * Navigates back to the ship control screen.
     * This method loads the ship control FXML file, sets up the controller, and updates the scene.
     */
    public void goBackToShipControl(){
        try { // Load the ship control FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipControlL1.fxml")); // Load the ship control FXML file
            Parent root = fxmlLoader.load(); // Load the root node from the FXML file

            ShipControlControllerL1 controller = fxmlLoader.getController(); // Get the controller for the ship control screen
            controller.setServer(this.server); // Set the server for the controller
            controller.setPlayerInfo(this.gameID, this.playerNickname, this.color); // Set the player information for the controller
            GuiInterface.getInstance().setShipControlController(controller); // Set the ship control controller in the GUI interface

            controller.setControlledStage(controlledStage); // Set the controlled stage for the controller
            Scene scene = new Scene(root, 1210, 740); // Create a new scene with the loaded root node and set it to the controlled stage
            controlledStage.setScene(scene); // Set the scene to the controlled stage
            controlledStage.show(); // Show the controlled stage

        } catch (IOException e) { // Handle any IO exceptions that may occur during the loading of the FXML file
            showError(e.getMessage());
        }
    }
    /**
     * Sets the controlled stage for this controller.
     * This method is used to set the stage that this controller will control.
     *
     * @param stage The stage to be controlled by this controller.
     */
    @Override
    public void setControlledStage(Stage stage) {
        this.controlledStage = stage;
    }
    /**
     * Sets the server for this controller.
     * This method is used to set the virtual server that this controller will communicate with.
     *
     * @param server The virtual server to be used by this controller.
     */
    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }
    /**
     * Sets the player information for this controller.
     * This method is used to set the game ID, player nickname, and color for the player.
     *
     * @param gameID The ID of the game the player is participating in.
     * @param playerNickname The nickname of the player.
     * @param color The color associated with the player.
     */
    @Override
    public void setPlayerInfo(int gameID, String playerNickname, Color color){
        this.playerNickname = playerNickname;
        this.color = color;
        this.gameID = gameID;
    }
    /**
     * Notifies the controller about an error message.
     * This method is called to display an error message to the user.
     *
     * @param errorMessage The error message to be displayed.
     */
    @Override
    public void notifyError(String errorMessage) {
        Platform.runLater(() -> {
            showError(errorMessage);

            if (start.getText().isEmpty()) {
                start.setText("🚀");
            }
        });
    }
    /**
     * Updates the picked deck with the specified deck IDs.
     * @param deckIDs
     */
    @Override
    public void updatePickedDeck(List<Integer> deckIDs) {}
    /**
     * Updates the released deck.
     * This method is called when the deck has been released.
     * It can be used to refresh the UI or perform any necessary actions.
     */
    @Override
    public void updateReleasedDeck() {}
    /**
     * Updates the assembling phase with the specified nickname and position.
     * This method is called when a player finishes assembling their ship.
     *
     * @param nickname The nickname of the player who finished assembling.
     * @param position The position where the player's ship is placed.
     */
    @Override
    public void updateFinishAssembling(String nickname, int position) { // Update the flight board with the player's position
        Platform.runLater(() -> { // Update the UI on the JavaFX Application Thread
            Color playerColor = playerColorMap.get(nickname); // Get the color of the player who finished assembling
            if (playerColor == null || position < 0 || position >= targetLabels.size()) return; // Check if the player color is valid and the position is within bounds

            Label targetLabel = targetLabels.get(position); // Get the target label for the player's position
            targetLabel.setText("⬤"); // Set the text of the target label to indicate the player's position
            targetLabel.setStyle(Color.convertColorIntoStyle(playerColor)); // Set the style of the target label based on the player's color

            if(playerNickname.equals(nickname)){ // If the current player is the one who finished assembling
                start.setText(""); // Clear the start label text
                start.setOnDragDetected(null); // Remove the drag detection event handler from the start label
            }
        });
    }

    /**
     * Updates the start of a new cycle.
     */
    @Override
    public void updateStartNewCycle() {}

    /**
     * Updates the finished cycle.
     */
    @Override
    public void updateFinishedCycle() {}
    /**
     * Updates the ship building phase.
     * This method is called when the ship building phase is updated.
     * It can be used to refresh the UI or perform any necessary actions.
     */
    @Override
    public void updateShipControl() throws Exception {
        Platform.runLater(() -> { // Update the UI on the JavaFX Application Thread
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipControlL1.fxml")); // Load the ship control FXML file
                Parent root = loader.load(); // Load the root node from the FXML file

                ShipControlControllerL1 controller = loader.getController(); // Get the controller for the ship control screen
                controller.setServer(this.server); // Set the server for the controller
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.color); // Set the player information for the controller
                GuiInterface.getInstance().setShipControlController(controller); // Set the ship control controller in the GUI interface

                controller.setControlledStage(controlledStage); // Set the controlled stage for the controller
                controlledStage.setScene(new Scene(root, 1210, 740)); // Create a new scene with the loaded root node and set it to the controlled stage
                controlledStage.show(); // Show the controlled stage

            } catch (IOException e) { // Handle any IO exceptions that may occur during the loading of the FXML file
                showError(e.getMessage());
            }
        });
    }
    /**
     * Updates the ship building phase.
     * This method is called when the ship building phase is updated.
     * It can be used to refresh the UI or perform any necessary actions.
     */
    @Override
    public void updateShipRepair(String nickname) throws Exception {
        Platform.runLater(() -> { // Update the UI on the JavaFX Application Thread
            showGameState("SHIP REPAIR (player " + nickname + ")");
        });
    }
    /**
     * Updates the card picking phase.
     * This method is called when the card picking phase is updated.
     * It can be used to refresh the UI or perform any necessary actions.
     */
    @Override
    public void updateCardPicking() throws Exception {
        Platform.runLater(() -> {
            showGameState("CARD PICKING");
        });
    }

    /**
     * Updates the card solving phase.
     * @param imageID The ID of the card that has been picked.
     * @throws Exception
     */
    @Override
    public void updateCardSolving(int imageID) throws Exception {
        Platform.runLater(() -> {
            showGameState("CARD SOLVING");
        });
    }
    /**
     * Updates the player position on the flight board.
     * This method is called when a player's position is updated during the game.
     *
     * @param nickname The nickname of the player whose position is being updated.
     * @param cell     The cell index where the player's position should be set.
     * @throws Exception If an error occurs while updating the player position.
     */
    @Override
    public void updatePlayerPosition(String nickname, int cell) throws Exception {
        Platform.runLater(() -> {
            Color playerColor = playerColorMap.get(nickname); // Get the color of the player whose position is being updated
            freePosition(playerColor); // Free the previous position of the player
            setPosition(playerColor, cell); // Set the new position of the player on the flight board
        });
    }

    /**
     * Updates the end game screen.
     * @throws Exception
     */
    @Override
    public void updateEndGame() throws Exception {
        Platform.runLater(() -> {
            try { // Load the end game FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/endgame.fxml"));
                Parent root = loader.load(); // Load the root node from the FXML file

                EndgameController controller = loader.getController(); // Get the controller for the end game screen
                controller.setServer(this.server); // Set the server for the controller
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.color); // Set the player information for the controller

                controller.setControlledStage(controlledStage); // Set the controlled stage for the controller
                controlledStage.setScene(new Scene(root, 1210, 740)); // Create a new scene with the loaded root node and set it to the controlled stage
                controlledStage.show(); // Show the controlled stage

            } catch (IOException e) { // Handle any IO exceptions that may occur during the loading of the FXML file
                showError(e.getMessage());
            }
        });
    }

    //notifies the view about a change in the game phase
    /**
     * Notifies the controller about a change in the game phase.
     * This method is called to update the game phase displayed on the flight board.
     *
     * @param gamePhase The current game phase to be displayed.
     */
    @Override
    public void notifyGamePhase(String gamePhase) {
        Platform.runLater(() -> {
            showGameState(gamePhase);
        });
    }

}