package it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.network.VirtualServer;

public interface ShipBuildingController extends GuiController {
    @Override
    void setServer(VirtualServer server);

    //notifies the view about the fact that a component has been successfully picked/released (depending on
    //the value of the boolean parameter) by the corresponding player; the parameter imageID is needed for the
    //view in order to show the right component to the user
    void updatePickedComponent(int imageID, boolean released) throws Exception;

    //notifies the view about the fact that a player (identified by the nickname parameter) has picked a reserved
    //component/ reserved a component (depending on the value of the boolean parameter); the parameter imageID
    //is needed for the view in order to show the right component to the user
    void updateReservedComponent(String nickname, int imageID, boolean released) throws Exception;

    //notifies the view about the fact that the picked component of the corresponding player has been rotated
    void updateRotatePickedComponent() throws Exception;

    //notifies the view about the fact that a player (identified by the nickname parameter) has assembled a
    //component in position (x,y) of its ship board; the parameter imageID is needed for the view in order
    //to show the right component to the user
    void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws Exception;

    //notifies the view about the fact that a player has finished the assembling phase and is
    //correctly positioned on the flight board; still, other players have to finish building their ships
    void updateFinishAssembling(String nickname, int position) throws Exception;

    //invoked when the game switches to the ship placement phase, which means that the players can only
    //place their ship on the flight board
    void updateShipPlacement() throws Exception;

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase
    void updateShipControl() throws Exception;

}
