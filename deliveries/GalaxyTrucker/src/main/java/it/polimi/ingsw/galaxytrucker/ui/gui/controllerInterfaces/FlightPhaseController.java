package it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;

public interface FlightPhaseController extends GuiController{
    //notifies the view that a player has to repair its ship board before the player in turn can pick a new card
    void updateShipRepair(String nickname) throws Exception;

    //notifies the view that a component of a player's ship board has been destroyed
    void updateDestroyedComponent(String nickname, int x, int y) throws Exception;

    //notifies the view about a change in the number of crew of a cabin
    void updateCrewChange(String nickname, int x, int y, int change) throws Exception;

    //notifies the view that a player has initialized a battery container with batteries
    void updateBatteries(String nickname, int x, int y, int change) throws Exception;

    //notifies the view about a change in the number of aliens of a cabin
    void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) throws Exception;

    //notifies the view that a good has been loaded in a cargo hold
    void updateLoadedGood(String nickname, int x, int y, Color good) throws Exception;

    //notifies the view that some goods have been removed form a cargo hold
    void updateRemovedGoods(String nickname, int x, int y, Color good, int numberGoods) throws Exception;

    //notifies the view about the fact that a player has to pick a card in order to continue the game
    void updateCardPicking() throws Exception;

    //notifies the view about the next player whose turn it is to perform an action
    void updateNextTurn(String nickname) throws Exception;

    //notifies the view that a new card has been picked and must be solved
    void updateCardSolving(int imageID) throws Exception;

    //notifies the view that a player has quit the game
    void updatePlayerQuit(String nickname) throws Exception;

    //notifies the view that a player has gained/lost credits
    void updatePlayerCredits(String nickname, int change) throws Exception;

    //notifies the view about the fact that the game is finished
    void updateEndGame() throws Exception;
}
