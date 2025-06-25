package it.polimi.ingsw.galaxytrucker.ui.gui.actionSettingsControllers;

import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ActionSettingsController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

/**
 * This controller manages the graphic popup for the "planet landing" action
 */
public class PlanetLandingController implements ActionSettingsController {
    @FXML
    private ComboBox<Integer> planetComboBox;
    @FXML
    private Button confirmButton;

    private VirtualServer server;
    private int gameID;
    private String playerNickname;
    private Runnable onConfirm;
    /**
     * Initializes the controller by populating the planetComboBox with planet numbers
     * and setting up the confirm button action.
     */
    @FXML
    public void initialize() {
        for (int i = 1; i <= 4; i++) {
            planetComboBox.getItems().add(i);
        }
        planetComboBox.setVisibleRowCount(3);

        setupConfirmButton();
    }
    /**
     * Sets up the confirm button to send the selected planet number to the server
     * when clicked, and then runs the onConfirm action.
     */
    @FXML
    private void setupConfirmButton() { // Sets the action for the confirm button
        confirmButton.setOnAction(e -> { // When the button is clicked
            int planetNumber = planetComboBox.getValue(); // Get the selected planet number
            try{ // Attempt to send the planet landing request to the server
                server.planetLanding(this.gameID, this.playerNickname, planetNumber-1);
                onConfirm.run();
            }
            catch(Exception ignored){} // If an exception occurs, it is ignored (could be improved with proper error handling)
        });
    }
    /**
     * Sets the server instance for this controller.
     *
     * @param server the VirtualServer instance to be used
     */
    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }
    /**
     * Sets the player information for this controller.
     *
     * @param gameID the ID of the game
     * @param playerNickname the nickname of the player
     */
    @Override
    public void setPlayerInfo(int gameID, String playerNickname) {
        this.gameID = gameID;
        this.playerNickname = playerNickname;
    }
    /**
     * Sets the action to be executed when the confirm button is clicked.
     *
     * @param onConfirm the Runnable action to be executed
     */
    @Override
    public void setOnConfirm(Runnable onConfirm) {
        this.onConfirm = onConfirm;
    }
}
