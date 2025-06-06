package it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;

public interface GuiController {
    void setServer(VirtualServer server);

    //invoked to set the players information needed for method invocation on server
    void setPlayerInfo(int gameID, String playerNickname, Color color);

    //notifies the view about an error obtained while performing an action
    void notifyError(String error) throws Exception;

    //notifies the view about a change in the game phase
    void notifyGamePhase(String gamePhase) throws Exception;
}
