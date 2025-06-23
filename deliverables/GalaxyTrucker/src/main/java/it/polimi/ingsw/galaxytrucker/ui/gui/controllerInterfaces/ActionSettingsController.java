package it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces;

import it.polimi.ingsw.galaxytrucker.network.VirtualServer;

public interface ActionSettingsController {
    void setServer(VirtualServer server);

    //invoked to set the players information needed for method invocation on server
    void setPlayerInfo(int gameID, String playerNickname);

    //invoked when the confirm button is clicked to generate a callback to the controller of the main scene
    void setOnConfirm(Runnable onConfirm);
}
