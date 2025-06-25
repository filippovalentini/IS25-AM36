package it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces;

import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
/** * Interface for controllers that handle action settings in the GUI.
 * It allows setting the server, player information, and a callback for confirmation actions.
 */
public interface ActionSettingsController {
    /**
     * Sets the server.
     *
     * @param server
     */
    void setServer(VirtualServer server);

    //invoked to set the players information needed for method invocation on server
    /**
     * Sets the player information.
     *
     * @param gameID the ID of the game
     * @param playerNickname the nickname of the player
     */
    void setPlayerInfo(int gameID, String playerNickname);

    //invoked when the confirm button is clicked to generate a callback to the controller of the main scene
    /**
     * Sets the action to be performed when the confirm button is clicked.
     *
     * @param onConfirm the action to perform on confirmation
     */
    void setOnConfirm(Runnable onConfirm);
}
