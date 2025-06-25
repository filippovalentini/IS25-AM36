package it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
/** * Interface for the Flight Phase Controller, which handles the interactions during the flight phase of the game.
 * It extends GuiController to provide methods for updating the GUI based on game events.
 */
public interface FlightPhaseController extends GuiController{
    //notifies the view that a player has to repair its ship board before the player in turn can pick a new card
    /**
     * Notifies the view that a player has to repair its ship board before the player in turn can pick a new card.
     *
     * @param nickname the nickname of the player who needs to repair their ship
     * @throws Exception if an error occurs while updating the view
     */
    void updateShipRepair(String nickname) throws Exception;

    //notifies the view that a component of a player's ship board has been destroyed
    /**
     * Notifies the view that a component of a player's ship board has been destroyed.
     *
     * @param nickname the nickname of the player whose ship component was destroyed
     * @param x        the x-coordinate of the destroyed component
     * @param y        the y-coordinate of the destroyed component
     * @throws Exception if an error occurs while updating the view
     */
    void updateDestroyedComponent(String nickname, int x, int y) throws Exception;

    //notifies the view that a component of the ship board has changed and must be reloaded
    /**
     * Notifies the view that a component of the ship board has changed and must be reloaded.
     *
     * @param nickname the nickname of the player whose ship component has changed
     * @param x        the x-coordinate of the changed component
     * @param y        the y-coordinate of the changed component
     * @throws Exception if an error occurs while updating the view
     */
    void updateComponentChange(String nickname, int x, int y) throws Exception;

    //notifies the view about the fact that a player has to pick a card in order to continue the game
    /**
     * Notifies the view that a player has to pick a card in order to continue the game.
     *
     * @throws Exception if an error occurs while updating the view
     */
    void updateCardPicking() throws Exception;

    //notifies the view about the next player whose turn it is to perform an action
    /**
     * Notifies the view about the next player whose turn it is to perform an action.
     *
     * @param nickname the nickname of the player whose turn it is
     * @throws Exception if an error occurs while updating the view
     */
    void updateNextTurn(String nickname) throws Exception;

    //notifies the view that a new card has been picked and must be solved
    /**
     * Notifies the view that a new card has been picked and must be solved.
     *
     * @param imageID the ID of the image representing the card to be solved
     * @throws Exception if an error occurs while updating the view
     */
    void updateCardSolving(int imageID) throws Exception;

    //notifies the view that a player has quit the game
    /**
     * Notifies the view that a player has quit the game.
     *
     * @param nickname the nickname of the player who has quit
     * @throws Exception if an error occurs while updating the view
     */
    void updatePlayerQuit(String nickname) throws Exception;

    //notifies the view that a player has gained/lost credits
    /**
     * Notifies the view that a player has gained or lost credits.
     *
     * @param nickname the nickname of the player whose credits have changed
     * @param change   the amount of credits gained (positive value) or lost (negative value)
     * @throws Exception if an error occurs while updating the view
     */
    void updatePlayerCredits(String nickname, int change) throws Exception;

    //notifies the view about the fact that the game is finished
    /**
     * Notifies the view that the game is finished.
     *
     * @throws Exception if an error occurs while updating the view
     */
    void updateEndGame() throws Exception;
}
