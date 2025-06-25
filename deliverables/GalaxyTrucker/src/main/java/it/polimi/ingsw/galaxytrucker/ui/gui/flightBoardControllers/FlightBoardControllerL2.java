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
/** * FlightBoardControllerL2 is the controller for the flight board in Level 2 of the game.
 */
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

    /**
     * Initializes the FlightBoardControllerL2.
     */
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
            Dragboard db = start.startDragAndDrop(TransferMode.MOVE); // Start the drag-and-drop operation
            ClipboardContent content = new ClipboardContent(); // Create a clipboard content object
            content.putString("🚀"); // Set the string content to be transferred
            db.setContent(content); // Set the content of the dragboard

            InputStream imgStream = getClass().getResourceAsStream("/it/polimi/ingsw/galaxytrucker/images/cardboard/spaceShip.png"); // Load the image from resources
            Image rocketImage = new Image(imgStream); // Create an Image object from the input stream
            db.setDragView(rocketImage, rocketImage.getWidth() / 2, rocketImage.getHeight() / 2); // Set the drag view to the image

            event.consume(); // Consume the event to indicate that it has been handled
        });

        for (Label label : targetLabels) { // Enable drag-and-drop functionality on each target label
            enableDropOn(label);
        }

        colorCellMap = GuiInterface.getInstance().getView().getColorCellMap(); // Initialize the color-cell mapping
        playerColorMap = GuiInterface.getInstance().getView().getPlayerColorMap(); // Initialize the player-color mapping
        this.playerNickname = GuiInterface.getInstance().getView().getNickname(); // Get the player's nickname
        this.color = GuiInterface.getInstance().getView().getColor(); // Get the player's color
        showGameState(GuiInterface.getInstance().getView().getGameState()); // Display the current game state

        initializeHourglass(); // Initialize the hourglass buttons
        initializeFlightBoardFromMap(); // Set up the flight board based on the current map state
    }
    /**
     * Displays the current game state on the gameStateLabel.
     * @param message The message to display.
     */
    public void showGameState(String message){
        gameStateLabel.setText(message);
    }
    /**
     * Displays an error message on the errorLabel and errorBackground.
     * The error message will fade in, stay visible for 3 seconds, and then fade out.
     * @param message The error message to display.
     */
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
    /**
     * Initializes the hourglass buttons based on the current game state.
     * It sets the appropriate button to show the hourglass rotation or stop it based on the current position and whether the hourglass is running.
     */
    public void initializeHourglass(){
        View view = GuiInterface.getInstance().getView();
        if(view.isHourglassRunning() && view.getHourglassPosition()==1){ // Check if the hourglass is running and at position 1
            hourglassRotation(hourglass1Button);
            hourglass2Button.setDisable(true);
            hourglass1Button.setDisable(false);
        }
        else if(view.isHourglassRunning() && view.getHourglassPosition()==2){ // Check if the hourglass is running and at position 2
            hourglassRotation(hourglass2Button);
            hourglass2Button.setDisable(false);
            hourglass1Button.setDisable(true);
        }
        else if(!view.isHourglassRunning() && view.getHourglassPosition()==1){ // Check if the hourglass is not running and at position 1
            stopHourglass(hourglass1Button, false);
            hourglass2Button.setDisable(true);
            hourglass1Button.setDisable(false);
        }
        else if(!view.isHourglassRunning() && view.getHourglassPosition()==2){ // Check if the hourglass is not running and at position 2
            stopHourglass(hourglass2Button, true);
            hourglass2Button.setDisable(false);
            hourglass1Button.setDisable(true);
        }
    }
    /**
     * Initializes the flight board from the colorCellMap.
     * It sets the text of each target label to an empty string and places the players' positions based on the colorCellMap.
     * If the player is not already placed, it sets the start label to show a rocket emoji and enables drag detection.
     */
    public void initializeFlightBoardFromMap() {
        for (Label label : targetLabels) { // Clear the text of each target label
            label.setText("");
        }

        boolean playerAlreadyPlaced = false; // Flag to check if the player is already placed

        for (Map.Entry<Color, Integer> entry : colorCellMap.entrySet()) { // Iterate through the colorCellMap
            Color playerColor = entry.getKey(); // Get the player's color
            Integer position = entry.getValue(); // Get the player's position

            if (position != null && position >= 0 && position < targetLabels.size()) { // Check if the position is valid
                setPosition(playerColor, position); // Set the position of the player on the flight board

                if (playerColor.equals(this.color)) { // Check if the player's color matches the current player's color
                    playerAlreadyPlaced = true;
                }
            }
        }
        if (!playerAlreadyPlaced) { // If the player is not already placed, set the start label to show a rocket emoji
            start.setText("🚀");
        } else { // If the player is already placed, clear the start label and disable the drag detection
            start.setText("");
            start.setOnDragDetected(null);
            deck1Button.setDisable(true);
            deck2Button.setDisable(true);
            deck3Button.setDisable(true);
            releaseDeckButton.setDisable(true);
        }
    }
    /**
     * Sets the position of a player on the flight board.
     * It updates the target label at the specified cell with a colored dot and stores the color-cell mapping.
     * @param color The color of the player.
     * @param cell The cell index where the player should be placed.
     */
    public void setPosition(Color color, int cell) {
        Label targetLabel = targetLabels.get(cell); // Get the target label for the specified cell
        targetLabel.setText("⬤"); // Set the text of the target label to a colored dot
        targetLabel.setStyle(Color.convertColorIntoStyle(color));; // Set the style of the target label based on the player's color
        colorCellMap.put(color, cell); // Update the colorCellMap with the player's color and cell index
    }
    /**
     * Frees the position of a player on the flight board.
     * It clears the text of the target label at the specified cell and removes the color-cell mapping.
     * @param color The color of the player whose position should be freed.
     */
    public void freePosition(Color color) {
        int cell = colorCellMap.get(color);
        Label targetLabel = targetLabels.get(cell);
        targetLabel.setText("");
        colorCellMap.remove(color);
    }
    /**
     * Enables drag-and-drop functionality on a label.
     * It sets up event handlers for drag over, drag dropped, drag entered, and drag exited events.
     * @param label The label to enable drag-and-drop on.
     */
    private void enableDropOn(Label label) {
        label.setOnDragOver(event -> { // Handle the drag over event
            if (event.getGestureSource() != label && event.getDragboard().hasString()) { // Check if the drag source is not the label itself and if it has a string content
                event.acceptTransferModes(TransferMode.MOVE); // Accept the move transfer mode
            }
            event.consume(); // Consume the event to indicate that it has been handled
        });

        label.setOnDragDropped(event -> { // Handle the drag dropped event
            try {
                int pos = targetLabels.indexOf(label); // Get the index of the target label in the targetLabels list
                server.setPosition(this.gameID, this.playerNickname, pos); // Send the position update to the server
            } catch (Exception e) { // Handle any exceptions that may occur
                showError(e.getMessage()); // Show an error message if an exception occurs
            }
            event.setDropCompleted(true); // Set the drop completed flag to true
            event.consume();
        });

        label.setOnDragEntered(event -> { // Handle the drag entered event
            if (event.getGestureSource() != label && event.getDragboard().hasString()) { // Check if the drag source is not the label itself and if it has a string content
                String currentStyle = label.getStyle(); // Get the current style of the label
                label.setStyle(currentStyle + "; -fx-border-color: white; -fx-border-width: 2px;"); // Set the style to indicate a valid drop target
            }
        });

        label.setOnDragExited(event -> { // Handle the drag exited event
            String currentStyle = label.getStyle(); // Get the current style of the label
            String newStyle = currentStyle  // Remove the border styles from the label's style
                    .replaceAll("-fx-border-color: white;?", "")
                    .replaceAll("-fx-border-width: 2px;?", "");
            label.setStyle(newStyle.trim()); // Set the new style without the border styles
        });
    }
    /**
     * Sets up the back button to navigate to the appropriate screen based on the current game state.
     * It checks the game state label and calls the corresponding method to navigate back to the ship building, ship control, or flight phase screens.
     */
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
    /**
     * Navigates back to the ship building screen.
     * It loads the ship building FXML file, sets up the controller, and updates the scene on the controlled stage.
     */
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
            showError(e.getMessage());
        }
    }
    /**
     * Navigates back to the ship control screen.
     * It loads the ship control FXML file, sets up the controller, and updates the scene on the controlled stage.
     */
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
            showError(e.getMessage());
        }
    }
    /**
     * Navigates back to the flight phase screen.
     * It loads the flight phase FXML file, sets up the controller, and updates the scene on the controlled stage.
     */
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
            showError(e.getMessage());
        }
    }
    /**
     * Sets up the action for the deck 1 button.
     * It sends a request to the server to pick deck 1 when the button is clicked.
     */
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
    /**
     * Sets up the action for the deck 2 button.
     * It sends a request to the server to pick deck 2 when the button is clicked.
     */
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
    /**
     * Sets up the action for the deck 3 button.
     * It sends a request to the server to pick deck 3 when the button is clicked.
     */
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
    /**
     * Sets up the action for the release deck button.
     * It sends a request to the server to release the deck when the button is clicked.
     */
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
    /**
     * Sets up the action for the hourglass 1 button.
     * It sends a request to the server to start a new cycle when the button is clicked.
     */
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
    /**
     * Sets up the action for the hourglass 2 button.
     * It sends a request to the server to start a new cycle when the button is clicked.
     */
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
    /**
     * Rotates the hourglass button with a specific image and effect.
     * It creates an ImageView with the hourglass image, applies a color adjustment effect, and starts a rotation animation.
     * @param hourglassButton The button to apply the hourglass rotation to.
     */
    private void hourglassRotation(Button hourglassButton) {
        Image image = cardImageMap.get("1000");
        if (image == null) {
            showError("Image not found");
            return;
        }

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(hourglassButton.getPrefWidth());
        imageView.setFitHeight(hourglassButton.getPrefHeight());
        imageView.setPreserveRatio(true);

        ColorAdjust redTint = new ColorAdjust();
        redTint.setHue(0.05);
        redTint.setSaturation(1.0);
        redTint.setBrightness(0.6);
        imageView.setEffect(redTint);

        javafx.animation.RotateTransition rotate = new javafx.animation.RotateTransition();
        rotate.setNode(imageView);
        rotate.setDuration(Duration.seconds(2));
        rotate.setByAngle(360);
        rotate.setCycleCount(javafx.animation.Animation.INDEFINITE);
        rotate.setInterpolator(javafx.animation.Interpolator.LINEAR);
        rotate.play();

        hourglassButton.setGraphic(imageView);
    }

    /**
     * Stops the hourglass rotation and sets the button graphic to a static image with a color adjustment effect.
     * If it's the last cycle, it applies a different color adjustment effect.
     * @param hourglassButton The button to stop the hourglass on.
     * @param lastCycle Indicates if this is the last cycle of the game.
     */
    private void stopHourglass(Button hourglassButton, boolean lastCycle) {
        Image image = cardImageMap.get("1000");
        if (image == null) {
            showError("Image not found");
            return;
        }

        ImageView hourglass = new ImageView(image);
        hourglass.setFitWidth(hourglassButton.getPrefWidth());
        hourglass.setFitHeight(hourglassButton.getPrefHeight());
        hourglass.setPreserveRatio(true);
        hourglass.setSmooth(true);

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

        hourglassButton.setGraphic(hourglass);
        hourglassButton.setStyle("-fx-background-color: transparent;");
    }
    /**
     * Clears the hourglass button by removing its graphic and resetting its style.
     * @param hourglassButton The button to clear.
     */
    private void clearHourglassButton(Button hourglassButton) {
        hourglassButton.setGraphic(null);
        hourglassButton.setStyle("-fx-background-color: transparent;");
    }
    /**
     * Sets the controlled stage for this controller.
     * @param stage The stage to control.
     */
    @Override
    public void setControlledStage(Stage stage) {
        controlledStage = stage;
    }
    /**
     * Sets the server for this controller.
     * @param server The virtual server to set.
     */
    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }
    /**
     * Sets the player information for this controller.
     * It updates the player's nickname, color, and game ID.
     * @param gameID The ID of the game.
     * @param playerNickname The nickname of the player.
     * @param color The color of the player.
     */
    @Override
    public void setPlayerInfo(int gameID, String playerNickname, Color color){
        this.playerNickname = playerNickname;
        this.color = color;
        this.gameID = gameID;
    }
    /**
     * Notifies the view about a change in the game phase.
     * It updates the game state label with the new game phase.
     * @param gamePhase The new game phase to display.
     */
    @Override
    public void notifyError(String errorMessage) {
        Platform.runLater(() -> {
            showError(errorMessage);
        });
    }
    /**
     * Notifies the view about a change in the game phase.
     * It updates the game state label with the new game phase.
     * @param gamePhase The new game phase to display.
     */
    @Override
    public void updatePickedDeck(List<Integer> deckIDs) {
        Platform.runLater(() -> {
            if (deckIDs == null || deckIDs.size() < 3) {
                showError("Invalid number of cards");
                return;
            }

            try {
                String key1 = String.valueOf(deckIDs.get(0));
                String key2 = String.valueOf(deckIDs.get(1));
                String key3 = String.valueOf(deckIDs.get(2));

                Image img1 = cardImageMap.get(key1);
                Image img2 = cardImageMap.get(key2);
                Image img3 = cardImageMap.get(key3);

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
                showError(e.getMessage());
            }
        });
    }
    /**
     * Updates the released deck by clearing the deck card images and making the deck preview pane invisible.
     * It also enables the deck buttons and back button.
     */
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
    /**
     * Updates the assembling phase by setting the start label to show a rocket emoji and enabling drag detection.
     * It also enables the deck buttons and back button.
     */
    @Override
    public void updateFinishAssembling(String nickname, int position) {
        Platform.runLater(() -> {
            Color playerColor = playerColorMap.get(nickname);
            if (playerColor == null || position < 0 || position >= targetLabels.size()) return;

            Label targetLabel = targetLabels.get(position);
            targetLabel.setText("⬤");
            targetLabel.setStyle(Color.convertColorIntoStyle(playerColor));

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

    /**
     * Updates the start of a new cycle by enabling the hourglass 2 button and starting its rotation.
     */
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
    /**
     * Updates the finished cycle by clearing the hourglass button and stopping its rotation.
     * It checks which hourglass button is currently active and stops it accordingly.
     */
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
    /**
     * Updates the assembling phase by loading the ship building screen.
     * It sets up the controller and updates the scene on the controlled stage.
     */
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
                showError(e.getMessage());
            }
        });
    }
    /**
     * Updates the assembling phase by loading the ship building screen.
     * It sets up the controller and updates the scene on the controlled stage.
     */
    @Override
    public void updateShipRepair(String nickname) throws Exception {
        Platform.runLater(() -> {
            showGameState("SHIP REPAIR (player " + nickname + ")");
        });
    }
    /**
     * Updates the assembling phase by loading the ship building screen.
     * It sets up the controller and updates the scene on the controlled stage.
     */
    @Override
    public void updateCardPicking() throws Exception {
        Platform.runLater(() -> {
            showGameState("CARD PICKING");
        });
    }
    /**
     * Updates the assembling phase by loading the ship building screen.
     * It sets up the controller and updates the scene on the controlled stage.
     */
    @Override
    public void updateCardSolving(int imageID) throws Exception {
        Platform.runLater(() -> {
            showGameState("CARD SOLVING");
        });
    }
    /**
     * Updates the player position on the flight board.
     * It frees the previous position of the player and sets the new position based on the provided nickname and cell index.
     * @param nickname The nickname of the player whose position is being updated.
     * @param cell The cell index where the player should be placed.
     */
    @Override
    public void updatePlayerPosition(String nickname, int cell) throws Exception {
        Platform.runLater(() -> {
            Color playerColor = playerColorMap.get(nickname);
            freePosition(playerColor);
            setPosition(playerColor, cell);
        });
    }
    /**
     * Updates the end game screen by loading the endgame FXML file.
     * It sets up the controller with the server, player information, and controlled stage.
     * @throws Exception If an error occurs while loading the FXML file.
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

    //notifies the view about a change in the game phase
    /**
     * Notifies the view about a change in the game phase.
     * It updates the game state label with the new game phase.
     * @param gamePhase The new game phase to display.
     */
    @Override
    public void notifyGamePhase(String gamePhase) {
        Platform.runLater(() -> {
            showGameState(gamePhase);
        });
    }
}