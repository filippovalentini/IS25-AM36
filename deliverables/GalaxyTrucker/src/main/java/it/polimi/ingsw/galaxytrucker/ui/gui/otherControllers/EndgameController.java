package it.polimi.ingsw.galaxytrucker.ui.gui.otherControllers;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.GuiController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
/** * Controller for the endgame screen.
 */
public class EndgameController implements GuiController {
    private Stage controlledStage;

    private VirtualServer server;
    private int gameID;
    private String playerNickname;
    private Color color;

    @FXML
    private Button quitButton;

    @FXML
    private Button findAnotherGameButton;

    @FXML
    private ListView<String> playerList;
    /** Initializes the controller and sets the current players in the list view.
     */
    @FXML
    private void initialize() {
        setCurrentPlayers();
    }
    /** Sets the current players in the list view.
     * This method retrieves the final ranking from the GuiInterface and updates the playerList ListView.
     */
    @FXML
    private void setCurrentPlayers() {
        List<String> finalRankingList = GuiInterface.getInstance().getView().getFinalRanking(); // Retrieves the final ranking from the GuiInterface
        Platform.runLater(() -> { // Updates the playerList ListView on the JavaFX Application Thread
            playerList.getItems().setAll(finalRankingList); // Sets the items of the playerList ListView to the final ranking list
        });
    }
    /** Handles the click event for the "Find Another Game" button.
     * It loads the GameSetupController and sets the controlled stage to allow the user to find another game.
     */
    @FXML
    private void onFindAnotherGameClick(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/galaxytrucker/fxml/mainScreens/selectNetwork.fxml"));
            Parent root = fxmlLoader.load(); // Loads the FXML file for the GameSetupController
            GameSetupController controller = fxmlLoader.getController(); // Gets the controller instance from the loaded FXML
            controller.setControlledStage(controlledStage); // Sets the controlled stage for the GameSetupController
            Scene scene = new Scene(root, 1210, 740); // Creates a new Scene with the loaded root and specified dimensions
            controlledStage.setScene(scene); // Sets the scene for the controlled stage
            controlledStage.show(); // Shows the controlled stage
        } catch (IOException e) { // Handles any IOException that may occur during the loading of the FXML file
            throw new RuntimeException(e);
        }
    }
    /** Handles the click event for the "Quit" button.
     * It exits the application and terminates the program.
     */
    @FXML
    private void onQuitButtonClick(){
        Platform.exit();
        System.exit(0);
    }
    /** Sets the player information for the endgame screen.
     * @param gameID The ID of the game.
     * @param playerNickname The nickname of the player.
     * @param color The color associated with the player.
     */
    @Override
    public void setPlayerInfo(int gameID, String playerNickname, Color color) {
        this.playerNickname = playerNickname;
        this.color = color;
        this.gameID = gameID;
    }
    /** Sets the controlled stage for the endgame screen.
     * @param stage The stage to be controlled by this controller.
     */
    @Override
    public void setControlledStage(Stage stage) {
        this.controlledStage = stage;
    }
    /** Sets the server for the endgame screen.
     * @param server The VirtualServer instance to be used by this controller.
     */
    public void setServer(VirtualServer server) {
        this.server = server;
    }
    /** Notifies the controller that the game has ended.
     * This method is called when the game ends, and it updates the endgame screen accordingly.
     */
    @Override
    public void notifyError(String error) {}
    /** Notifies the controller that the game has ended.
     * This method is called when the game ends, and it updates the endgame screen accordingly.
     */
    @Override
    public void notifyGamePhase(String gamePhase) throws Exception {

    }
}
