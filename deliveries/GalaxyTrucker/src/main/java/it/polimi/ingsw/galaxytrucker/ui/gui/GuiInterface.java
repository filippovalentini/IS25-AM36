package it.polimi.ingsw.galaxytrucker.ui.gui;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.ui.UserInterface;
import it.polimi.ingsw.galaxytrucker.ui.view.View;
import javafx.application.Application;

import java.util.List;

public class GuiInterface implements UserInterface {
    private static GuiInterface instance;
    private GameSetupController setupController;
    private LobbyController lobbyController;
    private ShipBuildingControllerL1 shipBuildingController;
    private View view;
    private String nickname;
    private Color color;


    public GuiInterface() {
        instance = this;
    }

    public static GuiInterface getInstance() {
        return instance;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public View getView() {
        return view;
    }

    public void setSetupController(GameSetupController controller) {
        this.setupController = controller;
    }

    public void setLobbyController(LobbyController lobbyController) {
        this.lobbyController = lobbyController;
    }

    public void setShipBuildingController(ShipBuildingControllerL1 shipBuildingController) {
        this.shipBuildingController = shipBuildingController;
    }

    public void launch() {
        Application.launch(JavaFxLauncher.class);
    }



    @Override
    public void notifyError(String errorMessage) throws Exception {
        if(setupController != null) {
            setupController.notifyError(errorMessage);
        }
        if(lobbyController != null) {
            lobbyController.notifyError(errorMessage);
        }
    }

    @Override
    public void updateWaitingForPlayers(boolean firstFlight) throws Exception {
        this.view = new View(nickname, color, firstFlight);
    }

    @Override
    public void updateNewPlayer(String nickname, Color color) throws Exception {
        this.view.updateNewPlayer(nickname, color);
        if(lobbyController != null){
            lobbyController.addPlayer(nickname, color);
        }
    }

    @Override
    public void updateStartAssembling() throws Exception {
        this.view.updateStartAssembling();
        new Thread(() -> {
            try{Thread.sleep(1000);}catch(Exception e){}
            lobbyController.startTimer();
        }).start();
    }

    @Override
    public void updatePickedComponent(int imageID, boolean released) throws Exception {
        //shipBuildingController.updatePickedComponentGUI(imageID, released);

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
        //shipBuildingController.updateAssembledComponentGUI( nickname,  imageID,  orientation,  x,  y);

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
