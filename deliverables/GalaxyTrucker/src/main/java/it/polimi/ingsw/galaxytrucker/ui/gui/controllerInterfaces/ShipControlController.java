package it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces;
/**
 * Interface for the Ship Control Controller in the GUI.
 * This interface defines methods to update the view based on changes in the ship's state.
 */
public interface ShipControlController extends GuiController{
    //notifies the view that a component of a player's ship board has been destroyed
    /**
     * Updates the view when a component of a player's ship board has been destroyed.
     *
     * @param nickname the nickname of the player whose ship board component was destroyed
     * @param x the x-coordinate of the destroyed component
     * @param y the y-coordinate of the destroyed component
     * @throws Exception if an error occurs during the update
     */
    void updateDestroyedComponent(String nickname, int x, int y) throws Exception;

    //notifies the view about a change in the number of crew of a cabin
    /**
     * Updates the view about a change in the number of crew members in a cabin.
     *
     * @param nickname the nickname of the player whose cabin's crew count has changed
     * @param x the x-coordinate of the cabin
     * @param y the y-coordinate of the cabin
     * @param change the change in the number of crew members (positive or negative)
     * @throws Exception if an error occurs during the update
     */
    void updateCrewChange(String nickname, int x, int y, int change) throws Exception;

    //notifies the view that a player has initialized a battery container with batteries
    /**
     * Updates the view when a player initializes a battery container with batteries.
     *
     * @param nickname the nickname of the player who initialized the battery container
     * @param x the x-coordinate of the battery container
     * @param y the y-coordinate of the battery container
     * @param change the number of batteries added to the container
     * @throws Exception if an error occurs during the update
     */
    void updateBatteries(String nickname, int x, int y, int change) throws Exception;

    //notifies the view about a change in the number of aliens of a cabin
    /**
     * Updates the view about a change in the number of aliens in a cabin.
     *
     * @param nickname the nickname of the player whose cabin's alien count has changed
     * @param x the x-coordinate of the cabin
     * @param y the y-coordinate of the cabin
     * @param isPurple true if the alien is purple, false otherwise
     * @param added true if an alien was added, false if one was removed
     * @throws Exception if an error occurs during the update
     */
    void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) throws Exception;

    //notifies the view about the fact that a player has to pick a card in order to continue the game
    /**
     * Updates the view to indicate that a player needs to pick a card to continue the game.
     *
     * @throws Exception if an error occurs during the update
     */
    void updateCardPicking() throws Exception;
}
