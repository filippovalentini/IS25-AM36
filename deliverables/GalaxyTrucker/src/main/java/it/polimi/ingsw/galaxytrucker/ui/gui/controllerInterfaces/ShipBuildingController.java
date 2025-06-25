package it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;
/** * This interface defines the methods that the ShipBuildingController must implement in order to notify the
 * view.
 */
public interface ShipBuildingController extends GuiController {
    //notifies the view about the fact that a component has been successfully picked/released (depending on
    //the value of the boolean parameter) by the corresponding player; the parameter imageID is needed for the
    //view in order to show the right component to the user
    /**
     * Notifies the view about the fact that a component has been successfully picked or released by the corresponding player.
     * @param imageID the ID of the component image
     * @param released true if the component has been released, false if it has been picked
     * @throws Exception if an error occurs during the update
     */
    void updatePickedComponent(int imageID, boolean released) throws Exception;

    //notifies the view about the fact that a player (identified by the nickname parameter) has picked a reserved
    //component/ reserved a component (depending on the value of the boolean parameter); the parameter imageID
    //is needed for the view in order to show the right component to the user
    /**
     * Notifies the view about the fact that a player has picked or reserved a component.
     * @param nickname the nickname of the player
     * @param imageID the ID of the component image
     * @param released true if the component has been released, false if it has been picked
     * @throws Exception if an error occurs during the update
     */
    void updateReservedComponent(String nickname, int imageID, boolean released) throws Exception;

    //notifies the view about the fact that the picked component of the corresponding player has been rotated
    /**
     * Notifies the view about the fact that the picked component of the corresponding player has been rotated.
     * @throws Exception if an error occurs during the update
     */
    void updateRotatePickedComponent() throws Exception;

    //notifies the view about the fact that a player (identified by the nickname parameter) has assembled a
    //component in position (x,y) of its ship board; the parameter imageID is needed for the view in order
    //to show the right component to the user
    /**
     * Notifies the view about the fact that a player has assembled a component in a specific position of its ship board.
     * @param nickname the nickname of the player
     * @param imageID the ID of the component image
     * @param orientation the orientation of the component
     * @param x the x coordinate of the position on the ship board
     * @param y the y coordinate of the position on the ship board
     * @throws Exception if an error occurs during the update
     */
    void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws Exception;

    //notifies the view about the fact that a player has finished the assembling phase and is
    //correctly positioned on the flight board; still, other players have to finish building their ships
    /**
     * Notifies the view that a player has finished the assembling phase and is correctly positioned on the flight board.
     * @param nickname the nickname of the player
     * @param position the position on the flight board
     * @throws Exception if an error occurs during the update
     */
    void updateFinishAssembling(String nickname, int position) throws Exception;

    //invoked when the game switches to the ship placement phase, which means that the players can only
    //place their ship on the flight board
    /**
     * Notifies the view that the game has switched to the ship placement phase.
     * @throws Exception if an error occurs during the update
     */
    void updateShipPlacement() throws Exception;

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase
    /**
     * Notifies the view that all the players have concluded the assembling phase and are now in the ship control phase.
     * @throws Exception if an error occurs during the update
     */
    void updateShipControl() throws Exception;

}
