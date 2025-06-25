package it.polimi.ingsw.galaxytrucker.ui.gui.actionSettingsControllers;

import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ActionSettingsController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

/**
 * This controller manages the graphic popup for the "use batteries" action
 */
public class UseBatteriesController implements ActionSettingsController {
    @FXML
    private ComboBox<Integer> batteryComboBox;
    @FXML
    private Button confirmButton;

    private VirtualServer server;
    private int gameID;
    private String playerNickname;
    private Runnable onConfirm;
    /**
     * Initializes the controller by populating the batteryComboBox with values from 0 to 10
     * and setting up the confirm button action.
     */
    @FXML
    public void initialize() {
        for (int i = 0; i <= 10; i++) {
            batteryComboBox.getItems().add(i);
        }
        batteryComboBox.setVisibleRowCount(3);

        setupConfirmButton();
    }
    /**
     * Sets up the action for the confirm button. When clicked, it retrieves the selected number of batteries
     * from the batteryComboBox and sends a request to the server to use that many batteries.
     * If successful, it runs the onConfirm runnable.
     */
    @FXML
    private void setupConfirmButton() {
        confirmButton.setOnAction(e -> { // When the confirm button is clicked
            int selectedBatteries = batteryComboBox.getValue(); // Get the selected number of batteries from the combo box
            try{
                server.useBatteries(this.gameID, this.playerNickname, selectedBatteries); // Send the request to the server to use the selected number of batteries
                onConfirm.run(); // Run the onConfirm runnable if the request was successful
            }
            catch(Exception ignored){} // If an exception occurs (e.g., if the server is not connected), it is ignored
        });
    }
    /**
     * Sets the server instance for this controller.
     *
     * @param server the VirtualServer instance to be used for communication
     */
    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }
    /**
     * Sets the player information for this controller, including the game ID and player nickname.
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
     * Sets the action to be performed when the confirm button is clicked.
     * @param onConfirm
     */
    @Override
    public void setOnConfirm(Runnable onConfirm) {
        this.onConfirm = onConfirm;
    }
}
