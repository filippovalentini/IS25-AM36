package it.polimi.ingsw.galaxytrucker.network.rmi.server;

import it.polimi.ingsw.galaxytrucker.controller.GameController;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.network.VirtualView;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.VirtualServerRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServerRMI extends UnicastRemoteObject implements VirtualServerRMI {
    private final Map<Integer, GameController> controllers;             //maps each controller with the ID of the respective game
    private final Map<String, VirtualViewRMI> clients = new HashMap<>();    //maps each client with the nickname of the respective player

    //constructor, initializes the controllers mapping with a provided empty map
    public ServerRMI(Map<Integer, GameController> controllers) throws RemoteException {
        super();
        this.controllers = controllers;
    }

    //invoked by the ping thread when a client disconnection has been detected
    public void manageClientDisconnection(int gameID, String nickname) throws RemoteException {
        try{
            controllers.get(gameID).forceQuit(nickname);
            clients.remove(nickname);
        } catch (Exception ignored) {}
    }

    //determines if a game with the specified ID has exists already
    @Override
    public boolean startedGame(int gameID) throws RemoteException{
        return controllers.containsKey(gameID);
    }

    //invoked when a player decides to start a new game
    @Override
    public void startNewGame(VirtualView client, int gameID, boolean firstFlight, int numberPlayers) throws RemoteException{
        try{
            GameController controller = new GameController(firstFlight, numberPlayers);
            controllers.put(gameID, controller);
        }
        catch(Exception e){
            try{
                client.notifyError(e.getMessage());
            }
            catch(Exception e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when one of the players decides enter the game; the remote client view is added to the list
    //of connected clients
    @Override
    public boolean addPlayer(VirtualView client, int gameID, String nickname, Color color) throws RemoteException {
        boolean addedToGame = false;
        try{
            controllers.get(gameID).addPlayer(client, nickname, color);
            clients.put(nickname, (VirtualViewRMI) client);
            addedToGame = true;
            new PingThreadRMI(this, (VirtualViewRMI) client, gameID, nickname).start();
        }
        catch(Exception e){
            try{
                client.notifyError(e.getMessage());
            }
            catch(Exception e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
        return addedToGame;
    }

    //invoked when a player wants to pick a component among the one placed face down (assembling phase)
    @Override
    public void pickHidden(int gameID, String nickname) throws RemoteException{
        try{
            controllers.get(gameID).pickHidden(nickname);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to pick a specific component among the one placed face up (assembling phase)
    @Override
    public void pickShown(int gameID, String nickname, int index) throws RemoteException{
        try{
            controllers.get(gameID).pickShown(nickname, index);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to release (therefore, place face up) the component that it has picked
    @Override
    public void putShown(int gameID, String nickname) throws RemoteException{
        try{
            controllers.get(gameID).putShown(nickname);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to reserve the component that it has picked for its ship board
    @Override
    public void reserveComponent(int gameID, String nickname) throws RemoteException{
        try{
            controllers.get(gameID).reserveComponent(nickname);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to pick one of the components that it has reserved for its ship board
    @Override
    public void pickReservedComponent(int gameID, String nickname, int position) throws RemoteException{
        try{
            controllers.get(gameID).pickReservedComponent(nickname, position);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to change the orientation of the component that it has picked
    @Override
    public void rotatePickedComponent(int gameID, String nickname) throws RemoteException{
        try{
            controllers.get(gameID).rotatePickedComponent(nickname);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to assemble on the ship board the component that it has picked
    @Override
    public void assembledComponent(int gameID, String nickname, int x, int y) throws RemoteException{
        try{
            controllers.get(gameID).assembleComponent(nickname, x, y);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to pick a deck during the assembling phase to see its content
    @Override
    public void pickDeck(int gameID, String nickname, int deckNumber) throws RemoteException{
        try{
            controllers.get(gameID).pickDeck(nickname, deckNumber);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to release the deck it has picked, during the assembling phase
    @Override
    public void releaseDeck(int gameID, String nickname) throws RemoteException{
        try{
            controllers.get(gameID).releaseDeck(nickname);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player has finished the assembling phase and has to pick a free position on the flight board
    @Override
    public void setPosition(int gameID, String nickname, int initCell) throws RemoteException{
        try{
            controllers.get(gameID).setPosition(nickname, initCell);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to turn around the hourglass
    @Override
    public void startNewCycle(int gameID, String nickname) throws RemoteException{
        try{
            controllers.get(gameID).startNewCycle(nickname);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to destroy a component in order to validate its ship board or when a
    //component is destroyed due to a cannon shot/meteor attack
    @Override
    public void destroyComponent(int gameID, String nickname, int x, int y) throws RemoteException{
        try{
            controllers.get(gameID).destroyComponent(nickname, x, y);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to initialize a cabin of its shipboard with 2 human crew members
    @Override
    public void addCrew(int gameID, String nickname, int x, int y) throws RemoteException{
        try{
            controllers.get(gameID).addCrew(nickname, x, y);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to initialize a battery container of its shipboard with batteries
    @Override
    public void addBatteries(int gameID, String nickname, int x, int y) throws RemoteException{
        try{
            controllers.get(gameID).addBatteries(nickname, x, y);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to initialize a cabin of its shipboard with an alien
    @Override
    public void addAlien(int gameID, String nickname, boolean isPurple, int x, int y) throws RemoteException{
        try{
            controllers.get(gameID).addAlien(nickname, isPurple, x, y);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to pick a new card from the game deck
    @Override
    public void pickNextCard(int gameID, String nickname) throws RemoteException{
        try{
            controllers.get(gameID).pickNextCard(nickname);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to leave the game
    @Override
    public void quitGame(int gameID, String nickname) throws RemoteException{
        try{
            controllers.get(gameID).quitGame(nickname);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to skip an action during the flight phase
    @Override
    public void skip(int gameID, String nickname) throws RemoteException{
        try{
            controllers.get(gameID).skip(nickname);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to land on an abandoned ship or station
    @Override
    public void landing(int gameID, String nickname, List<Integer> x, List<Integer> y, List<Integer> z) throws RemoteException{
        try{
            controllers.get(gameID).landing(nickname, x, y, z);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when th ship board of a player must be hit by meteor/cannon shot
    @Override
    public void hitShip(int gameID, String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws RemoteException{
        try{
            controllers.get(gameID).hit(nickname, diceResult, activateShield, activateCannon);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to fly across the flight board exploiting its engine strength
    @Override
    public void fly(int gameID, String nickname, int usedBatteries) throws RemoteException{
        try{
            controllers.get(gameID).fly(nickname, usedBatteries);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to defeat an enemy; the player can decide whether to lose flight days
    //to gain credits/goods or not
    @Override
    public void defeat(int gameID, String nickname, int usedBatteries, boolean loseDays) throws RemoteException{
        try{
            controllers.get(gameID).defeat(nickname, usedBatteries, loseDays);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player decides to load goods inside cargo hold components of its ship
    @Override
    public void loadGoods(int gameID, String nickname, List<Integer> x, List<Integer> y) throws RemoteException{
        try{
            controllers.get(gameID).loadGoods(nickname, x, y);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to land on a planet
    @Override
    public void planetLanding(int gameID, String nickname, int numberPlanet) throws RemoteException{
        try{
            controllers.get(gameID).planetLanding(nickname, numberPlanet);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }

    //invoked when a player wants to use batteries to declare its engine/cannon strength
    @Override
    public void useBatteries(int gameID, String nickname, int usedBatteries) throws RemoteException{
        try{
            controllers.get(gameID).useBatteries(nickname, usedBatteries);
        }
        catch(Exception e){
            try{
                clients.get(nickname).notifyError(e.getMessage());
            }
            catch(RemoteException e1){
                System.out.println("Error during remote method invocation on client");
            }
        }
    }
}
