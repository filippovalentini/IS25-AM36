package it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
import javafx.stage.Stage;
/** * Interface for GUI controllers that manage the interaction between the view and the server.
 */
public interface GuiController {
    /** * Sets the stage that this controller will control.
     *
     * @param stage the stage to be controlled
     */
    void setControlledStage(Stage stage);
    /** * Sets the server that this controller will use to communicate with the game.
     *
     * @param server the server to be used for communication
     */
    void setServer(VirtualServer server);

    //invoked to set the players information needed for method invocation on server
    /** * Sets the player information for the game.
     *
     * @param gameID          the ID of the game
     * @param playerNickname  the nickname of the player
     * @param color           the color associated with the player
     */
    void setPlayerInfo(int gameID, String playerNickname, Color color);

    //notifies the view about an error obtained while performing an action
    /** * Notifies the view about an error that occurred during an action.
     *
     * @param error the error message to be displayed
     * @throws Exception if an error occurs while notifying the view
     */
    void notifyError(String error) throws Exception;

    //notifies the view about a change in the game phase
    /** * Notifies the view about a change in the game phase.
     *
     * @param gamePhase the new game phase to be displayed
     * @throws Exception if an error occurs while notifying the view
     */
    void notifyGamePhase(String gamePhase) throws Exception;
}
