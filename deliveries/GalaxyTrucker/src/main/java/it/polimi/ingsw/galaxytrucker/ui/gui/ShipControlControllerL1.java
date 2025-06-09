package it.polimi.ingsw.galaxytrucker.ui.gui;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipControlController;
import javafx.stage.Stage;

public class ShipControlControllerL1 implements ShipControlController {
    private Stage controlledStage;

    @Override
    public void updateDestroyedComponent(String nickname, int x, int y) throws Exception {

    }

    @Override
    public void updateCrewChange(String nickname, int x, int y, int change) throws Exception {

    }

    @Override
    public void updateBatteries(String nickname, int x, int y, int change) throws Exception {

    }

    @Override
    public void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) throws Exception {

    }

    @Override
    public void updateCardPicking() throws Exception {

    }

    @Override
    public void setControlledStage(Stage stage) {
        controlledStage = stage;
    }

    @Override
    public void setServer(VirtualServer server) {

    }

    @Override
    public void setPlayerInfo(int gameID, String playerNickname, Color color) {

    }

    @Override
    public void notifyError(String error) throws Exception {

    }

    @Override
    public void notifyGamePhase(String gamePhase) throws Exception {

    }
}
