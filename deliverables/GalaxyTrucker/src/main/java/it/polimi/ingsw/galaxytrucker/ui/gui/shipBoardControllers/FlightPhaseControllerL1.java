package it.polimi.ingsw.galaxytrucker.ui.gui.shipBoardControllers;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ActionSettingsController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.FlightPhaseController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBoardController;
import it.polimi.ingsw.galaxytrucker.ui.gui.flightBoardControllers.FlightBoardControllerL1;
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
/** * FlightPhaseControllerL1 is the controller for the flight phase in Level 1 of the Galaxy Trucker game.
 * It manages the user interface for the flight phase, including displaying player information,
 * handling button actions, and updating the game state.
 */
public class FlightPhaseControllerL1 extends ShipBoardGraphics implements FlightPhaseController {
    private Stage controlledStage;

    @FXML
    private Label playerNameLabel;
    @FXML private Label playerColorLabel;
    @FXML private Label playerCreditsLabel;
    @FXML private Label lostComponentsLabel;
    @FXML private Label statusLabel;

    @FXML private Pane errorPane;
    @FXML private Label errorLabel;

    @FXML private Label gameStateLabel;


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
    /** * Initializes the controller after its root element has been completely processed.
     * Loads images, initializes game information, sets up buttons, and prepares the UI for the flight phase.
     */
    @FXML
    public void initialize() {
        componentImageMap = GuiInterface.getInstance().loadImageMap("components");
        cardImageMap = GuiInterface.getInstance().loadImageMap("cards");
        diceImageMap = GuiInterface.getInstance().loadImageMap("diceFaces");

        initializeDice();
        initializeGameInfo();
        initializeButtons();
        initializeAssembledComponents();
        initializeCurrentCard();

        setupDiceButton();
        setupOtherPlayerButton(player1ShipButton);
        setupOtherPlayerButton(player2ShipButton);
        setupOtherPlayerButton(player3ShipButton);
        setupDestroyButton();
        setupFlightBoardButton();
        setupPickCardButton();
        setupQuitButton();

        popupContainer.setMouseTransparent(true);
        setupSkipButton();
        setupHitShipButton();
        setupPlanetLandingButton();
        setupCrewLandingButton();
        setupUseBatteriesButton();
        setupLoadGoodsButton();
        setupFlyButton();
        setupDefeatEnemyButton();
    }

    //UTILITIES CARD METHODS:

    //PlanetLanding
    /** * Sets up the action for the planet landing button.
     * When clicked, it shows a popup with planet landing settings or hides the popup if already opened.
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

    //Crew Landing
    /** * Sets up the action for the crew landing button.
     * When clicked, it shows a popup with crew landing settings or hides the popup if already opened.
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

    //Use Batteries
    /** * Sets up the action for the use batteries button.
     * When clicked, it shows a popup with use batteries settings or hides the popup if already opened.
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

    //Load Goods
    /** * Sets up the action for the load goods button.
     * When clicked, it shows a popup with load goods settings or hides the popup if already opened.
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

    //Fly
    /** * Sets up the action for the fly button.
     * When clicked, it shows a popup with fly settings or hides the popup if already opened.
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

    //defeat enemy
    /** * Sets up the action for the defeat enemy button.
     * When clicked, it shows a popup with defeat enemy settings or hides the popup if already opened.
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



    //Hit Ship Button
    /** * Sets up the action for the hit ship button.
     * When clicked, it checks if the dice have been thrown and shows a popup with hit ship settings or hides the popup if already opened.
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
    /** * Hides the popup container and clears its content.
     * Resets the popup state and enables action buttons.
     */
    public void hidePopup() {
        popupContainer.setVisible(false);
        popupContainer.getChildren().clear();
        popupContainer.setMouseTransparent(true);

        popupOpened = false;
        disableActionButtons(false);
    }
    /** * Disables or enables all action buttons based on the provided boolean value.
     * @param disable true to disable buttons, false to enable them.
     */
    public void disableActionButtons(boolean disable){
        flyButton.setDisable(disable);
        useBatteriesButton.setDisable(disable);
        defeatEnemyButton.setDisable(disable);
        hitShipButton.setDisable(disable);
        loadGoodsButton.setDisable(disable);
        switchGoodsButton.setDisable(disable);
        crewLandingButton.setDisable(disable);
        planetLandingButton.setDisable(disable);
        skipButton.setDisable(disable);
    }
    /** * Shows a popup with the specified FXML file for action settings.
     * Loads the FXML file, sets up the controller, and displays the popup content.
     * @param fxml the name of the FXML file to load.
     */
    public void showPopup(String fxml) {
        try {
            String resourcePath = "/it/polimi/ingsw/galaxytrucker/fxml/actionSettings/" + fxml; // Construct the resource path for the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath)); // Load the FXML file
            Parent popupContent = loader.load(); // Load the content of the popup

            ActionSettingsController controller = loader.getController(); // Get the controller from the loaded FXML
            controller.setServer(this.server); // Set the server for the controller
            controller.setPlayerInfo(this.gameID, this.playerNickname); // Set player information for the controller
            controller.setOnConfirm(() -> { // Define the action to perform when the confirm button is clicked
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
    /** * Invalidates the dice state by enabling the dice button and clearing the dice images.
     * Resets the styles of the dice buttons to their default state.
     */
    public void invalidDice(){
        diceButton.setDisable(false);
        dice1Button.setGraphic(null);
        dice2Button.setGraphic(null);
        dice1Button.setStyle("-fx-background-color: #87CEFA;");
        dice2Button.setStyle("-fx-background-color: #87CEFA;");
    }

    //Skip Button
    /** * Sets up the action for the skip button.
     * When clicked, it sends a skip request to the server for the current game and player.
     * If an error occurs, it displays the error message in the error label.
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

    // Initialize Current Card
    /** * Initializes the current card by retrieving its image ID from the GUI interface.
     * If the image ID is not available, it sets a default image.
     */
    public void initializeCurrentCard(){
        Integer imageID = GuiInterface.getInstance().getView().getCurrentCard();
        if (imageID != null) {
            setPickedCardImage(String.valueOf(imageID));
        }else{
            setPickedCardImage("9001");
        }
    }
    /** * Sets the image of the picked card button based on the provided image ID.
     * It retrieves the image from the cardImageMap, creates an ImageView, and sets it as the graphic of the button.
     * @param imageID the ID of the image to be set on the picked card button.
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

    // Dice setting
    /** * Initializes the dice by checking if the GUI interface allows throwing dice.
     * If not, it sets the dice images based on the results from the GUI interface.
     */
    public void initializeDice(){
        if(!GuiInterface.getInstance().getView().throwableDice()){
            setDice();
        }
    }
    /** * Sets the dice images based on the results from the GUI interface.
     * Retrieves the results of the dice rolls, sets the images on the respective buttons,
     * and disables the dice button to prevent further rolls.
     */
    public void setDice() {
        int result1 = GuiInterface.getInstance().getView().dice1result();
        int result2 = GuiInterface.getInstance().getView().dice2result();
        setDiceImage(dice1Button, result1);
        setDiceImage(dice2Button, result2);
        diceButton.setDisable(true);
    }
    /** * Sets the image of a dice button based on the provided result.
     * It retrieves the image from the diceImageMap, creates an ImageView, and sets it as the graphic of the button.
     * @param diceButton the button to set the dice image on.
     * @param result the result of the dice roll to determine which image to use.
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

    // Initialize Game Info
    /** * Initializes the game information by retrieving player details from the GUI interface.
     * Sets the player's nickname, color, lost components, credits, game state, and other relevant labels.
     */
    public void initializeGameInfo() {
        this.playerNickname = GuiInterface.getInstance().getView().getNickname(); // Get the player's nickname from the GUI interface
        this.playerColor = GuiInterface.getInstance().getView().getColor(); // Get the player's color from the GUI interface
        this.lostComponents = GuiInterface.getInstance().getView().getLostComponents(playerNickname); // Get the number of lost components for the player
        this.credits = GuiInterface.getInstance().getView().getCredits(playerNickname); // Get the player's credits from the GUI interface
        gameStateLabel.setText(GuiInterface.getInstance().getView().getGameState()); // Get the current game state from the GUI interface
        playerNameLabel.setText(playerNickname); // Set the player's name label to the player's nickname
        playerColorLabel.setText("██"); // Set the player's color label to a colored block
        playerColorLabel.setStyle(Color.convertColorIntoStyle(playerColor)); // Set the style of the player's color label based on the player's color
        lostComponentsLabel.setText(String.valueOf(lostComponents)); // Set the lost components label to the number of lost components
        playerCreditsLabel.setText(String.valueOf(credits)); // Set the player's credits label to the number of credits
        turnPlayerLabel.setText(GuiInterface.getInstance().getView().getTurnPlayer()); // Set the turn player label to the current turn player
        if(GuiInterface.getInstance().getView().hasAbandoned(playerNickname)){ // Check if the player has abandoned the game
            statusLabel.setText("ABANDONED"); // Set the status label to "ABANDONED"
            statusLabel.setStyle("-fx-text-fill: red;"); // Set the style of the status label to red
        }else{
            statusLabel.setText("IN THE GAME"); // Set the status label to "IN THE GAME"
            statusLabel.setStyle("-fx-text-fill: green;"); // Set the style of the status label to green
        }
    }

    //initialize Buttons
    /** * Initializes the buttons on the ship board.
     * Disables the destroy button initially and sets up other player ship buttons based on the number of players in the game.
     */
    public void initializeButtons(){
        destroyButton.setDisable(true);

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

    // Initialize Assembled Component
    /** * Initializes the assembled components on the ship board.
     * Retrieves the list of assembled components for the player and sets them on the grid.
     */
    public void initializeAssembledComponents(){
        List<List<ViewComponent>> assembledComponents = GuiInterface.getInstance().getView().getAssembledComponents(this.playerNickname);
        for(int i = 0; i < assembledComponents.size(); i++){
            for(int j = 0; j < assembledComponents.get(i).size(); j++){
                ViewComponent component = assembledComponents.get(i).get(j);
                setComponentOnGrid(component, i, j);
            }
        }
    }

    //set Images methods

    /**
     * Sets a component on the grid based on the provided ViewComponent.
     * @param imageID
     * @param orientation
     * @param column
     * @param row
     */
    @Override
    public void setImageOnGrid(String imageID, Orientation orientation, int column, int row) {
        if (imageID.equals("000") || imageID.equals("003")) {
            return;
        }

        Image image = componentImageMap.get(imageID);

        double cellSize = 90;

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

            destroyButton.setDisable(false);
        });
    }

    //Show Errors
    /** * Displays an error message in the error label and fades it in and out after a short duration.
     * @param message the error message to be displayed.
     */
    public void showError(String message) {
        errorLabel.setText(message);
        fadeInThenOut(errorPane);
    }
    /** * Fades in the specified pane, waits for a duration, and then fades it out.
     * @param pane the pane to be faded in and out.
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

    //Quit button
    /** * Sets up the action for the quit button.
     * When clicked, it sends a quit request to the server for the current game and player.
     * If an error occurs, it displays the error message in the error label.
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
    /** * Sets up the action for the dice button.
     * When clicked, it sends a request to roll the dice and updates the dice images accordingly.
     * If an error occurs, it displays the error message in the error label.
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
    /** * Sets up the action for the other player ship buttons.
     * When clicked, it loads the ship board for the selected player and displays it in the controlled stage.
     * If an error occurs, it displays the error message in the error label.
     * @param button the button representing another player's ship.
     */
    @FXML
    private void setupOtherPlayerButton(Button button) {
        button.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipBoardL1.fxml"));
                ShipBoardController controller = new ShipBoardControllerL1(button.getText());
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
    /** * Sets up the action for the destroy button.
     * When clicked, it sends a request to destroy a component at the selected row and column.
     * If an error occurs, it displays the error message in the error label.
     */
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
    /** * Sets up the action for the flight board button.
     * When clicked, it loads the flight board for Level 1 and displays it in the controlled stage.
     * If an error occurs, it displays the error message in the error label.
     */
    @FXML
    private void setupFlightBoardButton() {
        flightBoardButton.setOnAction(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/flightBoardL1.fxml"));
                Parent root = fxmlLoader.load();

                FlightBoardControllerL1 controller = fxmlLoader.getController();
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
    /** * Sets up the action for the pick card button.
     * When clicked, it sends a request to pick the next card for the current game and player.
     * If an error occurs, it displays the error message in the error label.
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
    /** * Sets the controlled stage for this controller.
     * @param stage the stage to be controlled by this controller.
     */
    @Override
    public void setControlledStage(Stage stage) {
        this.controlledStage = stage;
    }
    /** * Sets the server for this controller.
     * @param server the virtual server to be used by this controller.
     */
    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }
    /** * Sets the player information for this controller.
     * @param gameID the ID of the game the player is in.
     * @param playerNickname the nickname of the player.
     * @param color the color associated with the player.
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
     * Updates the destroyed component on the player's ship grid.
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
     * Updates the ship component change on the player's ship grid.
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
    /** * Displays the current game phase message in the game state label.
     * @param message the message to be displayed for the current game phase.
     */
    public void showGamePhase(String message){
        gameStateLabel.setText(message);
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
     * It loads the endgame FXML file, sets the controller, and displays the endgame screen.
     * @throws Exception if an error occurs while loading the FXML file or setting up the controller.
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


