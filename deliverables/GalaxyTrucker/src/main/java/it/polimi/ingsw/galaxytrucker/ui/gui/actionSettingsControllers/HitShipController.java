package it.polimi.ingsw.galaxytrucker.ui.gui.actionSettingsControllers;

import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ActionSettingsController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

/**
 * This controller manages the graphic popup for the "hit ship" action
 */
public class HitShipController implements ActionSettingsController {
    @FXML
    private ComboBox<String> shieldComboBox;
    @FXML
    private ComboBox<String> cannonComboBox;
    @FXML
    private Button confirmButton;

    private VirtualServer server;
    private int gameID;
    private String playerNickname;
    private Runnable onConfirm;
    /**
     * Initializes the controller by populating the combo boxes and setting up the confirm button action.
     */
    @FXML
    public void initialize() {
        shieldComboBox.getItems().addAll("yes", "no");
        cannonComboBox.getItems().addAll("yes", "no");

        setupConfirmButton();
    }
    /**
     * Sets up the action for the confirm button, which sends the hit ship request to the server.
     */
    @FXML
    private void setupConfirmButton() {
        confirmButton.setOnAction(e -> {
            String shield = shieldComboBox.getValue(); // Get the selected value from the shield combo box
            boolean useShield = shield.equals("yes"); // Check if the selected value is "yes"
            String cannon = cannonComboBox.getValue(); // Get the selected value from the cannon combo box
            boolean useCannon = cannon.equals("yes"); // Check if the selected value is "yes"
            int diceResult = GuiInterface.getInstance().getView().diceResult(); // Get the dice result from the GUI interface
            try{
                server.hitShip(this.gameID, this.playerNickname, diceResult, useShield, useCannon); // Send the hit ship request to the server with the provided parameters
                onConfirm.run(); // Execute the onConfirm runnable if provided
            }
            catch(Exception ignored){}
        });
    }
    /**
     * Sets the server instance for this controller.
     *
     * @param server the VirtualServer instance to set
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
     * Sets the action to be executed when the confirm button is pressed.
     *
     * @param onConfirm the Runnable to execute on confirmation
     */
    @Override
    public void setOnConfirm(Runnable onConfirm) {
        this.onConfirm = onConfirm;
    }
}
