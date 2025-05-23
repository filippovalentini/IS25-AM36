package it.polimi.ingsw.galaxytrucker.ui.gui;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.ui.UserInterface;

import java.util.List;

public class GuiInterface implements UserInterface {
    private static GuiInterface instance;
    private GuiController controller;

    public GuiInterface() {
        instance = this;
    }

    public static GuiInterface getInstance() {
        return instance;
    }

    public void setController(GuiController controller) {
        this.controller = controller;
    }



    @Override
    public void notifyError(String errorMessage) throws Exception {

    }

    @Override
    public void updateWaitingForPlayers(boolean firstFlight) throws Exception {

    }

    @Override
    public void updateNewPlayer(String nickname, Color color) throws Exception {

    }

    @Override
    public void updateStartAssembling() throws Exception {

    }

    @Override
    public void updatePickedComponent(int imageID, boolean released) throws Exception {

    }

    @Override
    public void updateShownComponent(int imageID, boolean released) throws Exception {

    }

    @Override
    public void updateReservedComponent(String nickname, int imageID, boolean released) throws Exception {

    }

    @Override
    public void updateRotatePickedComponent() throws Exception {

    }

    @Override
    public void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws Exception {

    }

    @Override
    public void updatePickedDeck(List<Integer> deckIDs) throws Exception {

    }

    @Override
    public void updateReleasedDeck() throws Exception {

    }

    @Override
    public void updateFinishAssembling(String nickname, int position) throws Exception {

    }

    @Override
    public void updateStartNewCycle() throws Exception {

    }

    @Override
    public void updateFinishedCycle() throws Exception {

    }

    @Override
    public void updateShipPlacement() throws Exception {

    }

    @Override
    public void updateShipControl() throws Exception {

    }

    @Override
    public void updateDestroyedComponent(String nickname, int x, int y) throws Exception {

    }

    @Override
    public void updateCrewChange(String nickname, int x, int y, int change) throws Exception {

    }

    @Override
    public void updateBatteries(String nickname, int x, int y, int change) throws Exception {

    }

    @Override
    public void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) throws Exception {

    }

    @Override
    public void updateLoadedGood(String nickname, int x, int y, Color good) throws Exception {

    }

    @Override
    public void updateRemovedGoods(String nickname, int x, int y, Color good, int numberGoods) throws Exception {

    }

    @Override
    public void updateCardPicking() throws Exception {

    }

    @Override
    public void updateNextTurn(String nickname) throws Exception {

    }

    @Override
    public void updateCardSolving(int imageID) throws Exception {

    }

    @Override
    public void updatePlayerQuit(String nickname) throws Exception {

    }

    @Override
    public void updatePlayerCredits(String nickname, int change) throws Exception {

    }

    @Override
    public void updatePlayerPosition(String nickname, int lap, int cell) throws Exception {

    }

    @Override
    public void updateEndGame() throws Exception {

    }
}
