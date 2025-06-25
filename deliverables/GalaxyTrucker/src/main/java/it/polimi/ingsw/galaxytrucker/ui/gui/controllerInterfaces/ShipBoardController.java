package it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
/** * Interface for the ShipBoardController, which is responsible for managing the ship board interactions in the GUI.
 */
public interface ShipBoardController extends GuiController {
    //notifies the view about the fact that a player (identified by the nickname parameter) has picked a reserved
    //component/ reserved a component (depending on the value of the boolean parameter); the parameter imageID
    //is needed for the view in order to show the right component to the user
    /**
     * Updates the view when a player picks or releases a reserved component.
     *
     * @param nickname the nickname of the player who picked or released the component
     * @param imageID the ID of the component image
     * @param released true if the component was released, false if it was picked
     * @throws Exception if an error occurs during the update
     */
    void updateReservedComponent(String nickname, int imageID, boolean released) throws Exception;

    //notifies the view about the fact that a player (identified by the nickname parameter) has assembled a
    //component in position (x,y) of its ship board; the parameter imageID is needed for the view in order
    //to show the right component to the user
    /**
     * Updates the view when a player assembles a component on their ship board.
     *
     * @param nickname the nickname of the player who assembled the component
     * @param imageID the ID of the component image
     * @param orientation the orientation of the component
     * @param x the x-coordinate of the component on the ship board
     * @param y the y-coordinate of the component on the ship board
     * @throws Exception if an error occurs during the update
     */
    void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws Exception;

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase
    /**
     * Notifies the view that all players have finished the assembling phase and are now in the ship control phase.
     *
     * @throws Exception if an error occurs during the update
     */
    void updateShipControl() throws Exception;

    //notifies the view that a player has to repair its ship board before the player in turn can pick a new card
    /**
     * Notifies the view that a player needs to repair their ship board.
     *
     * @param nickname the nickname of the player who needs to repair their ship
     * @throws Exception if an error occurs during the update
     */
    void updateShipRepair(String nickname) throws Exception;

    //notifies the view that a component of a player's ship board has been destroyed
    /**
     * Notifies the view that a component of a player's ship board has been destroyed.
     *
     * @param nickname the nickname of the player whose component was destroyed
     * @param x the x-coordinate of the destroyed component
     * @param y the y-coordinate of the destroyed component
     * @throws Exception if an error occurs during the update
     */
    void updateDestroyedComponent(String nickname, int x, int y) throws Exception;

    //notifies the view that a component of the ship board has changed and must be reloaded
    /**
     * Notifies the view that a component of the ship board has changed and must be reloaded.
     *
     * @param nickname the nickname of the player whose component has changed
     * @param x the x-coordinate of the changed component
     * @param y the y-coordinate of the changed component
     * @throws Exception if an error occurs during the update
     */
    void updateComponentChange(String nickname, int x, int y) throws Exception;

    //notifies the view about the fact that a player has to pick a card in order to continue the game
    /**
     * Notifies the view that a player needs to pick a card to continue the game.
     *
     * @throws Exception if an error occurs during the update
     */
    void updateCardPicking() throws Exception;

    //notifies the view that a new card has been picked and must be solved
    /**
     * Notifies the view that a new card has been picked and must be solved.
     *
     * @param imageID the ID of the image representing the card
     * @throws Exception if an error occurs during the update
     */
    void updateCardSolving(int imageID) throws Exception;

    //notifies the view that a player has quit the game
    /**
     * Notifies the view that a player has quit the game.
     *
     * @param nickname the nickname of the player who quit
     * @throws Exception if an error occurs during the update
     */
    void updatePlayerQuit(String nickname) throws Exception;

    //notifies the view that a player has gained/lost credits
    /**
     * Updates the player's credits by a specified amount.
     *
     * @param nickname the nickname of the player whose credits are being updated
     * @param change the amount to change the player's credits by (can be positive or negative)
     * @throws Exception if an error occurs during the update
     */
    void updatePlayerCredits(String nickname, int change) throws Exception;

    //notifies the view about the fact that the game is finished
    /**
     * Notifies the view that the game has ended and updates the end game state.
     *
     * @throws Exception if an error occurs during the update
     */
    void updateEndGame() throws Exception;
}
