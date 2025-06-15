package it.polimi.ingsw.galaxytrucker.ui.gui.actionSettingsControllers;

import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ActionSettingsController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class PlanetLandingController implements ActionSettingsController {
    @FXML
    private ComboBox<Integer> planetComboBox;
    @FXML
    private Button confirmButton;

    private VirtualServer server;
    private int gameID;
    private String playerNickname;
    private Runnable onConfirm;

    @FXML
    public void initialize() {
        for (int i = 1; i <= 4; i++) {
            planetComboBox.getItems().add(i);
        }
        planetComboBox.setVisibleRowCount(3);

        setupConfirmButton();
    }

    @FXML
    private void setupConfirmButton() {
        confirmButton.setOnAction(e -> {
            int planetNumber = planetComboBox.getValue();
            try{
                server.planetLanding(this.gameID, this.playerNickname, planetNumber-1);
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
