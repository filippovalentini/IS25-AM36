package it.polimi.ingsw.galaxytrucker.ui.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.ui.UserInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.FlightBoardController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBoardController;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.ShipBuildingController;
import it.polimi.ingsw.galaxytrucker.ui.view.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiInterface implements UserInterface {
    private static GuiInterface instance;
    private GameSetupController setupController;
    private LobbyController lobbyController;
    private ShipBuildingController shipBuildingController;
    private FlightBoardController flightBoardController;
    private ShownComponentsController shownComponentsController;
    private ShipBoardController shipBoardController;
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

    public void setShipBuildingController(ShipBuildingController shipBuildingController) {
        this.shipBuildingController = shipBuildingController;
    }

    public void setFlightBoardController(FlightBoardController flightBoardController) {
        this.flightBoardController = flightBoardController;
    }

    public void setShownComponentsController(ShownComponentsController shownComponentsController) {
        this.shownComponentsController = shownComponentsController;
    }

    public void setShipBoardController(ShipBoardController shipBoardController) {
        this.shipBoardController = shipBoardController;
    }

    public void launch() {
        Application.launch(JavaFxLauncher.class);
    }

    public Map<String, Image> loadImageMap(String imageType) {
        Map<String, Image> result = new HashMap<>();

        try (InputStream jsonStream = getClass().getResourceAsStream(
                "/it/polimi/ingsw/galaxytrucker/jsonImageMappings/" + imageType + ".json")) {

            if (jsonStream == null) {
                System.err.println(imageType + ".json non trovato!");
                return result;
            }

            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> idToPath = mapper.readValue(jsonStream, Map.class);

            for (Map.Entry<String, String> entry : idToPath.entrySet()) {
                String id = entry.getKey();
                String fullPath = "/it/polimi/ingsw/galaxytrucker/images/" + imageType + "/" + entry.getValue();

                try (InputStream imageStream = getClass().getResourceAsStream(fullPath)) {
                    if (imageStream == null) {
                        System.err.println("Immagine mancante: " + fullPath);
                        continue;
                    }
                    result.put(id, new Image(imageStream));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    //notifies the view about a change in the game phase
    public void notifyGamePhase(String gamePhase) throws Exception{
        if(setupController!=null){
            setupController.notifyGamePhase(gamePhase);
        }
        if(shipBuildingController != null){
            shipBuildingController.notifyGamePhase(gamePhase);
        }
        if(flightBoardController != null){
            flightBoardController.notifyGamePhase(gamePhase);
        }
        if(shownComponentsController != null){
            shownComponentsController.notifyGamePhase(gamePhase);
        }
        if(shipBoardController != null){
            shipBoardController.notifyGamePhase(gamePhase);
        }
    }

    @Override
    public void notifyError(String errorMessage) throws Exception {
        if(setupController!=null){
            setupController.notifyError(errorMessage);
        }
        if(shipBuildingController != null){
            shipBuildingController.notifyError(errorMessage);
        }
        if(flightBoardController != null){
            flightBoardController.notifyError(errorMessage);
        }
        if(shownComponentsController != null){
            shownComponentsController.notifyError(errorMessage);
        }
        if(shipBoardController != null){
            shipBoardController.notifyError(errorMessage);
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
            try{Thread.sleep(2000);}catch(Exception e){}
            lobbyController.startTimer(view.isFirstFlight());
        }).start();
    }

    @Override
    public void updatePickedComponent(int imageID, boolean released) throws Exception {
        this.view.updatePickedComponent(imageID, released);
        if(shipBuildingController != null){
            shipBuildingController.updatePickedComponent(imageID, released);
        }
    }

    @Override
    public void updateShownComponent(int imageID, boolean released) throws Exception {
        this.view.updateShownComponent(imageID, released);
        if(shownComponentsController != null){
            shownComponentsController.updateShownComponents(imageID, released);
        }
    }

    @Override
    public void updateReservedComponent(String nickname, int imageID, boolean released) throws Exception {
        this.view.updateReservedComponent(nickname, imageID, released);
        if(shipBuildingController != null && nickname.equals(this.nickname)){
            shipBuildingController.updateReservedComponent(nickname, imageID, released);
        }
        if(shipBoardController != null){
            shipBoardController.updateReservedComponent(nickname, imageID, released);
        }
    }

    @Override
    public void updateRotatePickedComponent() throws Exception {
        this.view.updateRotatePickedComponent();
        if(shipBuildingController != null){
            shipBuildingController.updateRotatePickedComponent();
        }
    }

    @Override
    public void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) throws Exception {
        this.view.updateAssembledComponent(nickname, imageID, orientation, x, y);
        if(shipBuildingController != null && nickname.equals(this.nickname)){
            shipBuildingController.updateAssembledComponent(nickname, imageID, orientation, x, y);
        }
        if(shipBoardController != null){
            shipBoardController.updateAssembledComponent(nickname, imageID, orientation, x, y);
        }
    }

    @Override
    public void updatePickedDeck(List<Integer> deckIDs) throws Exception {
        this.view.updatePickedDeck(deckIDs);
        if(flightBoardController != null){
            flightBoardController.updatePickedDeck(deckIDs);
        }
    }

    @Override
    public void updateReleasedDeck() throws Exception {
        this.view.updateReleasedDeck();
        if(flightBoardController != null){
            flightBoardController.updateReleasedDeck();
        }
    }

    @Override
    public void updateFinishAssembling(String nickname, int position) throws Exception {
        this.view.updateFinishAssembling(nickname, position);
        if(shipBuildingController != null){
            shipBuildingController.updateFinishAssembling(nickname, position);
        }
        if(flightBoardController != null){
            flightBoardController.updateFinishAssembling(nickname, position);
        }
        if(nickname.equals(this.nickname)){
            notifyGamePhase(this.view.getGameState());
        }
    }

    @Override
    public void updateStartNewCycle() throws Exception {
        this.view.updateStartNewCycle();
    }

    @Override
    public void updateFinishedCycle() throws Exception {
        this.view.updateFinishedCycle();
    }

    @Override
    public void updateShipPlacement() throws Exception {
        this.view.updateShipPlacement();
        if(shipBuildingController != null){
            shipBuildingController.updateShipPlacement();
        }
    }

    @Override
    public void updateShipControl() throws Exception {
        this.view.updateShipControl();
        if(shipBuildingController != null){
            shipBuildingController.updateShipControl();
        }
        notifyGamePhase(this.view.getGameState());
    }

    @Override
    public void updateDestroyedComponent(String nickname, int x, int y) throws Exception {
        this.view.updateDestroyedComponent(nickname, x, y);
    }

    @Override
    public void updateCrewChange(String nickname, int x, int y, int change) throws Exception {
        this.view.updateCrewChange(nickname, x, y, change);
    }

    @Override
    public void updateBatteries(String nickname, int x, int y, int change) throws Exception {
        this.view.updateBatteries(nickname, x, y, change);
    }

    @Override
    public void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) throws Exception {
        this.view.updateAlienChange(nickname, x, y, isPurple, added);
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
        this.view.updateEndGame();
    }
}
