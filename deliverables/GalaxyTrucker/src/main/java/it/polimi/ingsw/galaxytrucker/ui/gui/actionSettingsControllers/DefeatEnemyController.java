package it.polimi.ingsw.galaxytrucker.ui.gui.actionSettingsControllers;

import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ActionSettingsController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

/**
 * This controller manages the graphic popup for the "defeat enemy" action
 */
public class DefeatEnemyController implements ActionSettingsController {
    @FXML
    private ComboBox<String> flightDaysComboBox;
    @FXML
    private ComboBox<Integer> batteryComboBox;
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
        for (int i = 0; i <= 10; i++) {
            batteryComboBox.getItems().add(i);
        }
        flightDaysComboBox.getItems().addAll("yes", "no");
        batteryComboBox.setVisibleRowCount(3);
        flightDaysComboBox.setVisibleRowCount(3);


        setupConfirmButton();
    }
    /**
     * Sets up the action for the confirm button, which sends the defeat enemy command to the server.
     */
    @FXML
    private void setupConfirmButton() {
        confirmButton.setOnAction(e -> {
            String flightDays = flightDaysComboBox.getValue();
            boolean loseDays = flightDays.equals("yes");
            Integer batteries = batteryComboBox.getValue();
            try{
                server.defeat(this.gameID, this.playerNickname, batteries, loseDays);
                onConfirm.run();
            }
            catch(Exception ignored){}
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
     * Sets the action to be performed when the confirm button is clicked.
     *
     * @param onConfirm the Runnable action to be executed on confirmation
     */
    @Override
    public void setOnConfirm(Runnable onConfirm) {
        this.onConfirm = onConfirm;
    }
}
