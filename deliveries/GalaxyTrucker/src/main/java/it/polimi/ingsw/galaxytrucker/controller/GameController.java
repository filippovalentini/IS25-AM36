package it.polimi.ingsw.galaxytrucker.controller;

import it.polimi.ingsw.galaxytrucker.model.enumerations.*;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import java.util.List;

public class GameController {
    private final GameState model;

    public GameController(boolean firstFlight, int numPlayers) {
        this.model = new GameState(firstFlight, numPlayers);
    }

    //STARTING PHASE

    //invoked when one of the players decides to start the assembling phase
    public int addPlayer(String nickname, Color color) {
        synchronized (model) {
            try{
                model.addPlayer(nickname, color);
                if(model.getGameState() == State.WAITING_FOR_PLAYERS){
                    return 0;       //still waiting for players
                }
                else{
                    return 1;       //start assembling phase
                }
            }
            catch(InvalidActionException e){
                return -1;           //game already started
            }
            catch(UniqueNicknameException e){
                return -2;          //already existing name
            }
            catch(UniquePlayerColorException e){
                return -3;          //already chosen color
            }
        }
    }

    //ASSEMBLING PHASE

    //invoked when a player wants to pick a component among the one placed face down (assembling phase)
    public int pickHidden(String nickname){
        synchronized (model) {
            try{
                model.pickHidden(nickname);
                return 0;       //successful picked component
            }
            catch(InvalidActionException e){
                return -1;      //invalid action (wrong game phase)
            }
            catch(PickedComponentException e){
                return -2;      //already picked one component
            }
        }
    }
    //invoked when a player wants to pick a specific component among the one placed face up (assembling phase)
    public int pickShown(String nickname, int index){
        synchronized (model) {
            try{
                model.pickShown(nickname, index);
                return 0;       //successful picked component
            }
            catch(InvalidActionException e){
                return -1;      //invalid action (wrong game phase)
            }
            catch(PickedComponentException e){
                return -2;      //already picked one component
            }
        }
    }
    //invoked when a player wants to reserve the component that it has picked for its ship board
    public int reserveComponent(String nickname){
        synchronized (model) {
            try{
                model.reserveComponent(nickname);
                return 0;       //successful reserved component
            }
            catch(InvalidActionException e){
                return -1;      //invalid action (wrong game phase)
            }
            catch(PickedComponentException e){
                return -2;      //no picked component to reserve
            }
            catch(ReservedComponentException e){
                return -3;      //too many reserved components
            }
        }
    }
    //invoked when a player wants to pick one of the components that it has reserved for its ship board
    public int pickReservedComponent(String nickname, int position)  {
        synchronized (model) {
            try{
                model.pickReservedComponent(nickname, position);
                return 0;       //successful picked component
            }
            catch(InvalidActionException e){
                return -1;      //invalid action (wrong game phase)
            }
            catch(PickedComponentException e){
                return -2;      //already picked component
            }
            catch(ReservedComponentException e){
                return -3;      //no reserved component in this position
            }
        }
    }
    //invoked when a player wants to release (therefore, place face up) the component that it has picked
    public int putShown(String nickname)  {
        synchronized (model) {
            try{
                model.putShown(nickname);
                return 0;       //successful released component
            }
            catch(InvalidActionException e){
                return -1;      //invalid action (wrong game phase)
            }
            catch(PickedComponentException e){
                return -2;      //no picked component to release
            }
        }
    }
    //invoked when a player wants to assemble on the ship board the component that it has picked
    public int assembleComponent(String nickname, int x, int y)  {
        synchronized (model) {
            try{
                model.assembleComponent(nickname, x, y);
                return 0;       //successful assembled component
            }
            catch(InvalidActionException e){
                return -1;      //invalid action (wrong game phase)
            }
            catch(PickedComponentException e){
                return -2;      //no picked component to assemble
            }
            catch (AssembledComponentException e){
                return -3;      //already assembled component
            }
        }
    }
    //invoked when a player wants to change the orientation of the component that it has picked
    public int rotatePickedComponent(String nickname) {
        synchronized (model) {
            try{
                model.rotatePickedComponent(nickname);
                return 0;       //successful rotated component
            }
            catch(InvalidActionException e){
                return -1;      //invalid action (wrong game phase)
            }
            catch(PickedComponentException e) {
                return -2;      //no picked component to rotate
            }
        }
    }
    //invoked when a player has finished the assembling phase and has to pick a free position on the flight board
    public int setPosition(String nickname, int initCell)  {
        synchronized (model) {
            try{
                model.setPosition(nickname, initCell);
                if(model.getGameState() == State.SHIP_BUILDING){
                    return 0;       //set initial position, waiting for other players to finish building
                }
                else{
                    return 1;       //set initial position and assembling phase finished
                }
            }
            catch(InvalidActionException e){
                return -1;      //invalid action (wrong game phase)
            }
            catch(InvalidPositionException e) {
                return -2;      //wrong initial position
            }
        }
    }

    //SHIP CONTROL PHASE

    public int destroyComponent(String nickname, int x, int y)  {
        synchronized (model) {
            try{
                model.destroyComponent( nickname, x, y);
                if(model.getGameState() == State.SHIP_CONTROL){
                    return 0;       //component destroyed, but still other components must be destroyed, belonging
                                    //to the player's ship or to one of other players.
                }else{
                    return 1;       //component destroyed, all the ships are ok and the game can start
                }
            }
            catch(InvalidActionException e){
                return -1;          //invalid action (wrong game phase)
            }
            catch(AssembledComponentException e){
                return -2;          //no assembled component to destroy
            }
        }
    }

    //FLIGHT PHASE

    //this method is invoked when a player has to leave the game
    public int quitGame(String nickname)  {
        synchronized (model) {
            model.quitGame(nickname);
            return 0;           //player has correctly left the game
        }
    }
    //invoked when the leader draws a new card from the deck (during the game), which must be solved
    public int pickNextCard(String nickname) {
        synchronized (model) {
            try{
                model.pickNextCard(nickname);
                if(model.getGameState() == State.CARD_SOLVING){
                    return 0;       //card picked, it must be solved
                }else{
                    return 1;       //no card to pick, game over
                }
            }
            catch(InvalidActionException e){
                return -1;          //invalid action (wrong game phase)
            }
        }
    }
    //invoked when a player decides to land on a planet in order to gain goods
    public int planetLanding(String nickname, int numberPlanet) {
        synchronized (model) {
            try {
                model.planetLanding(nickname, numberPlanet);
                if(model.getGameState() == State.CARD_SOLVING){
                    return 0;       //player has finished but other player have to solve the card
                }
                else {
                    return 1;       //player has finished and a new card has to be picked
                }
            }
            catch(InvalidActionException e){
                return -1;          //invalid action (wrong game phase)
            }
        }
    }
    //invoked when a player's ship has to be hit by a meteor/cannon shot; the player can decide whether to
    //activate a shield or a cannon to defend its ship
    public int hit(String nickname, int diceResult, boolean activateShield, boolean activateCannon) {
        synchronized (model) {
            try {
                model.hit(nickname, diceResult, activateShield, activateCannon);
                if(model.getGameState() == State.CARD_SOLVING){
                    return 0;       //player has finished but other player have to solve the card
                }
                else {
                    return 1;       //player has finished and a new card has to be picked
                }
            }
            catch(InvalidActionException e){
                return -1;          //invalid action (wrong game phase)
            }
            catch(NoBatteriesException e) {
                return -2;          //player doesn't have enough batteries
            }
        }
    }
    //invoked when a player decides to land on an abandoned station/ship
    public int landing(String nickname, List<Integer> x, List<Integer> y, List<Integer> z) {
        synchronized (model) {
            try {
                model.landing(nickname, x, y, z);
                if(model.getGameState() == State.CARD_SOLVING){
                    return 0;       //player has finished but other player have to solve the card
                }
                else {
                    return 1;       //player has finished and a new card has to be picked
                }
            }
            catch(InvalidActionException e){
                return -1;          //invalid action (wrong game phase)
            }
            catch(NoCrewException e) {
                return -2;          //player doesn't have enough crew
            }
        }
    }
    //invoked when a player wants to defeat an enemy; the player can decide whether to lose flight days
    //to gain credits/goods or not
    public int defeat(String nickname, int usedBatteries, boolean loseDays) {
        synchronized (model) {
            try {
                model.defeat(nickname, usedBatteries, loseDays);
                if(model.getGameState() == State.CARD_SOLVING){
                    return 0;       //player has finished but other player have to solve the card
                }
                else {
                    return 1;       //player has finished and a new card has to be picked
                }
            }
            catch(InvalidActionException e){
                return -1;          //invalid action (wrong game phase)
            }
            catch(NoBatteriesException e) {
                return -2;          //player doesn't have enough batteries
            }
        }
    }
    //invoked when a player wants to fly across the flight board exploiting its engine strength
    public int fly(String nickname, int usedBatteries)  {
        synchronized (model) {
            try {
                model.fly(nickname, usedBatteries);
                if(model.getGameState() == State.CARD_SOLVING){
                    return 0;       //player has finished but other player have to solve the card
                }
                else {
                    return 1;       //player has finished and a new card has to be picked
                }
            }
            catch(InvalidActionException e){
                return -1;          //invalid action (wrong game phase)
            }
            catch(NoBatteriesException e) {
                return -2;          //player doesn't have enough batteries
            }
        }
    }
    //invoked when a player wants to use batteries to have an advantage while solving a card
    public int useBatteries(String nickname, int usedBatteries)  {
        synchronized (model) {
            try {
                model.useBatteries(nickname, usedBatteries);
                if(model.getGameState() == State.CARD_SOLVING){
                    return 0;       //player has finished but other player have to solve the card
                }
                else {
                    return 1;       //player has finished and a new card has to be picked
                }
            }
            catch(InvalidActionException e){
                return -1;          //invalid action (wrong game phase)
            }
            catch(NoBatteriesException e) {
                return -2;          //player doesn't have enough batteries
            }
        }
    }
    //invoked when a player doesn't want to exploit the benefits of a card and therefore skips the turn
    public int skip(String nickname)  {
        synchronized (model) {
            try {
                model.skip(nickname);
                if(model.getGameState() == State.CARD_SOLVING){
                    return 0;       //player has finished but other player have to solve the card
                }
                else {
                    return 1;       //player has finished and a new card has to be picked
                }
            }
            catch(InvalidActionException e){
                return -1;          //invalid action (wrong game phase)
            }
        }
    }



}
