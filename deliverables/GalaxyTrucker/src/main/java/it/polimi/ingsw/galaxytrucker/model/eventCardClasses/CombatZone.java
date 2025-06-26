package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoBatteriesException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoCrewException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.CannonShot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * CombatZone class represents a event card.
 */
public class CombatZone extends EventCard{
    private final boolean levelOne;     //discriminates between level I and level II card
    private int phase;   //discriminates between the different phases of the card
    private double worstCannonStrength;   //stores the worst cannon strength of the players
    private int worstEngineStrength;  //stores the worst engine strength of the players
    private String worstEnginePlayer;   //stores the nickname of the player with the worst engine strength
    private String worstCannonPlayer;  //stores the nickname of the player with the worst cannon strength
    private final List<CannonShot> cannonShots; //stores the cannon shots of the card
    private int currentShot;   //stores the index of the current cannon shot to be used

    /**
     * CombatZone constructor.
     * @param levelOne
     * @param imageID
     */
    public CombatZone(boolean levelOne, int imageID) {     //constructor
        super(imageID);
        this.levelOne = levelOne;
        this.phase = 1;
        this.worstCannonStrength = 1000;
        this.worstEngineStrength = 1000;
        this.worstEnginePlayer = "";
        this.worstCannonPlayer = "";
        this.currentShot = 0;
        if(levelOne){
            this.cannonShots = new ArrayList<>(Arrays.asList(new CannonShot(false, Orientation.SOUTH), new CannonShot(true, Orientation.SOUTH)));
        }else{
            this.cannonShots = new ArrayList<>(Arrays.asList(new CannonShot(false, Orientation.NORTH), new CannonShot(false, Orientation.WEST), new CannonShot(false, Orientation.EAST), new CannonShot(true, Orientation.SOUTH)));
        }
    }

    /**
     * returns the phase of the combat zone.
     * @return phase
     */
    public int getPhase() {
        return phase;
    }

    /**
     * Returns the worst engine strength of the players.
     * @return worstEngineStrength
     */
    public String getWorstEnginePlayer() {
        return worstEnginePlayer;
    }

    /**
     * If a player leaves the game during a phase of the combat zone, the resolution of the card switches to the next phase as the competition between the players is compromised
     * @param gameState
     * @param nickname
     */
    @Override
    public void manageGameQuit(GameState gameState, String nickname){
        if(phase == 3){
            gameState.setGameState(State.CARD_PICKING);
        }
        else {
            phase++;
        }
        gameState.updateTurns();
    }

    /**
     * Special effect of the combat zone.
     * @param gameState
     * @throws InvalidActionException
     */
    @Override
    //the player with fewer crew members loses 3 flight days
    public void specialEffect(GameState gameState) throws InvalidActionException {
        if (this.levelOne) { //default effect of level one combat zone
            if (phase != 1) {
                throw new InvalidActionException("Wrong phase of the combat zone");
            } else {
                String nickname = gameState.getCrewMinPlayer(); //gets the player with fewer crew members
                gameState.changePlayerPosition(nickname, -3); //loses 3 flight days
                gameState.updateTurns(); // updates the turns
                phase = 2;
            }
        }
    }

    /**
     * Method invoked when a player wants to declare its cannon strength or engine strength.
     * @param gameState
     * @param nickname
     * @param usedBatteries
     * @throws InvalidActionException
     * @throws NoBatteriesException
     */
    @Override
    //invoked when a player wants to declare its cannon strength or engine strength
    public void useBatteries(GameState gameState, String nickname, int usedBatteries) throws InvalidActionException, NoBatteriesException {
        if(usedBatteries > gameState.getNumberBatteries(nickname)){ //if the player doesn't have enough batteries, it can't invoke this method
            throw new NoBatteriesException("Player doesn't have enough batteries");
        }
        if(this.levelOne){ //level one
            if(phase==2){ //if phase is 2, the player has to declare its engine strength
                int engineStrength = gameState.getEngineStrength(nickname, usedBatteries); // gets the engine strength of the player
                if (engineStrength < worstEngineStrength) { //if the engine strength is lower than the worst engine strength, it updates the worst engine player and strength
                    worstEnginePlayer = nickname;
                    worstEngineStrength = engineStrength;
                }
                if(gameState.isLastInTurn(nickname)) { //if the player is the last in turn, it sets the turn player to the worst engine player
                    gameState.setTurnPlayer(worstEnginePlayer);
                }else{
                    gameState.nextTurn(); //otherwise it goes to the next turn
                }
            }
            else if(phase==3){ //if phase is 3, the player has to declare its cannon strength
                double cannonStrength = gameState.getCannonStrength(nickname, usedBatteries); // gets the cannon strength of the player
                if (cannonStrength < worstCannonStrength) { //if the cannon strength is lower than the worst cannon strength, it updates the worst cannon player and strength
                    worstCannonPlayer = nickname;
                    worstCannonStrength = cannonStrength;
                }
                if(gameState.isLastInTurn(nickname)) { //if the player is the last in turn, it sets the turn player to the worst cannon player
                    gameState.setTurnPlayer(worstCannonPlayer);
                }else{
                    gameState.nextTurn();
                }
            }
            else{ //if phase is not 2 or 3, it throws an exception
                throw new InvalidActionException("Wrong phase of the combat zone");
            }
        }
        else{ //level two
            if(phase==1){ //if phase is 1, the player has to declare its cannon strength
                double cannonStrength = gameState.getCannonStrength(nickname, usedBatteries); // gets the cannon strength of the player
                if (cannonStrength < worstCannonStrength) { //if the cannon strength is lower than the worst cannon strength, it updates the worst cannon player and strength
                    worstCannonPlayer = nickname;
                    worstCannonStrength = cannonStrength;
                }
                if(gameState.isLastInTurn(nickname)) { //if the player is the last in turn, it sets the turn player to the worst cannon player
                    gameState.changePlayerPosition(worstCannonPlayer, -4);
                    gameState.updateTurns();
                    phase = 2;
                    /*
                        after the last in turn player has decided to use the batteries,
                         the worst player can just lose the positions with changePlayerPos(...)
                     */
                }else{
                    gameState.nextTurn();
                }
            }
            else if(phase==2){ //if phase is 2, the player has to declare its engine strength
                int engineStrength = gameState.getEngineStrength(nickname, usedBatteries); // gets the engine strength of the player
                if(engineStrength < worstEngineStrength){ //if the engine strength is lower than the worst engine strength, it updates the worst engine player and strength
                    worstEnginePlayer = nickname;
                    worstEngineStrength = engineStrength;
                }
                if(gameState.isLastInTurn(nickname)) { //if the player is the last in turn, it sets the turn player to the worst engine player
                    gameState.losePreciousGoods(worstEnginePlayer, 3);
                    gameState.setTurnPlayer(gameState.getCrewMinPlayer());
                    phase = 3;
                    /*
                        after the last in turn player has decided to use the batteries,
                         the worst player can just lose the 3 most precious goods (or batteries if goods are not enough)
                     */
                }else{
                    gameState.nextTurn();
                }
            }
            else{
                throw new InvalidActionException("Wrong phase of the combat zone");
            }

        }
    }

    /**
     * Method invoked when the player with smaller engine strength must lose crew members (level one)
     * @param gameState
     * @param nickname
     * @param x
     * @param y
     * @param z
     * @throws InvalidActionException
     * @throws NoCrewException
     */
    @Override

    public void landing(GameState gameState, String nickname, List<Integer> x, List<Integer> y, List<Integer> z) throws InvalidActionException, NoCrewException {
        if(levelOne){ //level one
            if(phase!=2){ //if phase is not 2, it throws an exception
                throw new InvalidActionException("Wrong phase of the combat zone");
            }
            else if (gameState.getCrewCount(nickname)<= 2) { //if the player has 2 or fewer crew members, it removes all the crew members and quits the game
                gameState.removeCrewMembers(nickname, x, y, z, gameState.getCrewCount(nickname));
                gameState.quitGame(nickname, false);
                phase = 3; //updates the phase to 3 as the player has lost all its crew members
                gameState.updateTurns();
                throw new NoCrewException("You have lost all your crew: quitting game...");
            }else{ //if the player has more than 2 crew members, it removes 2 crew members from the ship
                gameState.removeCrewMembers(nickname, x, y, z, 2);
            }
            phase = 3; //updates the phase to 3 as the player has lost 2 crew members
            gameState.updateTurns();
        }else{ //level two
            throw new InvalidActionException("Invalid action for level 2 combat zone");
        }
    }

    @Override
    /**
     * Method invoked when the player with smaller cannon strength (level one) or crew (level two) has to be hit by a cannon shot
        * @param gameState
     * @param nickname
     * @param diceResult
     * @param activateShield
     * @param activateCannon
     * @throws InvalidActionException
     */
    public void hitShip(GameState gameState, String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws InvalidActionException {
        if(levelOne) { //level one
            if(phase==3) { //cannon shot from special effect
                if (activateShield && gameState.getNumberBatteries(nickname) == 0) { //if the player doesn't have enough batteries, it can't invoke this method
                    throw new InvalidActionException("Too few batteries");
                }
                Orientation orientation = cannonShots.get(currentShot).getOrientation(); // gets the orientation of the cannon shot
                int direction = (orientation.isVertical() ? diceResult - 4 : diceResult - 5); // calculates the direction of the cannon shot
                gameState.cannonFireAttack(nickname, cannonShots.get(currentShot), direction, activateShield); // current cannon shot hits the ship
                if(currentShot == cannonShots.size() - 1){ //if no more cannon shots have to hit the ship, the player's turn is finished
                    gameState.checkDamages(); // checks the damages of the ship
                    if (gameState.getCrewCount(nickname)==0) { //if the player has no crew members left, it quits the game
                        gameState.quitGame(nickname, false); // quits the game
                        throw new NoCrewException("You have lost all your crew: quitting game...");
                    }else{
                        gameState.updateTurns(); // goes to the next turn
                    }
                }
                else{
                    currentShot++; //otherwise the cannon shot counter is incremented and the current player in turn will have to invoke this method again
                }
            }
            else{
                throw new InvalidActionException("Wrong phase of the combat zone");
            }
        }else{ //level two
            if(phase==3){ //cannon shot from special effect
                if (activateShield && gameState.getNumberBatteries(nickname) == 0) { //if the player doesn't have enough batteries, it can't invoke this method
                    throw new InvalidActionException("Too few batteries");
                }
                Orientation orientation = cannonShots.get(currentShot).getOrientation(); // gets the orientation of the cannon shot
                int direction = (orientation.isVertical() ? diceResult - 4 : diceResult - 5); // calculates the direction of the cannon shot
                gameState.cannonFireAttack(nickname, cannonShots.get(currentShot), direction, activateShield); // current cannon shot hits the ship
                if(currentShot == cannonShots.size() - 1){ //if no more cannon shots have to hit the ship, the player's turn is finished
                    gameState.checkDamages(); // checks the damages of the ship
                    if (gameState.getCrewCount(nickname)==0) { //if the player has no crew members left, it quits the game
                        gameState.quitGame(nickname, false); // quits the game
                        throw new NoCrewException("You have lost all your crew: quitting game...");
                    }else{
                        gameState.updateTurns();
                    }
                }
                else{
                    currentShot++;
                }
            }
            else{
                throw new InvalidActionException("Wrong phase of the combat zone");
            }
        }
    }


}










