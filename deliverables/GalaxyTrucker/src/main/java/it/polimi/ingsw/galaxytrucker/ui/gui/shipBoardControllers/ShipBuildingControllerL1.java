package it.polimi.ingsw.galaxytrucker.ui.gui.shipBoardControllers;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBoardController;
import it.polimi.ingsw.galaxytrucker.ui.gui.otherControllers.ShownComponentsController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBuildingController;
import it.polimi.ingsw.galaxytrucker.ui.gui.flightBoardControllers.FlightBoardControllerL1;
import it.polimi.ingsw.galaxytrucker.ui.view.ViewComponent;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

/**
 * This class is responsible for managing the ship building phase.
 */
public class ShipBuildingControllerL1 extends ShipBoardGraphics implements ShipBuildingController {
    private Stage controlledStage;

    private VirtualServer server;

    private int gameID;
    private String playerNickname;
    private Color color;

    private int colDroppedComponent;
    private int rowDroppedComponent;
    private boolean isComponentPlaced = false;
    private boolean firstComponent = true;
    private boolean componentPicked = false;

    private Button lastDroppedButton = null;
    private final Map<String, Button> draggableButtons = new HashMap<>();
    private Map<String, Image> componentImageMap = new HashMap<>();

    // FXML Components - existing
    @FXML private Rectangle gameStateBackground;
    @FXML private Pane handComponentArea;
    @FXML private Button handComponentButton;
    @FXML private Button setButton;
    @FXML private Button flightBoardButton;
    @FXML private Button pickComponent;
    @FXML private Button rotateButton;
    @FXML private Button viewShownComponentButton;
    @FXML private Button player1ShipButton;
    @FXML private Button player2ShipButton;
    @FXML private Button player3ShipButton;
    @FXML private Button discardButton;
    @FXML private Label playerNameLabel;
    @FXML private Label playerColorLabel;

    @FXML private Button shownComponentButton;

    // Da aggiungere
    @FXML private Pane errorPane;
    @FXML private Label gameStateLabel;
    @FXML private Label errorLabel;

    /**
     * Initializes the controller, setting up the grid pane for drag-and-drop functionality,
     */
    @FXML
    public void initialize() {
        setupGridPaneDragOver();
        setupGridPaneDragDropped();
        setupSetButton();
        setupPickComponentButton();
        setupDiscardButton();
        setupFlightBoardButton();
        setupRotateButton();
        setupViewShownComponentButton();
        setupShownComponentsButton();


        componentImageMap = GuiInterface.getInstance().loadImageMap("components");
        showPlaceholderImage();

        initializeShipBoard();
        setupOtherPlayerButton(player1ShipButton);
        setupOtherPlayerButton(player2ShipButton);
        setupOtherPlayerButton(player3ShipButton);
        setupImages();
    }
    /**
     * Initializes the ship board by setting up game information, assembled components, other components, and buttons.
     */
    public void initializeShipBoard(){
        initializeGameInfo();
        initializeAssembledComponents();
        initializeOtherComponents();
        initializeButtons();
    }

    /**
     * Initializes the assembled components on the ship board.
     */
    public void initializeAssembledComponents(){
        List<List<ViewComponent>> assembledComponents = GuiInterface.getInstance().getView().getAssembledComponents(this.playerNickname);
        for(int i = 0; i < assembledComponents.size(); i++){
            for(int j = 0; j < assembledComponents.get(i).size(); j++){
                ViewComponent component = assembledComponents.get(i).get(j);
                if(component != null){
                    setImageOnGrid(component.getImageID(), component.getOrientation(), j, i);
                }
            }
        }
    }

    /**
     * Initializes other components on the ship board, specifically the picked component if available.
     */
    public void initializeOtherComponents(){
        ViewComponent pickedComponent = GuiInterface.getInstance().getView().getPickedViewComponent();
        if(pickedComponent != null){
            initializePickedComponent(String.valueOf(pickedComponent.getImageID()), pickedComponent.getOrientation());
        }
    }
    /**
     * Initializes the picked component on the ship board.
     *
     * @param imageID The ID of the image representing the component.
     * @param orientation The orientation of the component.
     */
    public void initializePickedComponent(String imageID, Orientation orientation){
        if (isComponentPlaced || firstComponent) { // If a component is already placed or it's the first component, we reset the hand area
            Button newButton = new Button(); // Create a new button for the component
            double buttonSize = 150; // Set the size of the button

            newButton.setPrefSize(buttonSize, buttonSize); // Set preferred size
            newButton.setMinSize(buttonSize, buttonSize); // Set minimum size
            newButton.setMaxSize(buttonSize, buttonSize); // Set maximum size
            newButton.setStyle("-fx-padding: 0; -fx-background-color: transparent;"); // Set style to remove padding and background color

            Image image = componentImageMap.get(imageID); // Get the image from the map using the imageID

            if (image != null) { // If the image is found, set it as the graphic of the button
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(buttonSize);
                imageView.setFitHeight(buttonSize);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                imageView.setCache(true);
                if(orientation.equals(Orientation.WEST)){ // Adjust the rotation based on the orientation
                    imageView.setRotate((imageView.getRotate() - 90) % 360);
                }
                else if(orientation.equals(Orientation.SOUTH)){ // Adjust the rotation for south orientation
                    imageView.setRotate((imageView.getRotate() - 180) % 360);
                }
                else if(orientation.equals(Orientation.EAST)){ // Adjust the rotation for east orientation
                    imageView.setRotate((imageView.getRotate() - 270) % 360);
                }
                newButton.setGraphic(imageView);
            }

            String btnId = UUID.randomUUID().toString(); // Generate a unique ID for the button
            newButton.setUserData(btnId); // Set the button's user data to the generated ID
            draggableButtons.put(btnId, newButton); // Store the button in the draggableButtons map

            newButton.setOnDragDetected(event2 -> { // Set up drag detection for the button
                Dragboard db = newButton.startDragAndDrop(TransferMode.MOVE); // Start the drag-and-drop operation
                ClipboardContent content = new ClipboardContent(); // Create a clipboard content object
                content.putString(btnId); // Put the button's ID into the clipboard content
                db.setContent(content); // Set the clipboard content for the drag-and-drop operation
                firstComponent = false; // Set firstComponent to false as a component is now being picked
                componentPicked = true; // Set componentPicked to true as a component is being picked
                event2.consume(); // Consume the event to indicate it has been handled
            });

            handComponentArea.getChildren().clear(); // Clear the hand component area to prepare for the new button
            handComponentArea.getChildren().add(newButton); // Add the new button to the hand component area

            lastDroppedButton = newButton; // Store the last dropped button for further operations
        }
    }

    /**
     * Initializes the game information such as game state, player nickname, and color.
     */
    public void initializeGameInfo(){
        String gameState = GuiInterface.getInstance().getView().getGameState();
        String nn = GuiInterface.getInstance().getView().getNickname();
        Color c = GuiInterface.getInstance().getView().getColor();

        this.playerNickname = nn;
        this.color = c;

        if (gameStateLabel != null) {
            gameStateLabel.setText(gameState);
        }
        if (playerNameLabel != null) {
            playerNameLabel.setText(nn);
        }
        if (playerColorLabel != null) {
            playerColorLabel.setText("██");
            playerColorLabel.setStyle(Color.convertColorIntoStyle(c));
        }
    }
    /**
     * Initializes the buttons on the ship board, enabling or disabling them based on the current state.
     */
    public void initializeButtons(){
        if(GuiInterface.getInstance().getView().getPickedViewComponent() != null){ // If there is a picked component, disable the pickComponent button
            pickComponent.setDisable(true);
            rotateButton.setDisable(false);
            discardButton.setDisable(false);
        }
        else { // If there is no picked component, enable the pickComponent button
            pickComponent.setDisable(false);
            rotateButton.setDisable(true);
            discardButton.setDisable(true);
        }
        if (shownComponentButton != null) shownComponentButton.setDisable(false); // Enable the shownComponentButton if it exists
        setButton.setDisable(true);
        flightBoardButton.setDisable(false);

        List<String> otherPlayerNicknames = GuiInterface.getInstance().getView().getOtherPlayerNicknames(); // Get the nicknames of other players
        int numberOtherPlayers = otherPlayerNicknames.size();
        player1ShipButton.setDisable(false);
        player1ShipButton.setText(otherPlayerNicknames.get(0));
        if(numberOtherPlayers == 1){ // If there is only one other player, disable the other player buttons
            player2ShipButton.setDisable(true);
            player2ShipButton.setText("no player");
            player3ShipButton.setDisable(true);
            player3ShipButton.setText("no player");
        }
        if(numberOtherPlayers == 2){ // If there are two other players, enable the second player button and disable the third
            player2ShipButton.setDisable(false);
            player2ShipButton.setText(otherPlayerNicknames.get(1));
            player3ShipButton.setDisable(true);
            player3ShipButton.setText("no player");
        }
        if(numberOtherPlayers == 3){ // If there are three other players, enable both the second and third player buttons
            player2ShipButton.setDisable(false);
            player2ShipButton.setText(otherPlayerNicknames.get(1));
            player3ShipButton.setDisable(false);
            player3ShipButton.setText(otherPlayerNicknames.get(2));
        }
    }

    /**
     * Sets an image on the grid pane at the specified column and row indices.
     * @param imageID      The ID of the image to be set.
     * @param orientation  The orientation of the image (e.g., HORIZONTAL, VERTICAL).
     * @param column       The column index in the grid pane.
     * @param row          The row index in the grid pane.
     */
    @Override
    public void setImageOnGrid(String imageID, Orientation orientation, int column, int row){
        if(imageID.equals("000") || imageID.equals("003")){ // If the image ID is "000" or "003", do not set an image
            return;
        }
        Image componentImage = componentImageMap.get(imageID); // Retrieve the image from the componentImageMap using the imageID
        if (componentImage == null) { // If the image is not found, show an error message
            showError("Component image not found for component ID: " + imageID);
            return;
        }

        Button componentButton = new Button(); // Create a new button for the component
        double buttonSize = 110;

        componentButton.setPrefSize(buttonSize, buttonSize); // Set preferred size for the button
        componentButton.setMinSize(buttonSize, buttonSize); // Set minimum size for the button
        componentButton.setMaxSize(buttonSize, buttonSize); // Set maximum size for the button
        componentButton.setStyle("-fx-padding: 0; -fx-background-color: transparent;"); // Set style to remove padding and background color

        ImageView componentImageView = new ImageView(componentImage); // Create an ImageView for the component image
        componentImageView.setFitWidth(buttonSize); // Set the width of the image view
        componentImageView.setFitHeight(buttonSize); // Set the height of the image view
        componentImageView.setPreserveRatio(true); // Preserve the aspect ratio of the image
        if(orientation.equals(Orientation.WEST)){ // Adjust the rotation based on the orientation
            componentImageView.setRotate((componentImageView.getRotate() - 90) % 360);
        }
        else if(orientation.equals(Orientation.SOUTH)){ // Adjust the rotation for south orientation
            componentImageView.setRotate((componentImageView.getRotate() - 180) % 360);
        }
        else if(orientation.equals(Orientation.EAST)){ // Adjust the rotation for east orientation
            componentImageView.setRotate((componentImageView.getRotate() - 270) % 360);
        }
        componentButton.setGraphic(componentImageView); // Set the ImageView as the graphic of the button

        String btnId = UUID.randomUUID().toString(); // Generate a unique ID for the button
        componentButton.setUserData(btnId); // Set the button's user data to the generated ID

        myGridPane.add(componentButton, column, row); // Add the button to the grid pane at the specified column and row indices
    }

    /**
     * Displays an error message in the error pane.
     * @param message
     */
    public void showError(String message) {
        if (errorLabel != null && errorPane != null) {
            errorLabel.setText(message);
            fadeInThenOut(errorPane);
        } else {
            System.err.println("Error: " + message);
        }
    }

    /**
     * Fades in the specified pane, waits for 3 seconds, then fades it out.
     * @param pane
     */
    private void fadeInThenOut(Pane pane) {
        pane.setOpacity(1.0);

        PauseTransition wait = new PauseTransition(Duration.seconds(3));
        wait.setOnFinished(event -> { // Fade out the pane after waiting
            FadeTransition fade = new FadeTransition(Duration.seconds(1.5), pane);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.play();
        });
        wait.play();
    }

    /**
     * Sets up the drag-over event for the grid pane to allow dropping components.
     */
    private void setupGridPaneDragOver() {
        myGridPane.setOnDragOver(event -> { // Handle drag-over events on the grid pane
            if (event.getGestureSource() != myGridPane && event.getDragboard().hasString()) { // Check if the drag source is not the grid pane and if it has a string (the button ID)
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
    }

    /**
     * Sets up the drag-dropped event for the grid pane to handle dropping components onto it.
     */
    private void setupGridPaneDragDropped() {
        myGridPane.setOnDragDropped(event -> { // Handle drag-dropped events on the grid pane
            Dragboard db = event.getDragboard();    // Get the dragboard from the event
            boolean success = false; // Initialize success to false

            if (db.hasString()) { // Check if the dragboard has a string (the button ID)
                String btnId = db.getString(); // Get the button ID from the dragboard
                Button draggedButton = draggableButtons.get(btnId); // Retrieve the button from the draggableButtons map using the button ID

                if (draggedButton != null) {
                    // Safe removal from any container
                    if (draggedButton.getParent() instanceof Pane parentPane) {
                        parentPane.getChildren().remove(draggedButton);
                    } else if (myGridPane.getChildren().contains(draggedButton)) {
                        myGridPane.getChildren().remove(draggedButton);
                    }

                    // Calculate grid position
                    colDroppedComponent = getColumnIndexFromX(event.getX());
                    rowDroppedComponent = getRowIndexFromY(event.getY());

                    // Graphics settings
                    double buttonSize = 110;
                    draggedButton.setPrefSize(buttonSize, buttonSize);
                    draggedButton.setMinSize(buttonSize, buttonSize);
                    draggedButton.setMaxSize(buttonSize, buttonSize);
                    draggedButton.setStyle("-fx-padding: 0; -fx-background-color: transparent;");

                    if (draggedButton.getGraphic() instanceof ImageView imageView) {
                        imageView.setFitWidth(buttonSize);
                        imageView.setFitHeight(buttonSize);
                        imageView.setPreserveRatio(true);
                    }

                    // Add to grid
                    myGridPane.add(draggedButton, colDroppedComponent, rowDroppedComponent);
                    lastDroppedButton = draggedButton;

                    Glow glow = new Glow();
                    glow.setLevel(1);
                    draggedButton.setEffect(glow);
                    draggedButton.setOpacity(1);

                    // Update button states
                    rotateButton.setDisable(false);
                    discardButton.setDisable(false);
                    pickComponent.setDisable(true);
                    setButton.setDisable(false);

                    handComponentArea.getChildren().clear();
                    showPlaceholderImage();

                    success = true;
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    /**
     * Sets up the action for the "Set" button, which is used to assemble a component on the ship board.
     */
    private void setupSetButton() {
        setButton.setOnAction(event -> {
            try{
                server.assembledComponent(this.gameID, this.playerNickname, this.rowDroppedComponent, this.colDroppedComponent);
            }
            catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }

    /**
     * Sets up the action for the "Pick Component" button, which allows the player to pick a hidden component.
     */
    private void setupPickComponentButton() {
        pickComponent.setOnAction(event -> {
            try{
                server.pickHidden(gameID, playerNickname);
            }
            catch(Exception e){
                showError(e.getMessage());
            }
        });
    }

    /**
     * Sets up the action for the "Rotate" button, which allows the player to rotate the picked component.
     */
    private void setupRotateButton() {
        rotateButton.setOnAction(event -> {
            try{
                server.rotatePickedComponent(gameID, playerNickname);
            }
            catch(Exception e){
                showError(e.getMessage());
            }
        });
    }

    /**
     * Sets up the action for the "Discard" button, which allows the player to discard the picked component.
     */
    private void setupDiscardButton() {
        discardButton.setOnAction(event -> {
            try{
                server.putShown(this.gameID, this.playerNickname);
            }
            catch(Exception e){
                showError(e.getMessage());
            }
        });
    }


    /**
     * Sets up the action for the "Shown Components" button, which opens a new window to display the shown components.
     */
    private void setupShownComponentsButton(){
        if (shownComponentButton == null) { // If shownComponentButton is not set, check for viewShownComponentButton
            // Use viewShownComponentButton as fallback
            if (viewShownComponentButton != null) { // If viewShownComponentButton is available, use it
                shownComponentButton = viewShownComponentButton;
            }
        }

        Button buttonToUse = (shownComponentButton != null) ? shownComponentButton : viewShownComponentButton; // Use shownComponentButton if available, otherwise use viewShownComponentButton

        if (buttonToUse != null) { // If the button to use is not null, set up its action
            buttonToUse.setOnAction(event -> {
                try { // Load the FXML for the ShownComponentsController
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shownComponents.fxml"));
                    Parent root = fxmlLoader.load();

                    ShownComponentsController controller = fxmlLoader.getController();
                    controller.setServer(this.server);
                    controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);
                    controller.setGameType(true);
                    GuiInterface.getInstance().setShownComponentsController(controller);

                    controller.setControlledStage(controlledStage);
                    controlledStage.setScene(new Scene(root, 1210, 740));
                    controlledStage.show();

                } catch (IOException e) {
                    showError(e.getMessage());
                }
            });
        }
    }
    /**
     * Sets up the action for the "Flight Board" button, which opens a new window to display the flight board.
     */
    public void setupFlightBoardButton() {
        flightBoardButton.setOnAction(event -> { // Set up the action for the flightBoardButton
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/flightBoardL1.fxml"));
                Parent root = fxmlLoader.load(); // Load the FXML for the FlightBoardController

                FlightBoardControllerL1 controller = fxmlLoader.getController(); // Get the controller instance from the loaded FXML
                controller.setServer(this.server); // Set the server for the controller
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.color); // Set player information for the controller
                GuiInterface.getInstance().setFlightBoardController(controller); // Set the flight board controller in the GUI interface

                controller.setControlledStage(controlledStage); // Set the controlled stage for the controller
                controlledStage.setScene(new Scene(root, 1210, 740)); // Set the scene for the controlled stage
                controlledStage.show(); // Show the controlled stage

            } catch (IOException e) { // Handle any IO exceptions that occur during the loading of the FXML
                showError(e.getMessage());
            }
        });
    }

    /**
     * Sets up the action for the "View Shown Components" button, which opens a new window to display the shown components.
     */
    public void setupViewShownComponentButton() {
        if (viewShownComponentButton != null) {
            setupShownComponentsButton(); // This will handle the logic
        }
    }
    /**
     * Gets the column index based on the X coordinate of the mouse event.
     * @param x The X coordinate of the mouse event.
     * @return The column index corresponding to the X coordinate.
     */
    private int getColumnIndexFromX(double x) {
        double widthSoFar = 0;
        for (int i = 0; i < myGridPane.getColumnConstraints().size(); i++) {
            widthSoFar += myGridPane.getColumnConstraints().get(i).getPrefWidth();
            if (x < widthSoFar) return i;
        }
        return myGridPane.getColumnConstraints().size() - 1;
    }
    /**
     * Gets the row index based on the Y coordinate of the mouse event.
     * @param y The Y coordinate of the mouse event.
     * @return The row index corresponding to the Y coordinate.
     */
    private int getRowIndexFromY(double y) {
        double heightSoFar = 0;
        for (int i = 0; i < myGridPane.getRowConstraints().size(); i++) {
            heightSoFar += myGridPane.getRowConstraints().get(i).getPrefHeight();
            if (y < heightSoFar) return i;
        }
        return myGridPane.getRowConstraints().size() - 1;
    }
    /**
     * Displays a placeholder image in the hand component area when no component is picked.
     */
    private void showPlaceholderImage() {
        handComponentArea.getChildren().clear();
        Image placeholderImage = componentImageMap.get("3");
        if (placeholderImage != null) {
            ImageView placeholderView = new ImageView(placeholderImage);
            placeholderView.setFitWidth(150);
            placeholderView.setFitHeight(150);
            placeholderView.setPreserveRatio(true);
            placeholderView.setSmooth(true);
            placeholderView.setCache(true);
            handComponentArea.getChildren().add(placeholderView);
        }
    }
    /**
     * Sets up the images for the hand component button.
     */
    private void setupImages() {
        try {
            setupButtonWithImage(handComponentButton, "/it/polimi/ingsw/galaxytrucker/images/components/back.jpg", "handComponent", 150, 150);

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }
    /**
     * Sets up a button with an image, binding its width and height to the button's properties.
     *
     * @param button The button to set up.
     * @param imagePath The path to the image resource.
     * @param text The text to display on the button (if image loading fails).
     * @param width The width of the button.
     * @param height The height of the button.
     */
    private void setupButtonWithImage(Button button, String imagePath, String text, float width, float height) {
        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);

            imageView.fitWidthProperty().bind(button.widthProperty());
            imageView.fitHeightProperty().bind(button.heightProperty());
            imageView.setPreserveRatio(false);
            imageView.setSmooth(true);

            button.setGraphic(imageView);
            button.setText("");
            button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        } catch (Exception e) {
            showError(e.getMessage());
            button.setText(text);
        }
    }

    // Interface implementations with Platform.runLater for thread safety
    /**
     * Sets up the button for other players' ship boards.
     * @param button The button to set up.
     */
    private void setupOtherPlayerButton(Button button) {
        button.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipBoardL1.fxml"));
                ShipBoardController controller = new ShipBoardControllerL1(button.getText());
                loader.setController(controller);

                Parent root = loader.load();

                controller.setServer(this.server);
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);
                GuiInterface.getInstance().setShipBoardController(controller);

                controller.setControlledStage(controlledStage);
                controlledStage.setScene(new Scene(root, 1210, 740));
                controlledStage.show();

            } catch (IOException e) {
                showError(e.getMessage());
            }
        });
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
     * Sets the player information for this controller, including game ID, nickname, and color.
     * @param gameID The ID of the game.
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
     * Notifies the view about an error that occurred during the game.
     * @param errorMessage the error message to be displayed
     * @throws Exception
     */
    @Override
    public void notifyError(String errorMessage) throws Exception{
        Platform.runLater(() -> {showError(errorMessage);});
    }

    /**
     * Notifies the view about the current game phase.
     * @param gamePhase the new game phase to be displayed
     * @throws Exception
     */
    @Override
    public void notifyGamePhase(String gamePhase) throws Exception {

    }

    /**
     * Notifies the view about the fact that a component has been picked or released.
     * @param imageID the ID of the component image
     * @param released true if the component has been released, false if it has been picked
     * @throws Exception
     */
    @Override
    public void updatePickedComponent(int imageID, boolean released) throws Exception {
        Platform.runLater(() -> {
            if (released) { // If the component has been released
                if (lastDroppedButton != null) { // If there is a last dropped button
                    myGridPane.getChildren().remove(lastDroppedButton);
                    handComponentArea.getChildren().remove(lastDroppedButton);

                    String btnId = (String) lastDroppedButton.getUserData();
                    if (btnId != null) { // If the button ID is not null, remove it from the draggableButtons map
                        draggableButtons.remove(btnId);
                    }

                    lastDroppedButton = null;

                    firstComponent = true;
                    isComponentPlaced = false;
                    componentPicked = false;

                    rotateButton.setDisable(true);
                    discardButton.setDisable(true);
                    pickComponent.setDisable(false);
                    setButton.setDisable(true);

                    showPlaceholderImage();
                }
            } else { // If the component has been picked
                initializePickedComponent(String.valueOf(imageID), Orientation.NORTH);

                rotateButton.setDisable(false);
                discardButton.setDisable(false);
                pickComponent.setDisable(true);
                setButton.setDisable(true);
            }
        });

    }

    /**
     * Updates the reserved component for a player.
     * @param nickname the nickname of the player
     * @param imageID the ID of the component image
     * @param released true if the component has been released, false if it has been picked
     * @throws Exception
     */
    @Override
    public void updateReservedComponent(String nickname, int imageID, boolean released) throws Exception {
    }


    //notifies the view about the fact that the picked component of the corresponding player has been rotated

    /**
     * Updates the rotation of the picked component.
     * @throws Exception
     */
    @Override
    public void updateRotatePickedComponent() throws Exception{
        Platform.runLater(() -> {
            if (lastDroppedButton != null && lastDroppedButton.getGraphic() instanceof ImageView) {
                ImageView imageView = (ImageView) lastDroppedButton.getGraphic();
                imageView.setRotate((imageView.getRotate() - 90) % 360);
            }
        });
    }

    //notifies the view about the fact that a player (identified by the nickname parameter) has assembled a
    //component in position (x,y) of its ship board; the parameter imageID is needed for the view in order
    //to show the right component to the user

    /**
     * Updates the assembled component for a player.
     * @param nickname the nickname of the player
     * @param imageID the ID of the component image
     * @param orientation the orientation of the component
     * @param x the x coordinate of the position on the ship board
     * @param y the y coordinate of the position on the ship board
     * @throws Exception
     */
    @Override
    public void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws Exception{
        Platform.runLater(() -> {
            if (lastDroppedButton != null) {
                lastDroppedButton.setOnDragDetected(null);
                lastDroppedButton.setEffect(null);
                lastDroppedButton.setOpacity(1.0);
                isComponentPlaced = true;
                componentPicked = false;

                rotateButton.setDisable(true);
                discardButton.setDisable(true);
                pickComponent.setDisable(false);
                setButton.setDisable(true);
                lastDroppedButton = null;
            }
        });
    }

    //notifies the view about the fact that a player has finished the assembling phase and is
    //correctly positioned on the flight board; still, other players have to finish building their ships

    /**
     * Updates the finish assembling status for a player.
     * @param nickname the nickname of the player
     * @param position the position on the flight board
     * @throws Exception
     */
    @Override
    public void updateFinishAssembling(String nickname, int position) throws Exception{}


    //invoked when the game switches to the ship placement phase, which means that the players can only
    //place their ship on the flight board

    /**
     * Updates the ship placement phase.
     * @throws Exception
     */
    @Override
    public void updateShipPlacement() throws Exception{}

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase

    /**
     * Updates the ship control phase.
     * @throws Exception
     */
    @Override
    public void updateShipControl() throws Exception{}



}