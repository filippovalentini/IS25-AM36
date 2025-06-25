package it.polimi.ingsw.galaxytrucker.ui.gui.otherControllers;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.GuiController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBuildingController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Map;
/** * LobbyController is responsible for managing the lobby screen.
 */
public class LobbyController implements GuiController {
    private Stage controlledStage;

    private VirtualServer server;
    private int gameID;
    private String playerNickname;
    private Color color;

    @FXML
    private ListView<String> playerList;

    @FXML
    private Label timerLabel;

    @FXML
    private Label timerTitle;
    /** Initializes the LobbyController.
     * Sets the current players in the lobby and hides the timer label and title.
     */
    @FXML
    private void initialize() {
        setCurrentPlayers();
        timerLabel.setVisible(false);
        timerTitle.setVisible(false);
    }
    /** Sets the current players in the lobby.
     * This method retrieves the current players from the GuiInterface and updates the playerList ListView.
     */
    @FXML
    public void setCurrentPlayers() {
        Map<String, Color> currentPlayers = GuiInterface.getInstance().getView().getCurrentPlayers();
        Platform.runLater(() -> {
            playerList.getItems().clear();
            for (String nickname : currentPlayers.keySet()) {
                playerList.getItems().add(nickname);
            }
        });
    }

    /** Adds a new player to the lobby.
     * @param nickname The nickname of the player to be added.
     * @param color The color associated with the player.
     * This method updates the playerList ListView with the new player's nickname.
     */
    public void addPlayer(String nickname, Color color) {
        Platform.runLater(() -> {
            playerList.getItems().add(nickname);
        });
    }

    /**
     * Starts a countdown timer for the lobby.
     * @param firstFlight
     */
    public void startTimer(boolean firstFlight) {
        Platform.runLater(() -> { // This method is run on the JavaFX Application Thread
            timerTitle.setVisible(true);
            timerLabel.setVisible(true);

            int[] seconds = {3}; // Using an array to allow modification inside the Timeline lambda
            timerLabel.setText(String.valueOf(seconds[0])); // Initialize the timer label with the starting seconds

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> { // This code runs every second
                seconds[0]--; // Decrement the seconds count
                if (seconds[0] > 0) { // If there are still seconds left, update the label
                    timerLabel.setText(String.valueOf(seconds[0])); // Update the timer label with the remaining seconds
                } else { // If the timer reaches zero, hide the timer and switch to the assembling phase
                    timerTitle.setVisible(false); // Hide the timer title
                    timerLabel.setVisible(false); // Hide the timer label
                    try { // Switch to the assembling phase
                        switchToAssemblingPhase(firstFlight); // Call the method to switch to the assembling phase
                    } catch (IOException e) { // If an IOException occurs, print the stack trace and throw a RuntimeException
                        throw new RuntimeException(e);
                    }
                }
            }));
            timeline.setCycleCount(3);
            timeline.play();
        });
    }
    /** Switches to the assembling phase of the game.
     * @param firstFlight Indicates whether this is the first flight of the game.
     * @throws IOException If there is an error loading the FXML file for the assembling phase.
     */
    public void switchToAssemblingPhase(boolean firstFlight) throws IOException {
        ShipBuildingController controller; // Controller for the ship building phase
        Parent root; // Root node for the scene
        FXMLLoader fxmlLoader; // FXMLLoader to load the FXML file
        if (firstFlight) { // If this is the first flight, load the FXML for level 1 ship building
            fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipBuildingL1.fxml"));
        }else{ // If this is not the first flight, load the FXML for level 2 ship building
            fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/shipBuildingL2.fxml"));
        }
        root = fxmlLoader.load(); // Load the FXML file and create the root node
        controller = fxmlLoader.getController(); // Get the controller from the FXMLLoader
        GuiInterface.getInstance().setShipBuildingController(controller); // Set the controller in the GuiInterface
        controller.setServer(this.server); // Set the server in the controller
        controller.setPlayerInfo(this.gameID, this.playerNickname, this.color); // Set the player information in the controller
        controller.setControlledStage(controlledStage); // Set the controlled stage in the controller
        Scene scene = new Scene(root, 1210, 740); // Create a new scene with the root node and specified dimensions
        controlledStage.setScene(scene); // Set the scene in the controlled stage
        controlledStage.show(); // Show the controlled stage
    }

    /** Sets the controlled stage for this controller.
     * @param stage The Stage to be controlled by this controller.
     */
    @Override
    public void setControlledStage(Stage stage) {
        this.controlledStage = stage;
    }
    /** Sets the server for this controller.
     * @param server The VirtualServer to be used by this controller.
     */
    public void setServer(VirtualServer server) {
        this.server = server;
    }


    //invoked to set the players information needed for method invocation on server
    /** Sets the player information for this controller.
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
    /** Notifies the controller that the game has started.
     * This method is called when the game starts, and it can be used to perform any necessary setup or initialization.
     */
    @Override
    public void notifyError(String error) {}
    /** Notifies the controller that the game has started.
     * @throws Exception If an error occurs during the notification.
     */
    @Override
    public void notifyGamePhase(String gamePhase) throws Exception {

    }

}