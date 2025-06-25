package it.polimi.ingsw.galaxytrucker.ui.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.ui.UserInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.controllerInterfaces.*;
import it.polimi.ingsw.galaxytrucker.ui.gui.otherControllers.GameSetupController;
import it.polimi.ingsw.galaxytrucker.ui.gui.otherControllers.LobbyController;
import it.polimi.ingsw.galaxytrucker.ui.gui.otherControllers.ShownComponentsController;
import it.polimi.ingsw.galaxytrucker.ui.view.View;
import javafx.application.Application;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/** * This class implements the UserInterface for the GUI version.
 */
public class GuiInterface implements UserInterface {
    private static GuiInterface instance;
    private GameSetupController setupController;
    private LobbyController lobbyController;
    private ShipBuildingController shipBuildingController;
    private FlightBoardController flightBoardController;
    private ShownComponentsController shownComponentsController;
    private ShipBoardController shipBoardController;
    private ShipControlController shipControlController;
    private FlightPhaseController flightPhaseController;
    private View view;
    private String nickname;
    private Color color;

    /**
     * Constructor for GuiInterface.
     * Initializes the instance and sets up the initial state.
     */
    public GuiInterface() {
        instance = this;
    }
    /**
     * Returns the singleton instance of GuiInterface.
     *
     * @return the instance of GuiInterface
     */
    public static GuiInterface getInstance() {
        return instance;
    }

    /**
     * Gets the nickname of the player.
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }
    /**
     * Sets the nickname for the player.
     *
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets the color associated with the player.
     * @return the color of the player
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color for the player.
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns the view associated with this interface.
     * @return the View object representing the GUI
     */
    public View getView() {
        return view;
    }

    /**
     * Sets the GameSetupController for this interface.
     * @param controller
     */
    public void setSetupController(GameSetupController controller) {
        this.setupController = controller;
    }

    /**
     * Sets the LobbyController for this interface.
     * @param lobbyController
     */
    public void setLobbyController(LobbyController lobbyController) {
        this.lobbyController = lobbyController;
    }

    /**
     * Sets the ShipBuildingController for this interface.
     * @param shipBuildingController
     */
    public void setShipBuildingController(ShipBuildingController shipBuildingController) {
        this.shipBuildingController = shipBuildingController;
    }

    /**
     * Sets the FlightBoardController for this interface.
     * @param flightBoardController
     */
    public void setFlightBoardController(FlightBoardController flightBoardController) {
        this.flightBoardController = flightBoardController;
    }

    /**
     * Sets the ShownComponentsController for this interface.
     * @param shownComponentsController
     */
    public void setShownComponentsController(ShownComponentsController shownComponentsController) {
        this.shownComponentsController = shownComponentsController;
    }

    /**
     * Sets the ShipBoardController for this interface.
     * @param shipBoardController
     */
    public void setShipBoardController(ShipBoardController shipBoardController) {
        this.shipBoardController = shipBoardController;
    }

    /**
     * Sets the ShipControlController for this interface.
     * @param shipControlController
     */
    public void setShipControlController(ShipControlController shipControlController) {
        this.shipControlController = shipControlController;
    }

    /**
     * Sets the FlightPhaseController for this interface.
     * @param flightPhaseController
     */
    public void setFlightPhaseController(FlightPhaseController flightPhaseController) {
        this.flightPhaseController = flightPhaseController;
    }

    /**
     * Launches the JavaFX application.
     */
    public void launch() {
        Application.launch(JavaFxLauncher.class);
    }

    /**
     * Loads a map of images based on the specified image type.
     * @param imageType
     * @return a map where keys are image IDs and values are Image objects
     */
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
    /**
     * Notifies the view about a change in the game phase.
     * @param gamePhase the current game phase
     * @throws Exception if an error occurs during notification
     */
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
    /**
     * Notifies the view about an error message.
     * @param errorMessage the error message to display
     * @throws Exception if an error occurs during notification
     */
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
        if(shipControlController != null){
            shipControlController.notifyError(errorMessage);
        }
        if(flightPhaseController != null){
            flightPhaseController.notifyError(errorMessage);
        }
    }

    /**
     * Updates the view to indicate that players are waiting for others to join.
     * @param firstFlight
     * @throws Exception
     */
    @Override
    public void updateWaitingForPlayers(boolean firstFlight) throws Exception {
        this.view = new View(nickname, color, firstFlight);
    }

    /**
     * Updates the view with a new player.
     * @param nickname
     * @param color
     * @throws Exception
     */
    @Override
    public void updateNewPlayer(String nickname, Color color) throws Exception {
        this.view.updateNewPlayer(nickname, color);
        if(lobbyController != null){
            lobbyController.addPlayer(nickname, color);
        }
    }

    /**
     * Updates the view to indicate that the game is starting.
     * @throws Exception
     */
    @Override
    public void updateStartAssembling() throws Exception {
        this.view.updateStartAssembling();
        new Thread(() -> {
            try{Thread.sleep(2000);}catch(Exception e){}
            lobbyController.startTimer(view.isFirstFlight());
        }).start();
    }

    /**
     * Updates the view to indicate that a component has been picked.
     * @param imageID
     * @param released
     * @throws Exception
     */
    @Override
    public void updatePickedComponent(int imageID, boolean released) throws Exception {
        this.view.updatePickedComponent(imageID, released);
        if(shipBuildingController != null){
            shipBuildingController.updatePickedComponent(imageID, released);
        }
    }

    /**
     * Updates the view to indicate that a component has been shown.
     * @param imageID
     * @param released
     * @throws Exception
     */
    @Override
    public void updateShownComponent(int imageID, boolean released) throws Exception {
        this.view.updateShownComponent(imageID, released);
        if(shownComponentsController != null){
            shownComponentsController.updateShownComponents(imageID, released);
        }
    }

    /**
     * Updates the view to indicate that a reserved component has been updated.
     * @param nickname
     * @param imageID
     * @param released
     * @throws Exception
     */
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

    /**
     * Updates the view to indicate that a component has been rotated.
     * @throws Exception
     */
    @Override
    public void updateRotatePickedComponent() throws Exception {
        this.view.updateRotatePickedComponent();
        if(shipBuildingController != null){
            shipBuildingController.updateRotatePickedComponent();
        }
    }

    /**
     * Updates the view to indicate that an assembled component has been updated.
     * @param nickname
     * @param imageID
     * @param orientation
     * @param x
     * @param y
     * @throws Exception
     */
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

    /**
     * Updates the view to indicate that a deck has been picked.
     * @param deckIDs
     * @throws Exception
     */
    @Override
    public void updatePickedDeck(List<Integer> deckIDs) throws Exception {
        this.view.updatePickedDeck(deckIDs);
        if(flightBoardController != null){
            flightBoardController.updatePickedDeck(deckIDs);
        }
    }

    /**
     * Updates the view to indicate that a deck has been released.
     * @throws Exception
     */
    @Override
    public void updateReleasedDeck() throws Exception {
        this.view.updateReleasedDeck();
        if(flightBoardController != null){
            flightBoardController.updateReleasedDeck();
        }
    }

    /**
     * Updates the view to indicate that a player has finished assembling.
     * @param nickname
     * @param position
     * @throws Exception
     */
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

    /**
     * Updates the view to indicate that a new cycle is starting.
     * @throws Exception
     */
    @Override
    public void updateStartNewCycle() throws Exception {
        this.view.updateStartNewCycle();
        if(flightBoardController != null){
            flightBoardController.updateStartNewCycle();
        }
    }

    /**
     * Updates the view to indicate that a cycle has finished.
     * @throws Exception
     */
    @Override
    public void updateFinishedCycle() throws Exception {
        this.view.updateFinishedCycle();
        if(flightBoardController != null){
            flightBoardController.updateFinishedCycle();
        }
    }

    /**
     * Updates the view to indicate that ship placement is being updated.
     * @throws Exception
     */
    @Override
    public void updateShipPlacement() throws Exception {
        this.view.updateShipPlacement();
        if(shipBuildingController != null){
            shipBuildingController.updateShipPlacement();
        }
        notifyGamePhase(this.view.getGameState());
    }

    /**
     * Updates the view to indicate that ship control is being updated.
     * @throws Exception
     */
    @Override
    public void updateShipControl() throws Exception {
        this.view.updateShipControl();
        if(shipBuildingController != null){
            shipBuildingController.updateShipControl();
        }
        if(shipBoardController != null){
            shipBoardController.updateShipControl();
        }
        if(flightBoardController != null){
            flightBoardController.updateShipControl();
        }
    }

    /**
     * Updates the view to indicate that ship repair is being performed.
     * @param nickname
     * @throws Exception
     */
    @Override
    public void updateShipRepair(String nickname) throws Exception {
        this.view.updateShipRepair(nickname);
        if(flightPhaseController != null){
            flightPhaseController.updateShipRepair(nickname);
        }
        if(shipBoardController != null){
            shipBoardController.updateShipRepair(nickname);
        }
        if(flightBoardController != null){
            flightBoardController.updateShipRepair(nickname);
        }
    }

    /**
     * Updates the view to indicate that a component has been destroyed.
     * @param nickname
     * @param x
     * @param y
     * @throws Exception
     */
    @Override
    public void updateDestroyedComponent(String nickname, int x, int y) throws Exception {
        this.view.updateDestroyedComponent(nickname, x, y);
        if(shipControlController != null){
            shipControlController.updateDestroyedComponent(nickname, x, y);
        }
        if(flightPhaseController != null){
            flightPhaseController.updateDestroyedComponent(nickname, x, y);
        }
        if(shipBoardController != null){
            shipBoardController.updateDestroyedComponent(nickname, x, y);
        }
    }

    /**
     * Updates the view to indicate a change in crew members.
     * @param nickname
     * @param x
     * @param y
     * @param change
     * @throws Exception
     */
    @Override
    public void updateCrewChange(String nickname, int x, int y, int change) throws Exception {
        this.view.updateCrewChange(nickname, x, y, change);
        if(shipControlController != null){
            shipControlController.updateCrewChange(nickname, x, y, change);
        }
        if(flightPhaseController != null){
            flightPhaseController.updateComponentChange(nickname, x, y);
        }
        if(shipBoardController != null){
            shipBoardController.updateComponentChange(nickname, x, y);
        }
    }

    /**
     * Updates the view to indicate a change in batteries.
     * @param nickname
     * @param x
     * @param y
     * @param change
     * @throws Exception
     */
    @Override
    public void updateBatteries(String nickname, int x, int y, int change) throws Exception {
        this.view.updateBatteries(nickname, x, y, change);
        if(shipControlController != null){
            shipControlController.updateBatteries(nickname, x, y, change);
        }
        if(flightPhaseController != null){
            flightPhaseController.updateComponentChange(nickname, x, y);
        }
        if(shipBoardController != null){
            shipBoardController.updateComponentChange(nickname, x, y);
        }
    }

    /**
     * Updates the view to indicate a change in aliens.
     * @param nickname
     * @param x
     * @param y
     * @param isPurple
     * @param added
     * @throws Exception
     */
    @Override
    public void updateAlienChange(String nickname, int x, int y, boolean isPurple, boolean added) throws Exception {
        this.view.updateAlienChange(nickname, x, y, isPurple, added);
        if(shipControlController != null){
            shipControlController.updateAlienChange(nickname, x, y, isPurple, added);
        }
        if(flightPhaseController != null){
            flightPhaseController.updateComponentChange(nickname, x, y);
        }
        if(shipBoardController != null){
            shipBoardController.updateComponentChange(nickname, x, y);
        }
    }

    /**
     * Updates the view to indicate that a good has been loaded.
     * @param nickname
     * @param x
     * @param y
     * @param good
     * @throws Exception
     */
    @Override
    public void updateLoadedGood(String nickname, int x, int y, Color good) throws Exception {
        this.view.updateLoadedGood(nickname, x, y, good);
        if(flightPhaseController != null){
            flightPhaseController.updateComponentChange(nickname, x, y);
        }
        if(shipBoardController != null){
            shipBoardController.updateComponentChange(nickname, x, y);
        }
    }

    /**
     * Updates the view to indicate that goods have been removed.
     * @param nickname
     * @param x
     * @param y
     * @param good
     * @param numberGoods
     * @throws Exception
     */
    @Override
    public void updateRemovedGoods(String nickname, int x, int y, Color good, int numberGoods) throws Exception {
        this.view.updateRemovedGoods(nickname, x, y, good, numberGoods);
        if(flightPhaseController != null){
            flightPhaseController.updateComponentChange(nickname, x, y);
        }
        if(shipBoardController != null){
            shipBoardController.updateComponentChange(nickname, x, y);
        }
    }

    /**
     * Updates the view to indicate that card picking is being performed.
     * @throws Exception
     */
    @Override
    public void updateCardPicking() throws Exception {
        this.view.updateCardPicking();
        if(shipControlController != null){
            shipControlController.updateCardPicking();
        }
        if(flightPhaseController != null){
            flightPhaseController.updateCardPicking();
        }
        if(shipBoardController != null){
            shipBoardController.updateCardPicking();
        }
        if(flightBoardController != null){
            flightBoardController.updateCardPicking();
        }
    }

    /**
     * Updates the view to indicate that the next turn is starting.
     * @param nickname
     * @throws Exception
     */
    @Override
    public void updateNextTurn(String nickname) throws Exception {
        this.view.updateNextTurn(nickname);
        this.view.updateThrowableDice();
        if(flightPhaseController != null){
            flightPhaseController.updateNextTurn(nickname);
        }
    }

    /**
     * Updates the view to indicate that card solving is being performed.
     * @param imageID
     * @throws Exception
     */
    @Override
    public void updateCardSolving(int imageID) throws Exception {
        this.view.updateCardSolving(imageID);
        if(flightPhaseController != null){
            flightPhaseController.updateCardSolving(imageID);
        }
        if(shipBoardController != null){
            shipBoardController.updateCardSolving(imageID);
        }
        if(flightBoardController != null){
            flightBoardController.updateCardSolving(imageID);
        }
    }

    /**
     * Updates the view to indicate that a player has quit the game.
     * @param nickname
     * @throws Exception
     */
    @Override
    public void updatePlayerQuit(String nickname) throws Exception {
        this.view.updatePlayerQuit(nickname);
        if(flightPhaseController != null){
            flightPhaseController.updatePlayerQuit(nickname);
        }
        if(shipBoardController != null){
            shipBoardController.updatePlayerQuit(nickname);
        }
    }

    /**
     * Updates the view to indicate a change in player credits.
     * @param nickname
     * @param change
     * @throws Exception
     */
    @Override
    public void updatePlayerCredits(String nickname, int change) throws Exception {
        this.view.updatePlayerCredits(nickname, change);
        if(flightPhaseController != null){
            flightPhaseController.updatePlayerCredits(nickname, change);
        }
        if(shipBoardController != null){
            shipBoardController.updatePlayerCredits(nickname, change);
        }
    }

    /**
     * Updates the view to indicate a change in player position.
     * @param nickname
     * @param lap
     * @param cell
     * @throws Exception
     */
    @Override
    public void updatePlayerPosition(String nickname, int lap, int cell) throws Exception {
        this.view.updatePlayerPosition(nickname, lap, cell);
        if(flightBoardController != null){
            flightBoardController.updatePlayerPosition(nickname, cell);
        }
    }

    /**
     * Updates the view to indicate that the end of the game has been reached.
     * @throws Exception
     */
    @Override
    public void updateEndGame() throws Exception {
        this.view.updateEndGame();
        if(flightPhaseController != null){
            flightPhaseController.updateEndGame();
        }
        if(shipBoardController != null){
            shipBoardController.updateEndGame();
        }
        if(flightBoardController != null){
            flightBoardController.updateEndGame();
        }
    }
}
