package it.polimi.ingsw.galaxytrucker.ui.gui.shipBoardControllers;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ActionSettingsController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.FlightPhaseController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBoardController;
import it.polimi.ingsw.galaxytrucker.ui.gui.flightBoardControllers.FlightBoardControllerL2;
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
/** * FlightPhaseControllerL2 is the controller for the flight phase.
 */
public class FlightPhaseControllerL2 extends ShipBoardGraphics implements FlightPhaseController {
    private Stage controlledStage;

    @FXML private Label playerNameLabel;
    @FXML private Label playerColorLabel;
    @FXML private Label playerCreditsLabel;
    @FXML private Label lostComponentsLabel;
    @FXML private Label statusLabel;

    @FXML private Pane errorPane;
    @FXML private Label errorLabel;

    @FXML private Label gameStateLabel;

    @FXML private GridPane myGridPane;

    @FXML private Button player1ShipButton;
    @FXML private Button player2ShipButton;
    @FXML private Button player3ShipButton;
    @FXML private Button pickCardButton;
    @FXML private Button destroyButton;
    @FXML private Button flightBoardButton;

    @FXML private Button pickedCardButton;

    @FXML private Button diceButton;
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

    @FXML private Pane popupContainer;
    private boolean popupOpened = false;

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

    /**
     * Initializes the controller after its root element has been completely processed.
     */
    @FXML
    public void initialize() {
        componentImageMap = GuiInterface.getInstance().loadImageMap("components"); // Load component images
        cardImageMap = GuiInterface.getInstance().loadImageMap("cards"); // Load card images
        diceImageMap = GuiInterface.getInstance().loadImageMap("diceFaces"); // Load dice images

        initializeDice(); // Initialize dice images
        initializeGameInfo(); // Initialize game information
        initializeButtons(); // Initialize buttons and their states
        initializeAssembledComponents(); // Initialize assembled components on the grid
        initializeCurrentCard(); // Initialize the current card image

        setupDiceButton(); // Setup action for the dice button
        setupOtherPlayerButton(player1ShipButton); // Setup action for player ship buttons
        setupOtherPlayerButton(player2ShipButton); // Setup action for player ship buttons
        setupOtherPlayerButton(player3ShipButton); // Setup action for player ship buttons
        setupDestroyButton(); // Setup action for the destroy button
        setupFlightBoardButton(); // Setup action for the flight board button
        setupPickCardButton(); // Setup action for the pick card button
        setupQuitButton(); // Setup action for the quit button

        popupContainer.setMouseTransparent(true); // Make the popup container initially not interactable
        setupSkipButton(); // Setup action for the skip button
        setupHitShipButton(); // Setup action for the hit ship button
        setupPlanetLandingButton(); // Setup action for the planet landing button
        setupCrewLandingButton(); // Setup action for the crew landing button
        setupUseBatteriesButton(); // Setup action for the use batteries button
        setupLoadGoodsButton(); // Setup action for the load goods button
        setupFlyButton(); // Setup action for the fly button
        setupDefeatEnemyButton(); // Setup action for the defeat enemy button
    }
    /** * Initializes the dice images based on the current game state.
     * If the GUI is set to throw dice, it will not set the images.
     */
    public void initializeDice(){
        if(!GuiInterface.getInstance().getView().throwableDice()){
            setDice();
        }
    }
    /**
     * Sets the dice images based on the results from the GUI.
     * Disables the dice button after setting the images.
     */
    public void setDice() {
        int result1 = GuiInterface.getInstance().getView().dice1result();
        int result2 = GuiInterface.getInstance().getView().dice2result();
        setDiceImage(dice1Button, result1);
        setDiceImage(dice2Button, result2);
        diceButton.setDisable(true);
    }
    /**
     * Resets the dice buttons to their initial state.
     * Clears the graphics and styles of the dice buttons.
     */
    public void invalidDice(){
        diceButton.setDisable(false);
        dice1Button.setGraphic(null);
        dice2Button.setGraphic(null);
        dice1Button.setStyle("-fx-background-color: #87CEFA;");
        dice2Button.setStyle("-fx-background-color: #87CEFA;");
    }
    /**
     * Initializes the current card image based on the GUI state.
     * If no card is currently picked, it sets a default image.
     */
    public void initializeCurrentCard(){
        Integer imageID = GuiInterface.getInstance().getView().getCurrentCard();
        if (imageID != null) {
            setPickedCardImage(String.valueOf(imageID));
        }else{
            setPickedCardImage("9002");
        }
    }
    /** * Sets the image of a dice button based on the result.
     * @param diceButton The button to set the image on.
     * @param result The result of the dice roll (1-6).
     */
    public void setDiceImage(Button diceButton, int result){
        Image image = diceImageMap.get(String.valueOf(result));
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(diceButton.getPrefWidth());
        imageView.setFitHeight(diceButton.getPrefHeight());

        diceButton.setGraphic(imageView);
        diceButton.setStyle("-fx-padding: 0; -fx-background-color: transparent;");
    }
    /**
     * Sets the image of the picked card button based on the image ID.
     * @param imageID The ID of the image to set on the picked card button.
     */
    public void setPickedCardImage(String imageID) {
        Image image = cardImageMap.get(imageID);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(pickedCardButton.getPrefWidth());
        imageView.setFitHeight(pickedCardButton.getPrefHeight());

        pickedCardButton.setGraphic(imageView);
        pickedCardButton.setStyle("-fx-padding: 0; -fx-background-color: transparent;");
    }

    /**
     * Displays an error message in the error pane.
     * @param message
     */
    public void showError(String message) {
        errorLabel.setText(message);
        fadeInThenOut(errorPane);
    }

    /**
     * Displays the current game phase message in the game state label.
     * @param message
     */
    public void showGamePhase(String message){
        gameStateLabel.setText(message);
    }

    /**
     * Fades in the specified pane, waits for a duration, then fades it out.
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
     * Initializes the game information such as player nickname, color, lost components, credits, and game state.
     */
    public void initializeGameInfo() {
        this.playerNickname = GuiInterface.getInstance().getView().getNickname(); // Get the player's nickname from the GUI interface
        this.playerColor = GuiInterface.getInstance().getView().getColor(); // Get the player's color from the GUI interface
        this.lostComponents = GuiInterface.getInstance().getView().getLostComponents(playerNickname); // Get the number of lost components for the player
        this.credits = GuiInterface.getInstance().getView().getCredits(playerNickname); // Get the player's credits from the GUI interface
        gameStateLabel.setText(GuiInterface.getInstance().getView().getGameState()); // Get the current game state from the GUI interface
        playerNameLabel.setText(playerNickname); // Set the player's nickname in the label
        playerColorLabel.setText("██"); // Set the player's color in the label
        playerColorLabel.setStyle(Color.convertColorIntoStyle(playerColor)); // Set the player's color style in the label
        lostComponentsLabel.setText(String.valueOf(lostComponents)); // Set the number of lost components in the label
        playerCreditsLabel.setText(String.valueOf(credits)); // Set the player's credits in the label
        turnPlayerLabel.setText(GuiInterface.getInstance().getView().getTurnPlayer()); // Set the current turn player in the label
        if(GuiInterface.getInstance().getView().hasAbandoned(playerNickname)){ // Check if the player has abandoned the game
            statusLabel.setText("ABANDONED"); // Set the status label to "ABANDONED"
            statusLabel.setStyle("-fx-text-fill: red;"); // Set the status label text color to red
        }else{ // If the player is still in the game
            statusLabel.setText("IN THE GAME"); // Set the status label to "IN THE GAME"
            statusLabel.setStyle("-fx-text-fill: green;"); // Set the status label text color to green
        }
    }

    /**
     * Initializes the buttons for the flight phase.
     */
    public void initializeButtons(){
        destroyButton.setDisable(true); // Disable the destroy button initially

        List<String> otherPlayerNicknames = GuiInterface.getInstance().getView().getOtherPlayerNicknames(); // Get the nicknames of other players from the GUI interface
        int numberOtherPlayers = otherPlayerNicknames.size(); // Get the number of other players
        player1ShipButton.setDisable(false); // Enable the first player ship button
        player1ShipButton.setText(otherPlayerNicknames.get(0)); // Set the text of the first player ship button to the first other player's nickname
        if(numberOtherPlayers == 1){ // If there is only one other player
            player2ShipButton.setDisable(true); // Disable the second player ship button
            player2ShipButton.setText("no player"); // Set the text of the second player ship button to "no player"
            player3ShipButton.setDisable(true); // Disable the third player ship button
            player3ShipButton.setText("no player"); // Set the text of the third player ship button to "no player"
        }
        if(numberOtherPlayers == 2){ // If there are two other players
            player2ShipButton.setDisable(false); // Enable the second player ship button
            player2ShipButton.setText(otherPlayerNicknames.get(1)); // Set the text of the second player ship button to the second other player's nickname
            player3ShipButton.setDisable(true); // Disable the third player ship button
            player3ShipButton.setText("no player"); // Set the text of the third player ship button to "no player"
        }
        if(numberOtherPlayers == 3){ // If there are three other players
            player2ShipButton.setDisable(false); // Enable the second player ship button
            player2ShipButton.setText(otherPlayerNicknames.get(1)); // Set the text of the second player ship button to the second other player's nickname
            player3ShipButton.setDisable(false); // Enable the third player ship button
            player3ShipButton.setText(otherPlayerNicknames.get(2)); // Set the text of the third player ship button to the third other player's nickname
        }
    }

    /**
     * Initializes the assembled components on the grid based on the player's nickname.
     */
    public void initializeAssembledComponents(){
        List<List<ViewComponent>> assembledComponents = GuiInterface.getInstance().getView().getAssembledComponents(this.playerNickname); // Get the assembled components for the player from the GUI interface
        for(int i = 0; i < assembledComponents.size(); i++){ // Iterate through each row of assembled components
            for(int j = 0; j < assembledComponents.get(i).size(); j++){ // Iterate through each component in the row
                ViewComponent component = assembledComponents.get(i).get(j); // Get the component at the current row and column
                setComponentOnGrid(component, i, j); // Set the component on the grid at the current row and column
            }
        }
    }

    /**
     * Sets a component on the grid based on the ViewComponent object.
     * @param imageID
     * @param orientation
     * @param column
     * @param row
     */
    @Override
    public void setImageOnGrid(String imageID, Orientation orientation, int column, int row) {
        if (imageID.equals("000") || imageID.equals("003")) { // If the image ID is "000" or "003", do not set an image
            return;
        }

        Image image = componentImageMap.get(imageID); // Get the image from the component image map using the image ID

        double cellSize = 90; // Set the size of the cell in the grid

        ImageView imageView = new ImageView(image); // Create an ImageView with the image
        imageView.setFitWidth(cellSize); // Set the width of the ImageView to the cell size
        imageView.setFitHeight(cellSize); // Set the height of the ImageView to the cell size
        imageView.setPreserveRatio(true); // Preserve the aspect ratio of the image

        switch (orientation) { // Set the rotation of the ImageView based on the orientation
            case WEST -> imageView.setRotate(270);
            case SOUTH -> imageView.setRotate(180);
            case EAST -> imageView.setRotate(90);
        }

        Button button = new Button(); // Create a new button to hold the ImageView
        button.setPrefSize(cellSize, cellSize); // Set the preferred size of the button to the cell size
        button.setMinSize(cellSize, cellSize); // Set the minimum size of the button to the cell size
        button.setMaxSize(cellSize, cellSize); // Set the maximum size of the button to the cell size
        button.setStyle("-fx-padding: 0; -fx-background-color: transparent; -fx-border-color: transparent;"); // Set the button style to have no padding and a transparent background and border
        button.setGraphic(imageView); // Set the graphic of the button to the ImageView

        GridPane overlay = new GridPane(); // Create a GridPane to overlay on the button
        overlay.setPrefSize(cellSize, cellSize); // Set the preferred size of the overlay to the cell size
        overlay.setMouseTransparent(true); // Make the overlay mouse transparent so it does not intercept mouse events
        overlay.setPickOnBounds(false); // Ensure the overlay does not intercept mouse events
        overlay.setId("overlay-" + column + "-" + row); // Set the ID of the overlay for identification
        overlay.setHgap(2); // Set the horizontal gap between cells in the overlay
        overlay.setVgap(2); // Set the vertical gap between cells in the overlay

        for (int i = 0; i < 2; i++) { // Create a 2x2 grid for the overlay
            overlay.getColumnConstraints().add(new ColumnConstraints(cellSize / 2));
            overlay.getRowConstraints().add(new RowConstraints(cellSize / 2));
        }

        StackPane cell = new StackPane(button, overlay); // Create a StackPane to hold the button and overlay
        cell.setStyle("-fx-border-color: transparent;"); // Set the cell style to have a transparent border
        myGridPane.add(cell, column, row); // Add the cell to the grid at the specified column and row

        button.setOnAction(event -> { // Set an action for the button when clicked
            if (lastSelectedImageView != null) { // If there is a previously selected image view
                lastSelectedImageView.setEffect(null);
            }

            Glow glow = new Glow(); // Create a Glow effect for the selected image view
            glow.setLevel(0.8); // Set the glow level
            imageView.setEffect(glow); // Apply the glow effect to the selected image view

            selectedColumn = column; // Set the selected column to the current column
            selectedRow = row; // Set the selected row to the current row
            lastSelectedImageView = imageView; // Set the last selected image view to the current image view

            destroyButton.setDisable(false); // Enable the destroy button when a component is selected
        });
    }

    /**
     * Removes a component from the grid at the specified coordinates.
     * @param disable
     */
    public void disableActionButtons(boolean disable){
        flyButton.setDisable(disable); // Disable the fly button
        useBatteriesButton.setDisable(disable); // Disable the use batteries button
        defeatEnemyButton.setDisable(disable); // Disable the defeat enemy button
        hitShipButton.setDisable(disable); // Disable the hit ship button
        loadGoodsButton.setDisable(disable); // Disable the load goods button
        switchGoodsButton.setDisable(disable); // Disable the switch goods button
        crewLandingButton.setDisable(disable); // Disable the crew landing button
        planetLandingButton.setDisable(disable); // Disable the planet landing button
        skipButton.setDisable(disable); // Disable the skip button
    }

    /**
     * Sets up the action for the other player ship buttons.
     * @param button
     */
    @FXML
    private void setupOtherPlayerButton(Button button) {
        button.setOnAction(event -> { // When the button is clicked
            try { // Load the ship board for the selected player
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipBoardL2.fxml"));
                ShipBoardController controller = new ShipBoardControllerL2(button.getText()); // Create a new ShipBoardControllerL2 with the selected player's nickname
                loader.setController(controller); // Set the controller for the FXMLLoader

                Parent root = loader.load(); // Load the FXML file for the ship board

                controller.setServer(this.server); // Set the server for the controller
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.playerColor); // Set the player information for the controller
                GuiInterface.getInstance().setShipBoardController(controller); // Set the ship board controller in the GUI interface

                controller.setControlledStage(controlledStage); // Set the controlled stage for the controller
                controlledStage.setScene(new Scene(root, 1210, 740)); // Set the scene for the controlled stage with the loaded root
                controlledStage.show(); // Show the controlled stage

            } catch (IOException e) {
                showError(e.getMessage());
            }
        });
    }

    /**
     * Sets up the action for the destroy button.
     */
    @FXML
    private void setupDestroyButton() { // When the destroy button is clicked
        destroyButton.setOnAction(event -> { // When the destroy button is clicked
            try{
                server.destroyComponent(this.gameID, this.playerNickname, selectedRow, selectedColumn);
            }
            catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }

    /**
     * Sets up the action for the flight board button.
     */
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


    // Setters for buttons (excluding dice and pickedCard buttons)
    /**
     * Sets up the action for the pick card button.
     * It allows the player to pick the next card in the game.
     */
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
    /**
     * Sets up the action for the dice button.
     * It allows the player to roll the dice and update the game state accordingly.
     */
    @FXML
    public void setupDiceButton() {
        diceButton.setOnAction(event -> {
            try{
                GuiInterface.getInstance().getView().updateRollDice();
                setDice();
            }
            catch (Exception e) { showError(e.getMessage()); }
        });
    }
    /**
     * Sets up the action for the quit button.
     * It allows the player to quit the game.
     */
    public void showPopup(String fxml) {
        try {
            String resourcePath = "/it/polimi/ingsw/galaxytrucker/fxml/actionSettings/" + fxml;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
            Parent popupContent = loader.load();

            ActionSettingsController controller = loader.getController();
            controller.setServer(this.server);
            controller.setPlayerInfo(this.gameID, this.playerNickname);
            controller.setOnConfirm(() -> {
                GuiInterface.getInstance().getView().updateThrowableDice();
                invalidDice();
                hidePopup();
            });

            popupContainer.getChildren().clear();
            popupContainer.getChildren().add(popupContent);
            popupContainer.setVisible(true);
            popupContainer.setMouseTransparent(false);

            popupOpened = true;
            disableActionButtons(true);

        } catch (IOException e) {
            showError(e.getMessage());
        }
    }
    /**
     * Hides the popup container and clears its content.
     * It also resets the popupOpened flag and enables action buttons.
     */
    public void hidePopup() {
        popupContainer.setVisible(false);
        popupContainer.getChildren().clear();
        popupContainer.setMouseTransparent(true);

        popupOpened = false;
        disableActionButtons(false);
    }
    /**
     * Sets up the action for the hit ship button.
     * It allows the player to hit a ship and open the corresponding settings popup.
     */
    public void setupHitShipButton() {
        hitShipButton.setOnAction(event -> {
            if(!diceButton.isDisable()){
                showError("Throw the dice first");
                return;
            }
            if(!popupOpened) {
                showPopup("hitShipSettings.fxml");
                hitShipButton.setDisable(false);
            }else{
                hidePopup();
            }
        });
    }
    /**
     * Sets up the action for the planet landing button.
     * It allows the player to land on a planet and open the corresponding settings popup.
     */
    public void setupPlanetLandingButton() {
        planetLandingButton.setOnAction(event -> {
            if(!popupOpened) {
                showPopup("planetLandingSettings.fxml");
                planetLandingButton.setDisable(false);
            }else{
                hidePopup();
            }
        });
    }
    /**
     * Sets up the action for the crew landing button.
     * It allows the player to land a crew and open the corresponding settings popup.
     */
    public void setupCrewLandingButton() {
        crewLandingButton.setOnAction(event -> {
            if(!popupOpened) {
                showPopup("crewLandingSettings.fxml");
                crewLandingButton.setDisable(false);
            }else{
                hidePopup();
            }
        });
    }
    /**
     * Sets up the action for the switch goods button.
     * It allows the player to switch goods and open the corresponding settings popup.
     */
    public void setupDefeatEnemyButton() {
        defeatEnemyButton.setOnAction(event -> {
            if(!popupOpened) {
                showPopup("defeatEnemySettings.fxml");
                defeatEnemyButton.setDisable(false);
            }else{
                hidePopup();
            }
        });
    }
    /**
     * Sets up the action for the load goods button.
     * It allows the player to load goods and open the corresponding settings popup.
     */
    public void setupLoadGoodsButton() {
        loadGoodsButton.setOnAction(event -> {
            if(!popupOpened) {
                showPopup("loadGoodsSettings.fxml");
                loadGoodsButton.setDisable(false);
            }else{
                hidePopup();
            }
        });
    }
    /**
     * Sets up the action for the switch goods button.
     * It allows the player to switch goods and open the corresponding settings popup.
     */
    public void setupUseBatteriesButton() {
        useBatteriesButton.setOnAction(event -> {
            if(!popupOpened) {
                showPopup("useBatteriesSettings.fxml");
                useBatteriesButton.setDisable(false);
            }else{
                hidePopup();
            }
        });
    }
    /**
     * Sets up the action for the fly button.
     * It allows the player to fly and open the corresponding settings popup.
     */
    public void setupFlyButton() {
        flyButton.setOnAction(event -> {
            if(!popupOpened) {
                showPopup("flySettings.fxml");
                flyButton.setDisable(false);
            }else{
                hidePopup();
            }
        });
    }
/**
     * Sets up the action for the skip button.
     * It allows the player to skip their turn.
     */
    public void setupSkipButton() {
        skipButton.setOnAction(event -> {
            try{
                server.skip(this.gameID, this.playerNickname);
            }
            catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }
    /**
     * Sets up the action for the quit button.
     * It allows the player to quit the game.
     */
    public void setupQuitButton() {
        quitButton.setOnAction(event -> {
            try{
                server.quitGame(this.gameID, this.playerNickname);
            }
            catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }

    /**
     * Sets the stage that this controller will control.
     * @param stage the stage to be controlled
     */
    @Override
    public void setControlledStage(Stage stage) {
        this.controlledStage = stage;
    }

    /**
     * Sets the server for communication with the game server.
     * @param server the server to be used for communication
     */
    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }

    /**
     * Sets the player information for the game.
     * @param gameID          the ID of the game
     * @param playerNickname  the nickname of the player
     * @param color           the color associated with the player
     */
    @Override
    public void setPlayerInfo(int gameID, String playerNickname, Color color) {
        this.gameID = gameID;
        this.playerNickname = playerNickname;
        this.playerColor = color;
    }

    /**
     * Notifies the controller of an error that occurred during the game.
     * @param error the error message to be displayed
     * @throws Exception
     */
    @Override
    public void notifyError(String error) throws Exception {
        Platform.runLater(() -> {
            showError(error);
        });
    }

    /**
     * Notifies the controller of a change in the game phase.
     * @param gamePhase the new game phase to be displayed
     * @throws Exception
     */
    @Override
    public void notifyGamePhase(String gamePhase) throws Exception {
        Platform.runLater(() -> {
            showGamePhase(gamePhase);
        });
    }

    /**
     * Updates the ship repair phase for a specific player.
     * @param nickname the nickname of the player who needs to repair their ship
     * @throws Exception
     */
    @Override
    public void updateShipRepair(String nickname) throws Exception {
        Platform.runLater(() -> {
            showGamePhase("SHIP REPAIR (player " + nickname + ")");
        });
    }

    /**
     * Updates the destroyed component on the player's ship.
     * @param nickname the nickname of the player whose ship component was destroyed
     * @param x        the x-coordinate of the destroyed component
     * @param y        the y-coordinate of the destroyed component
     * @throws Exception
     */
    @Override
    public void updateDestroyedComponent(String nickname, int x, int y) throws Exception {
        Platform.runLater(() -> {
            if(nickname.equals(this.playerNickname)) {
                Platform.runLater(() -> {
                    removeComponentFromGrid(x, y);
                    selectedRow = -1;
                    selectedColumn = -1;
                    lostComponents++;
                    lostComponentsLabel.setText(String.valueOf(lostComponents));
                });
            }
        });
    }

    /**
     * Updates the component change on the player's ship.
     * @param nickname the nickname of the player whose ship component has changed
     * @param x        the x-coordinate of the changed component
     * @param y        the y-coordinate of the changed component
     * @throws Exception
     */
    @Override
    public void updateComponentChange(String nickname, int x, int y) throws Exception {
        Platform.runLater(() -> {
            if(nickname.equals(this.playerNickname)) {
                removeComponentFromGrid(x, y);
                ViewComponent component = GuiInterface.getInstance().getView().getAssembledComponents(nickname).get(x).get(y);
                setComponentOnGrid(component, x , y);
            }
        });
    }

    /**
     * Updates the card picking phase for the game.
     * @throws Exception
     */
    @Override
    public void updateCardPicking() throws Exception {
        Platform.runLater(() -> {
            showGamePhase("CARD PICKING");
            setPickedCardImage("9002");
        });
    }

    /**
     * Updates the next turn for the game.
     * @param nickname the nickname of the player whose turn it is
     * @throws Exception
     */
    @Override
    public void updateNextTurn(String nickname) throws Exception {
        Platform.runLater(() -> {
            turnPlayerLabel.setText(nickname);
            invalidDice();
        });
    }

    /**
     * Updates the card solving phase for the game.
     * @param imageID the ID of the image representing the card to be solved
     * @throws Exception
     */
    @Override
    public void updateCardSolving(int imageID) throws Exception {
        Platform.runLater(() -> {
            showGamePhase("CARD SOLVING");
            setPickedCardImage(String.valueOf(imageID));
        });
    }

    /**
     * Updates the player quit status in the game.
     * @param nickname the nickname of the player who has quit
     * @throws Exception
     */
    @Override
    public void updatePlayerQuit(String nickname) throws Exception {
        Platform.runLater(() -> {
            if(nickname.equals(this.playerNickname)) {
                statusLabel.setText("ABANDONED");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
            showError("Player " + nickname + " has left the game");
        });
    }

    /**
     * Updates the player's credits based on the change in credits.
     * @param nickname the nickname of the player whose credits have changed
     * @param change   the amount of credits gained (positive value) or lost (negative value)
     * @throws Exception
     */
    @Override
    public void updatePlayerCredits(String nickname, int change) throws Exception {
        Platform.runLater(() -> {
            if(nickname.equals(this.playerNickname)) {
                this.credits+=change;
                playerCreditsLabel.setText(String.valueOf(credits));
            }
        });
    }

    /**
     * Updates the end game state of the game.
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
                controller.setPlayerInfo(this.gameID, this.playerNickname, this.playerColor);

                controller.setControlledStage(controlledStage);
                controlledStage.setScene(new Scene(root, 1210, 740));
                controlledStage.show();

            } catch (IOException e) {
                showError(e.getMessage());
            }
        });
    }
}
