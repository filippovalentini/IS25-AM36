package it.polimi.ingsw.galaxytrucker.ui.gui.shipBoardControllers;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.otherControllers.ShownComponentsController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBoardController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBuildingController;
import it.polimi.ingsw.galaxytrucker.ui.gui.flightBoardControllers.FlightBoardControllerL2;
import it.polimi.ingsw.galaxytrucker.ui.view.ViewComponent;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
/** * ShipBuildingControllerL2 is the controller for the ship building phase in Level 2 of the game.
 */
public class ShipBuildingControllerL2 extends ShipBoardGraphics implements ShipBuildingController {
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

    @FXML private Pane notificationPane;
    @FXML private Pane errorPane;
    @FXML private Label notificationLabel;
    @FXML private Label errorLabel;
    @FXML private Rectangle gameStateBackground;
    @FXML private Label gameStateLabel;
    @FXML private Pane handComponentArea;
    @FXML private Label playerNameLabel;
    @FXML private Label playerColorLabel;
    @FXML private Button handComponentButton;
    @FXML private Button setButton;
    @FXML private Button flightBoardButton;
    @FXML private Button pickComponent;
    @FXML private Button reserveButton;
    @FXML private Button rotateButton;
    @FXML private Button reserved0Button;
    @FXML private Button reserved1Button;
    @FXML private Button shownComponentButton;
    @FXML private Button player1ShipButton;
    @FXML private Button player2ShipButton;
    @FXML private Button player3ShipButton;
    @FXML private Button mineShipButton;
    @FXML private Button discardButton;
    /** * Initializes the controller.
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
        setupReserveButton();
        setupReservedButtonsDrag();
        setupShownComponentsButton();
        setupOtherPlayerButton(player1ShipButton);
        setupOtherPlayerButton(player2ShipButton);
        setupOtherPlayerButton(player3ShipButton);

        componentImageMap = GuiInterface.getInstance().loadImageMap("components");
        showGameState(GuiInterface.getInstance().getView().getGameState());
        showPlaceholderImage();
        setReservedButtonPlaceholder(reserved0Button);
        setReservedButtonPlaceholder(reserved1Button);

        initializeShipBoard();
    }
    /** * Displays the current game state message on the game state label.
     * @param message The message to display.
     */
    public void showGameState(String message){
        gameStateLabel.setText(message);
    }

    /**
     * Initializes the ship board by setting up the game information, assembled components,
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
        List<List<ViewComponent>> assembledComponents = GuiInterface.getInstance().getView().getAssembledComponents(this.playerNickname) ; // Get the assembled components for the player
        for(int i = 0; i < assembledComponents.size(); i++){ // Iterate through each row of assembled components
            for(int j = 0; j < assembledComponents.get(i).size(); j++){ // Iterate through each component in the row
                ViewComponent component = assembledComponents.get(i).get(j); // Get the component at the current position
                if(component != null){ // Check if the component is not null
                    setImageOnGrid(component.getImageID(), component.getOrientation(), j, i); // Place the component on the grid
                }
            }
        }

    }
    /**
     * Initializes other components such as the picked component and reserved components.
     */
    public void initializeOtherComponents(){
        ViewComponent pickedComponent = GuiInterface.getInstance().getView().getPickedViewComponent(); // Get the picked component for the player
        if(pickedComponent != null){ // Check if there is a picked component
            initializePickedComponent(String.valueOf(pickedComponent.getImageID()), pickedComponent.getOrientation()); // Initialize the picked component
        }
        List<ViewComponent> reservedComponents = GuiInterface.getInstance().getView().getReservedComponents(this.playerNickname); // Get the reserved components for the player
        int numberReservedComponents = reservedComponents.size(); // Get the number of reserved components
        if(numberReservedComponents > 0){ // Check if there is at least one reserved component
            initializeReserveComponentButton(reserved0Button, componentImageMap.get(reservedComponents.get(0).getImageID()));
            if(numberReservedComponents > 1){ // Check if there is a second reserved component
                initializeReserveComponentButton(reserved1Button, componentImageMap.get(reservedComponents.get(1).getImageID()));
            }
        }
    }

    /**
     * Initializes the picked component with the specified image ID and orientation.
     * @param imageID
     * @param orientation
     */
    public void initializePickedComponent(String imageID, Orientation orientation){
        if (isComponentPlaced || firstComponent) { // Check if a component is already placed or if it's the first component
            Button newButton = new Button();
            double buttonSize = 150;

            newButton.setPrefSize(buttonSize, buttonSize);
            newButton.setMinSize(buttonSize, buttonSize);
            newButton.setMaxSize(buttonSize, buttonSize);
            newButton.setStyle("-fx-padding: 0; -fx-background-color: transparent;");

            Image image = componentImageMap.get(imageID);

            if (image != null) { // Check if the image exists in the map
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(buttonSize);
                imageView.setFitHeight(buttonSize);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                imageView.setCache(true);
                if(orientation.equals(Orientation.WEST)){ // Adjust the rotation based on the orientation
                    imageView.setRotate((imageView.getRotate() - 90) % 360);
                }
                else if(orientation.equals(Orientation.SOUTH)){ // Adjust the rotation based on the orientation
                    imageView.setRotate((imageView.getRotate() - 180) % 360);
                }
                else if(orientation.equals(Orientation.EAST)){ // Adjust the rotation based on the orientation
                    imageView.setRotate((imageView.getRotate() - 270) % 360);
                }
                newButton.setGraphic(imageView);
            }

            String btnId = UUID.randomUUID().toString();
            newButton.setUserData(btnId);
            draggableButtons.put(btnId, newButton);

            newButton.setOnDragDetected(event2 -> {
                Dragboard db = newButton.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(btnId);
                db.setContent(content);
                firstComponent = false;
                componentPicked = true;
                event2.consume();
            });

            handComponentArea.getChildren().clear();
            handComponentArea.getChildren().add(newButton);

            lastDroppedButton = newButton;
        }
    }


    /**
     * Initializes the game information such as player nickname, color, and game state.
     */
    public void initializeGameInfo(){
        String gameState = GuiInterface.getInstance().getView().getGameState(); // Get the current game state
        String nn = GuiInterface.getInstance().getView().getNickname(); // Get the player's nickname
        Color c = GuiInterface.getInstance().getView().getColor(); // Get the player's color

        this.playerNickname = nn;
        this.color = c;
        notificationLabel.setText(gameState);
        playerNameLabel.setText(nn);
        playerColorLabel.setText("██");
        playerColorLabel.setStyle(Color.convertColorIntoStyle(c));
    }

    /**
     * Initializes the buttons based on the current state of the game and the player's actions.
     */
    public void initializeButtons(){
        if(GuiInterface.getInstance().getView().getPickedViewComponent() != null){ // Check if there is a picked component
            pickComponent.setDisable(true);
            reserveButton.setDisable(false);
            rotateButton.setDisable(false);
            discardButton.setDisable(false);
        }
        else { // If there is no picked component
            pickComponent.setDisable(false);
            reserveButton.setDisable(true);
            rotateButton.setDisable(true);
            discardButton.setDisable(true);
        }
        reserved0Button.setDisable(false);
        reserved1Button.setDisable(false);
        shownComponentButton.setDisable(false);
        setButton.setDisable(true);
        flightBoardButton.setDisable(false);

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

    /**
     * Sets an image on the grid pane at the specified column and row.
     * @param imageID      The ID of the image to be set.
     * @param orientation  The orientation of the image (e.g., HORIZONTAL, VERTICAL).
     * @param column       The column index in the grid pane.
     * @param row          The row index in the grid pane.
     */
    @Override
    public void setImageOnGrid(String imageID, Orientation orientation, int column, int row){
        if(imageID.equals("000") || imageID.equals("003")){
            return;
        }
        Image image = componentImageMap.get(imageID);
        if (image == null) {
            showError("Cabin image not found for component ID: " + imageID);
            return;
        }

        Button button = new Button();
        double buttonSize = 110;

        button.setPrefSize(buttonSize, buttonSize);
        button.setMinSize(buttonSize, buttonSize);
        button.setMaxSize(buttonSize, buttonSize);
        button.setStyle("-fx-padding: 0; -fx-background-color: transparent;");

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(buttonSize);
        imageView.setFitHeight(buttonSize);
        imageView.setPreserveRatio(true);
        if(orientation.equals(Orientation.WEST)){
            imageView.setRotate((imageView.getRotate() - 90) % 360);
        }
        else if(orientation.equals(Orientation.SOUTH)){
            imageView.setRotate((imageView.getRotate() - 180) % 360);
        }
        else if(orientation.equals(Orientation.EAST)){
            imageView.setRotate((imageView.getRotate() - 270) % 360);
        }
        button.setGraphic(imageView);

        String btnId = UUID.randomUUID().toString();
        button.setUserData(btnId);

        myGridPane.add(button, column, row);
    }

    /**
     * Displays an error message in the error pane and fades it out after a few seconds.
     * @param message
     */
    public void showError(String message) {
        errorLabel.setText(message);
        fadeInThenOut(errorPane);
    }

    /**
     * Fades in the specified pane, waits for a few seconds, and then fades it out.
     * @param pane
     */
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

    /**
     * Sets up the drag-over event for the grid pane to allow dropping of components.
     */
    private void setupGridPaneDragOver() {
        myGridPane.setOnDragOver(event -> {
            if (event.getGestureSource() != myGridPane && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });
    }

    /**
     * Sets up the drag-dropped event for the grid pane to handle dropping of components.
     */
    private void setupGridPaneDragDropped() {
        myGridPane.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                String btnId = db.getString();
                Button draggedButton = draggableButtons.get(btnId);

                if (draggedButton != null) {
                    if (draggedButton.getParent() instanceof Pane parentPane) {
                        parentPane.getChildren().remove(draggedButton);
                    } else if (myGridPane.getChildren().contains(draggedButton)) {
                        myGridPane.getChildren().remove(draggedButton);
                    } else {
                        if (reserved0Button.getGraphic() == draggedButton.getGraphic()) {
                            setReservedButtonPlaceholder(reserved0Button);
                        } else if (reserved1Button.getGraphic() == draggedButton.getGraphic()) {
                            setReservedButtonPlaceholder(reserved1Button);
                        }
                    }

                    colDroppedComponent = getColumnIndexFromX(event.getX());
                    rowDroppedComponent = getRowIndexFromY(event.getY());

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

                    myGridPane.add(draggedButton, colDroppedComponent, rowDroppedComponent);
                    lastDroppedButton = draggedButton;

                    Glow glow = new Glow();
                    glow.setLevel(1);
                    draggedButton.setEffect(glow);
                    draggedButton.setOpacity(1);

                    rotateButton.setDisable(false);
                    discardButton.setDisable(false);
                    reserveButton.setDisable(false);
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
     * Sets up the action for the set button, which sends the assembled component to the server.
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
     * Sets a placeholder image on the reserved button when no component is reserved.
     * @param button
     */
    private void setReservedButtonPlaceholder(Button button) {
        Image placeholder = componentImageMap.get("3");
        if (placeholder != null) {
            ImageView placeholderView = new ImageView(placeholder);
            placeholderView.setFitWidth(110);
            placeholderView.setFitHeight(110);
            placeholderView.setPreserveRatio(true);
            button.setGraphic(placeholderView);
            button.setStyle("-fx-padding: 0; -fx-background-color: transparent;");
            button.setPrefSize(110, 110);
        }
    }

    /**
     * Sets up the action for the pick component button, which allows the player to pick a hidden component.
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
     * Sets up the action for the shown components button, which opens a new window to display the shown components.
     */
    private void setupShownComponentsButton(){
        shownComponentButton.setOnAction(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shownComponents.fxml"));
                Parent root = fxmlLoader.load();

                ShownComponentsController controller = fxmlLoader.getController();
                controller.setServer(this.server);
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);
                controller.setGameType(false);
                GuiInterface.getInstance().setShownComponentsController(controller);

                controller.setControlledStage(controlledStage);
                controlledStage.setScene(new Scene(root, 1210, 740));
                controlledStage.show();

            } catch (IOException e) {
                showError(e.getMessage());
            }
        });
    }

    /**
     * Sets up the action for the rotate button, which allows the player to rotate the picked component.
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
     * Sets up the action for the discard button, which allows the player to discard the picked component.
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
    /** * Sets up the action for the reserve button, which allows the player to reserve a component.
     */
    private void setupReserveButton() {
        reserveButton.setOnAction(event -> {
            try{
                server.reserveComponent(this.gameID, this.playerNickname);
            }
            catch(Exception e){
                showError(e.getMessage());
            }
        });
    }

    /**
     * Initializes the reserve component button with the specified image.
     * @param targetButton
     * @param image
     */
    private void initializeReserveComponentButton(Button targetButton, Image image) {
        ImageView reservedView = new ImageView(image);
        reservedView.setFitWidth(110);
        reservedView.setFitHeight(110);
        reservedView.setPreserveRatio(true);

        targetButton.setGraphic(reservedView);
        targetButton.setStyle("-fx-padding: 0; -fx-background-color: transparent;");
        targetButton.setPrefSize(110, 110);
    }

    /**
     * Sets up the drag-and-drop functionality for the reserved buttons.
     */
    private void setupReservedButtonsDrag() {
        setupClickFromReservedButton0();
        setupClickFromReservedButton1();
    }

    /**
     * Sets up the action for the reserved button 0, which allows the player to pick a reserved component.
     */
    private void setupClickFromReservedButton0() {
        reserved0Button.setOnAction(event -> {
            try{
                server.pickReservedComponent(this.gameID, this.playerNickname, 0);
            }
            catch(Exception e){
                showError(e.getMessage());
            }
        });
    }

    /**
     * Sets up the action for the reserved button 1, which allows the player to pick a reserved component.
     */
    private void setupClickFromReservedButton1() {
        reserved1Button.setOnAction(event -> {
            try{
                server.pickReservedComponent(this.gameID, this.playerNickname, 1);
            }
            catch(Exception e){
                showError(e.getMessage());
            }
        });
    }

    /**
     * Sets up the action for the other player buttons, which allows the player to view another player's ship board.
     * @param button
     */
    private void setupOtherPlayerButton(Button button) {
        button.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipBoardL2.fxml"));
                ShipBoardController controller = new ShipBoardControllerL2(button.getText());
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
    /** * Checks if the given button is a placeholder button.
     * A placeholder button is one that has a graphic that matches the placeholder image.
     * @param button The button to check.
     * @return true if the button is a placeholder, false otherwise.
     */
    private boolean isPlaceholder(Button button) {
        if (button.getGraphic() instanceof ImageView imageView) {
            Image placeholder = componentImageMap.get("3");
            return imageView.getImage().equals(placeholder);
        }
        return false;
    }

    /**
     * Checks if the given button corresponds to the specified image ID.
     * @param button
     * @param imageId
     * @return
     */
    private boolean imageButtonCorrespondence(Button button, String imageId) {
        if (!(button.getGraphic() instanceof ImageView imageView)) {
            return false;
        }
        Image buttonImage = imageView.getImage();
        if (buttonImage == null) {
            return false;
        }
        Image componentImage = componentImageMap.get(imageId);
        if (componentImage == null) {
            return false;
        }

        return buttonImage.equals(componentImage);
    }

    /**
     * Sets up the action for the flight board button, which opens the flight board screen.
     */
    public void setupFlightBoardButton() {
        flightBoardButton.setOnAction(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/flightBoardL2.fxml"));
                Parent root = fxmlLoader.load();

                FlightBoardControllerL2 controller = fxmlLoader.getController();
                controller.setServer(this.server);
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.color);
                GuiInterface.getInstance().setFlightBoardController(controller);

                controller.setControlledStage(controlledStage);
                controlledStage.setScene(new Scene(root, 1210, 740));
                controlledStage.show();

            } catch (IOException e) {
                showError(e.getMessage());
            }
        });
    }

    /**
     * Calculates the column index based on the x-coordinate of the mouse event.
     * @param x
     * @return the index of the column in the grid pane.
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
     * Calculates the row index based on the y-coordinate of the mouse event.
     * @param y
     * @return the index of the row in the grid pane.
     */
    private int getRowIndexFromY(double y) {
        double heightSoFar = 0;
        for (int i = 0; i < myGridPane.getRowConstraints().size(); i++) {
            heightSoFar += myGridPane.getRowConstraints().get(i).getPrefHeight();
            if (y < heightSoFar) return i;
        }
        return myGridPane.getRowConstraints().size() - 1;
    }
    /** * Displays a placeholder image in the hand component area when no component is picked.
     * This method clears the hand component area and adds a placeholder image.
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


    //
     //UPDATES launched by the server
    //

    /**
     * Sets the controlled stage for this controller.
     * @param stage the stage to be controlled
     */
    @Override
    public void setControlledStage(Stage stage) {
        controlledStage = stage;
    }

    /**
     * Sets the server for this controller.
     * @param server the server to be used for communication
     */
    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }

    //invoked to set the players information needed for method invocation on server

    /**
     * Sets the player information for this controller.
     * @param gameID          the ID of the game
     * @param playerNickname  the nickname of the player
     * @param color           the color associated with the player
     */
    @Override
    public void setPlayerInfo(int gameID, String playerNickname, Color color){
        this.playerNickname = playerNickname;
        this.color = color;
        this.gameID = gameID;
    }

    //notifies a view about an error committed while executing a method on the remote server; the parameter
    //errorMessage describes the type of error

    /**
     * Notifies the view about an error that occurred during method execution on the remote server.
     * @param errorMessage the error message to be displayed
     * @throws Exception
     */
    @Override
    public void notifyError(String errorMessage) throws Exception{
        Platform.runLater(() -> showError(errorMessage));
    }

    //notifies the view about the fact that a component has been successfully picked/released (depending on
    //the value of the boolean parameter) by the corresponding player; the parameter imageID is needed for the
    //view in order to show the right component to the user
    /**
     * Notifies the view about a picked or released component.
     * @param imageID the ID of the image associated with the component
     * @param released true if the component is released, false if it is picked
     * @throws Exception
     */
    @Override
    public void updatePickedComponent(int imageID, boolean released) throws Exception{
        Platform.runLater(() -> {
            if(released){
                if (lastDroppedButton != null) {
                    myGridPane.getChildren().remove(lastDroppedButton);
                    handComponentArea.getChildren().remove(lastDroppedButton);

                    String btnId = (String) lastDroppedButton.getUserData();
                    if (btnId != null) {
                        draggableButtons.remove(btnId);
                    }

                    lastDroppedButton = null;

                    firstComponent = true;
                    isComponentPlaced = false;
                    componentPicked = false;

                    rotateButton.setDisable(true);
                    discardButton.setDisable(true);
                    reserveButton.setDisable(true);
                    pickComponent.setDisable(false);
                    setButton.setDisable(true);

                    showPlaceholderImage();
                }
            }
            else{
                initializePickedComponent(String.valueOf(imageID), Orientation.NORTH);

                rotateButton.setDisable(false);
                discardButton.setDisable(false);
                reserveButton.setDisable(false);
                pickComponent.setDisable(true);
                setButton.setDisable(true);
            }
        });
    }

    //notifies the view about the fact that a player (identified by the nickname parameter) has picked a reserved
    //component/ reserved a component (depending on the value of the boolean parameter); the parameter imageID
    //is needed for the view in order to show the right component to the user

    /**
     * Updates the reserved component for a player.
     * @param nickname the nickname of the player
     * @param imageID the ID of the component image
     * @param released true if the component has been released, false if it has been picked
     * @throws Exception
     */
    @Override
    public void updateReservedComponent(String nickname, int imageID, boolean released) throws Exception{
        Platform.runLater(() -> {
            if(released){
                if (isPlaceholder(reserved0Button)) {
                    //reserveComponentToButton(reserved0Button);
                    initializeReserveComponentButton(reserved0Button, componentImageMap.get(String.valueOf(imageID)));
                    reserved0Button.setDisable(false);
                } else if (isPlaceholder(reserved1Button)) {
                    //reserveComponentToButton(reserved1Button);
                    initializeReserveComponentButton(reserved1Button, componentImageMap.get(String.valueOf(imageID)));
                    reserved1Button.setDisable(false);
                } else {
                    System.out.println("Entrambe le riserve sono piene.");
                }
            }
            else{
                if(imageButtonCorrespondence(reserved0Button, String.valueOf(imageID))){
                    ImageView buttonView = (ImageView) reserved1Button.getGraphic();
                    Image buttonImage = buttonView.getImage();
                    initializeReserveComponentButton(reserved0Button, buttonImage);
                }
                setReservedButtonPlaceholder(reserved1Button);
            }
        });
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
                reserveButton.setDisable(true);
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
    public void updateShipControl() throws Exception{
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