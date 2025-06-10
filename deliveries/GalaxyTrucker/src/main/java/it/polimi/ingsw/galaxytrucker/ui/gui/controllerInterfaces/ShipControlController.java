package it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces;

public interface ShipControlController extends GuiController{
    //notifies the view that a component of a player's ship board has been destroyed
    void updateDestroyedComponent(String nickname, int x, int y) throws Exception;

    //notifies the view about a change in the number of crew of a cabin
    void updateCrewChange(String nickname, int x, int y, int change) throws Exception;

    //notifies the view that a player has initialized a battery container with batteries
    void updateBatteries(String nickname, int x, int y, int change) throws Exception;

    //notifies the view about a change in the number of aliens of a cabin
    void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) throws Exception;

    //notifies the view about the fact that a player has to pick a card in order to continue the game
    void updateCardPicking() throws Exception;
}
