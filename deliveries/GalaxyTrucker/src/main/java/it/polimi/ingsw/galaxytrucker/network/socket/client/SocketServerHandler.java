package it.polimi.ingsw.galaxytrucker.network.socket.client;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualView;
import it.polimi.ingsw.galaxytrucker.network.socket.message.PlayerActionMessage;
import it.polimi.ingsw.galaxytrucker.network.socket.message.PlayerActionType;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//this class contains the methods to create and send serialized messages to the server starting from given
//parameters
public class SocketServerHandler implements VirtualServerSocket {
    final ObjectOutputStream out;

    public SocketServerHandler(ObjectOutputStream out) {
        this.out = out;
    }

    //closes the client's output stream
    public void close() throws IOException {
        this.out.close();
    }

    //converts a list of integers in a string
    public static String serializeList(List<Integer> list) {
        return list.toString()
                .replaceAll("[\\[\\]\\s]", "");
    }

    @Override
    public boolean startedGame(int gameID) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(String.valueOf(gameID)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.ASK_STARTED_GAME, params);
        out.writeObject(message);
        return true;
    }

    //invoked when the first player decides to start the game
    @Override
    public void startNewGame(VirtualView client, int gameID, boolean firstFlight, int numberPlayers) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(String.valueOf(gameID), String.valueOf(firstFlight), String.valueOf(numberPlayers)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.START_GAME, params);
        out.writeObject(message);
    }

    //invoked when one of the players decides enter the game; the remote client view is added to the list
    //of connected clients
    @Override
    public boolean addPlayer(VirtualView client, int gameID, String nickname, Color color) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(String.valueOf(gameID), nickname, color.toString()));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.ADD_PLAYER, params);
        out.writeObject(message);
        return true;
    }

    //invoked when a player wants to pick a component among the one placed face down (assembling phase)
    @Override
    public void pickHidden(int gameID, String nickname) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.PICK_HIDDEN, params);
        out.writeObject(message);
    }

    //invoked when a player wants to pick a specific component among the one placed face up (assembling phase)
    @Override
    public void pickShown(int gameID, String nickname, int index) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(index)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.PICK_SHOWN, params);
        out.writeObject(message);
    }

    //invoked when a player wants to release (therefore, place face up) the component that it has picked
    @Override
    public void putShown(int gameID, String nickname) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.RELEASE, params);
        out.writeObject(message);
    }

    //invoked when a player wants to reserve the component that it has picked for its ship board
    @Override
    public void reserveComponent(int gameID, String nickname) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.RESERVE, params);
        out.writeObject(message);
    }

    //invoked when a player wants to pick one of the components that it has reserved for its ship board
    @Override
    public void pickReservedComponent(int gameID, String nickname, int position) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(position)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.PICK_RESERVED, params);
        out.writeObject(message);
    }

    //invoked when a player wants to change the orientation of the component that it has picked
    @Override
    public void rotatePickedComponent(int gameID, String nickname) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.ROTATE, params);
        out.writeObject(message);
    }

    //invoked when a player wants to assemble on the ship board the component that it has picked
    @Override
    public void assembledComponent(int gameID, String nickname, int x, int y) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(x), String.valueOf(y)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.ASSEMBLE, params);
        out.writeObject(message);
    }

    //invoked when a player wants to pick a deck during the assembling phase to see its content
    @Override
    public void pickDeck(int gameID, String nickname, int deckNumber) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(deckNumber)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.PICK_DECK, params);
        out.writeObject(message);
    }

    //invoked when a player wants to release the deck it has picked, during the assembling phase
    @Override
    public void releaseDeck(int gameID, String nickname) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.RELEASE_DECK, params);
        out.writeObject(message);
    }

    //invoked when a player has finished the assembling phase and has to pick a free position on the flight board
    @Override
    public void setPosition(int gameID, String nickname, int initCell) throws IOException {
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(initCell)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.SET_POSITION, params);
        out.writeObject(message);
    }

    //invoked when a player wants to turn around the hourglass
    @Override
    public void startNewCycle(int gameID, String nickname) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.HOURGLASS, params);
        out.writeObject(message);
    }

    //invoked when a player wants to destroy a component in order to validate its ship board or when a
    //component is destroyed due to a cannon shot/meteor attack
    @Override
    public void destroyComponent(int gameID, String nickname, int x, int y) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(x), String.valueOf(y)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.DESTROY, params);
        out.writeObject(message);
    }

    //invoked when a player wants to initialize a cabin of its shipboard with 2 human crew members
    @Override
    public void addCrew(int gameID, String nickname, int x, int y) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(x), String.valueOf(y)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.ADD_CREW, params);
        out.writeObject(message);
    }

    //invoked when a player wants to initialize a battery container of its shipboard with batteries
    @Override
    public void addBatteries(int gameID, String nickname, int x, int y) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(x), String.valueOf(y)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.ADD_BATTERIES, params);
        out.writeObject(message);
    }

    //invoked when a player wants to initialize a cabin of its shipboard with an alien
    @Override
    public void addAlien(int gameID, String nickname, boolean isPurple, int x, int y) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(isPurple), String.valueOf(x), String.valueOf(y)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.ADD_ALIEN, params);
        out.writeObject(message);
    }

    //invoked when a player wants to pick a new card from the game deck
    @Override
    public void pickNextCard(int gameID, String nickname) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.PICK_CARD, params);
        out.writeObject(message);
    }

    //invoked when a player wants to leave the game
    @Override
    public void quitGame(int gameID, String nickname) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.QUIT, params);
        out.writeObject(message);
    }

    //invoked when a player wants to skip an action during the flight phase
    @Override
    public void skip(int gameID, String nickname) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.SKIP, params);
        out.writeObject(message);
    }

    //invoked when a player wants to land on an abandoned ship or station
    @Override
    public void landing(int gameID, String nickname, List<Integer> x, List<Integer> y, List<Integer> z) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, serializeList(x), serializeList(y), serializeList(z)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.LANDING, params);
        out.writeObject(message);
    }

    //invoked when th ship board of a player must be hit by meteor/cannon shot
    @Override
    public void hitShip(int gameID, String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(diceResult), String.valueOf(activateShield), String.valueOf(activateCannon)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.HIT_SHIP, params);
        out.writeObject(message);
    }

    //invoked when a player wants to fly across the flight board exploiting its engine strength
    @Override
    public void fly(int gameID, String nickname, int usedBatteries) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(usedBatteries)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.FLY, params);
        out.writeObject(message);
    }

    //invoked when a player wants to defeat an enemy; the player can decide whether to lose flight days
    //to gain credits/goods or not
    @Override
    public void defeat(int gameID, String nickname, int usedBatteries, boolean loseDays) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(usedBatteries), String.valueOf(loseDays)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.DEFEAT, params);
        out.writeObject(message);
    }

    //invoked when a player decides to load goods inside cargo hold components of its ship
    @Override
    public void loadGoods(int gameID, String nickname, List<Integer> x, List<Integer> y) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, serializeList(x), serializeList(y)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.LOAD_GOODS, params);
        out.writeObject(message);
    }

    //invoked when a player wants to land on a planet
    @Override
    public void planetLanding(int gameID, String nickname, int numberPlanet) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(numberPlanet)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.PLANET_LANDING, params);
        out.writeObject(message);
    }

    //invoked when a player wants to use batteries to declare its engine/cannon strength
    @Override
    public void useBatteries(int gameID, String nickname, int usedBatteries) throws IOException{
        List<String> params = new ArrayList<>(Arrays.asList(nickname, String.valueOf(usedBatteries)));
        PlayerActionMessage message = new PlayerActionMessage(PlayerActionType.USE_BATTERIES, params);
        out.writeObject(message);
    }

}
