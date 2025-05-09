package it.polimi.ingsw.galaxytrucker.network.rmi.server;

import it.polimi.ingsw.galaxytrucker.controller.GameController;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.network.VirtualView;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.VirtualServerRMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServerRMI extends UnicastRemoteObject implements VirtualServerRMI {
    private final GameController controller;
    final Map<String, VirtualViewRMI> clients = new HashMap<>();    //maps each client with the nickname of the respective player

    //constructor, initializes the game controller
    public ServerRMI(boolean firstFlight, int numPlayers) throws RemoteException {
        super();
        this.controller = new GameController(firstFlight, numPlayers);
    }

    //launches the server and receives in input for the user the number of players and the type of game
    //(standard game or first flight), in order to set up the server correctly
    public static void main(String[] args) throws RemoteException {
        int numPlayers;
        String ff;
        boolean firstFlight;
        Scanner inputScanner = new Scanner(System.in);
        do{
            System.out.println("Number of players (from 1 to 4): ");
            numPlayers = Integer.parseInt(inputScanner.nextLine());
        }while(numPlayers>4 || numPlayers<1);
        do{
            System.out.println("Standard game (S) or first flight (F): ");
            ff = inputScanner.nextLine();
        }while(!ff.equals("F") && !ff.equals("S"));
        firstFlight = (ff.equals("F"));
        ServerRMI server = new ServerRMI(firstFlight, numPlayers);
        final String serverName = "GalaxyTruckerServer";
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName, server);
        System.out.println("Waiting for remote invocation...");
    }


    //invoked when one of the players decides enter the game; the remote client view is added to the list
    //of connected clients
    @Override
    public boolean addPlayer(VirtualView client, String nickname, Color color) throws RemoteException {
        boolean addedToGame = false;
        try{
            controller.addPlayer(client, nickname, color);
            clients.put(nickname, (VirtualViewRMI) client);
            addedToGame = true;
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
    public void pickHidden(String nickname) throws RemoteException{
        try{
            controller.pickHidden(nickname);
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
    public void pickShown(String nickname, int index) throws RemoteException{
        try{
            controller.pickShown(nickname, index);
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
    public void putShown(String nickname) throws RemoteException{
        try{
            controller.putShown(nickname);
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
    public void reserveComponent(String nickname) throws RemoteException{
        try{
            controller.reserveComponent(nickname);
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
    public void pickReservedComponent(String nickname, int position) throws RemoteException{
        try{
            controller.pickReservedComponent(nickname, position);
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
    public void rotatePickedComponent(String nickname) throws RemoteException{
        try{
            controller.rotatePickedComponent(nickname);
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
    public void assembledComponent(String nickname, int x, int y) throws RemoteException{
        try{
            controller.assembleComponent(nickname, x, y);
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
    public void pickDeck(String nickname, int deckNumber) throws RemoteException{
        try{
            controller.pickDeck(nickname, deckNumber);
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
    public void releaseDeck(String nickname) throws RemoteException{
        try{
            controller.releaseDeck(nickname);
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
    public void setPosition(String nickname, int initCell) throws RemoteException{
        try{
            controller.setPosition(nickname, initCell);
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
    public void destroyComponent(String nickname, int x, int y) throws RemoteException{
        try{
            controller.destroyComponent(nickname, x, y);
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
    public void addCrew(String nickname, int x, int y) throws RemoteException{
        try{
            controller.addCrew(nickname, x, y);
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
    public void addBatteries(String nickname, int x, int y) throws RemoteException{
        try{
            controller.addBatteries(nickname, x, y);
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
    public void addAlien(String nickname, boolean isPurple, int x, int y) throws RemoteException{
        try{
            controller.addAlien(nickname, isPurple, x, y);
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
    public void pickNextCard(String nickname) throws RemoteException{
        try{
            controller.pickNextCard(nickname);
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
    public void quitGame(String nickname) throws RemoteException{
        try{
            controller.quitGame(nickname);
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
    public void skip(String nickname) throws RemoteException{
        try{
            controller.skip(nickname);
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
    public void landing(String nickname, List<Integer> x, List<Integer> y, List<Integer> z) throws RemoteException{
        try{
            controller.landing(nickname, x, y, z);
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
    public void hitShip(String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws RemoteException{
        try{
            controller.hit(nickname, diceResult, activateShield, activateCannon);
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
    public void fly(String nickname, int usedBatteries) throws RemoteException{
        try{
            controller.fly(nickname, usedBatteries);
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
    public void defeat(String nickname, int usedBatteries, boolean loseDays) throws RemoteException{
        try{
            controller.defeat(nickname, usedBatteries, loseDays);
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
    public void loadGoods(String nickname, List<Integer> x, List<Integer> y) throws RemoteException{
        try{
            controller.loadGoods(nickname, x, y);
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
    public void planetLanding(String nickname, int numberPlanet) throws RemoteException{
        try{
            controller.planetLanding(nickname, numberPlanet);
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
