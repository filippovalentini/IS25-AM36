package it.polimi.ingsw.galaxytrucker.ui.gui.actionSettingsControllers;

import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ActionSettingsController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class FlyController implements ActionSettingsController {
    @FXML
    private ComboBox<Integer> batteryComboBox;
    @FXML
    private Button confirmButton;

    private VirtualServer server;
    private int gameID;
    private String playerNickname;
    private Runnable onConfirm;

    @FXML
    public void initialize() {
        for (int i = 0; i <= 10; i++) {
            batteryComboBox.getItems().add(i);
        }
        batteryComboBox.setVisibleRowCount(3);

        setupConfirmButton();
    }

    @FXML
    private void setupConfirmButton() {
        confirmButton.setOnAction(e -> {
            try{
                server.fly(gameID, playerNickname, batteryComboBox.getValue());
                onConfirm.run();
            }
            catch(Exception ignored){}
        });
    }

    @Override
    public void setServer(VirtualServer server) {
        this.server = server;
    }

    @Override
    public void setPlayerInfo(int gameID, String playerNickname) {
        this.gameID = gameID;
        this.playerNickname = playerNickname;
    }

    @Override
    public void setOnConfirm(Runnable onConfirm) {
        this.onConfirm = onConfirm;
    }
}
