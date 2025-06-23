package it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;

public interface ShipBoardController extends GuiController {
    //notifies the view about the fact that a player (identified by the nickname parameter) has picked a reserved
    //component/ reserved a component (depending on the value of the boolean parameter); the parameter imageID
    //is needed for the view in order to show the right component to the user
    void updateReservedComponent(String nickname, int imageID, boolean released) throws Exception;

    //notifies the view about the fact that a player (identified by the nickname parameter) has assembled a
    //component in position (x,y) of its ship board; the parameter imageID is needed for the view in order
    //to show the right component to the user
    void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws Exception;

    //notifies the view that all the players have concluded the assembling phase, which means that the players
    //enter the ship control phase
    void updateShipControl() throws Exception;

    //notifies the view that a player has to repair its ship board before the player in turn can pick a new card
    void updateShipRepair(String nickname) throws Exception;

    //notifies the view that a component of a player's ship board has been destroyed
    void updateDestroyedComponent(String nickname, int x, int y) throws Exception;

    //notifies the view that a component of the ship board has changed and must be reloaded
    void updateComponentChange(String nickname, int x, int y) throws Exception;

    //notifies the view about the fact that a player has to pick a card in order to continue the game
    void updateCardPicking() throws Exception;

    //notifies the view that a new card has been picked and must be solved
    void updateCardSolving(int imageID) throws Exception;

    //notifies the view that a player has quit the game
    void updatePlayerQuit(String nickname) throws Exception;

    //notifies the view that a player has gained/lost credits
    void updatePlayerCredits(String nickname, int change) throws Exception;

    //notifies the view about the fact that the game is finished
    void updateEndGame() throws Exception;
}
