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

//COMBAT ZONE
public class CombatZone extends EventCard{
    private final boolean levelOne;     //discriminates between level I and level II card
    private int phase;
    private double worstCannonStrength;
    private int worstEngineStrength;
    private String worstEnginePlayer;
    private String worstCannonPlayer;
    private final List<CannonShot> cannonShots;
    private int currentShot;

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

    @Override
    //the player with fewer crew members loses 3 flight days
    public void specialEffect(GameState gameState) throws InvalidActionException {
        if (this.levelOne) { //default effect of level one combat zone
            if (phase != 1) {
                throw new InvalidActionException("Wrong phase of the combat zone");
            } else {
                String nickname = gameState.getCrewMinPlayer();
                gameState.changePlayerPosition(nickname, -3);
                gameState.updateTurns();
                phase = 2;
            }
        } else { //default effect of level two combat zone
                throw new InvalidActionException("This action is only available for level one card");
        }
    }

    @Override
    //invoked when a player wants to declare its cannon strength or engine strength
    public void useBatteries(GameState gameState, String nickname, int usedBatteries) throws InvalidActionException, NoBatteriesException {
        if(usedBatteries > gameState.getNumberBatteries(nickname)){
            throw new NoBatteriesException("Player doesn't have enough batteries");
        }
        if(this.levelOne){
            if(phase==2){
                int engineStrength = gameState.getEngineStrength(nickname, usedBatteries);
                if (engineStrength < worstEngineStrength) {
                    worstEnginePlayer = nickname;
                    worstEngineStrength = engineStrength;
                }
                if(gameState.isLastInTurn(nickname)) {
                    gameState.setTurnPlayer(worstEnginePlayer);
                }else{
                    gameState.nextTurn();
                }
            }
            else if(phase==3){
                double cannonStrength = gameState.getCannonStrength(nickname, usedBatteries);
                if (cannonStrength < worstCannonStrength) {
                    worstCannonPlayer = nickname;
                    worstCannonStrength = cannonStrength;
                }
                if(gameState.isLastInTurn(nickname)) {
                    gameState.setTurnPlayer(worstCannonPlayer);
                }else{
                    gameState.nextTurn();
                }
            }
            else{
                throw new InvalidActionException("Wrong phase of the combat zone");
            }
        }
        else{ //level two
            if(phase==1){
                double cannonStrength = gameState.getCannonStrength(nickname, usedBatteries);
                if (cannonStrength < worstCannonStrength) {
                    worstCannonPlayer = nickname;
                    worstCannonStrength = cannonStrength;
                }
                if(gameState.isLastInTurn(nickname)) {
                    gameState.setTurnPlayer(worstCannonPlayer);
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
            else if(phase==2){
                int engineStrength = gameState.getEngineStrength(nickname, usedBatteries);
                if(engineStrength < worstEngineStrength){
                    worstEnginePlayer = nickname;
                    worstEngineStrength = engineStrength;
                }
                if(gameState.isLastInTurn(nickname)){
                    gameState.setTurnPlayer(worstEnginePlayer);
                }else{
                    gameState.nextTurn();
                }
            }
            else{
                throw new InvalidActionException("Wrong phase of the combat zone");
            }

        }
    }

    @Override
    //invoked when the player with smaller engine strength must lose goods (level two) or crew members (level one)
    public void landing(GameState gameState, String nickname, List<Integer> x, List<Integer> y, List<Integer> z) throws InvalidActionException, NoCrewException {
        if(levelOne){
            if(phase!=2){
                throw new InvalidActionException("Wrong phase of the combat zone");
            }
            else if (gameState.getCrewCount(nickname)<= 2) {
                gameState.removeCrewMembers(nickname, x, y, z, gameState.getCrewCount(nickname));
                gameState.quitGame(nickname);
            }else{
                gameState.removeCrewMembers(nickname, x, y, z, 2);
            }
            phase = 3;
            gameState.updateTurns();
        }else{ //level two
            if(phase!=2){
                throw new InvalidActionException("Wrong phase of the combat zone");
            }
            if(!Objects.equals(nickname, worstEnginePlayer)){
                throw new InvalidActionException("You are not the player with less engine Strength");
            }
            if(gameState.getNumberGoods(nickname)<= 3) {
                gameState.losePreciousGoods(nickname,  gameState.getNumberGoods(nickname));
                /*
                    removeGoods should be a method similar to substituteGoods but instead
                    of set/add does the list.remove() from the goods list in the cargo
                 */
            }else{
                gameState.losePreciousGoods(nickname, 3);
            }
            phase = 3;
            gameState.updateTurns();
        }
    }

    @Override
    //invoked when the player with smaller cannon strength (level one) or crew (level two) has to be hit by a
    //cannon shot
    public void hitShip(GameState gameState, String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws InvalidActionException {
        if(levelOne) {
            if(phase==3) {
                if(!Objects.equals(nickname, worstCannonPlayer)){
                    throw new InvalidActionException("You are not the player with less cannon Strength");
                }
                if (activateShield && gameState.getNumberBatteries(nickname) == 0) {
                    throw new InvalidActionException("Too few batteries");
                }
                for (int i = 0; i < cannonShots.size(); i++){
                    Orientation orientation = cannonShots.get(i).getOrientation();
                    int direction = (orientation.isVertical() ? diceResult - 4 : diceResult - 5);
                    gameState.cannonFireAttack(nickname, cannonShots.get(i), direction, activateShield);
                    if (i == cannonShots.size() - 1) {
                        if (gameState.isLastInTurn(nickname)) {
                            gameState.setGameState(State.CARD_PICKING);
                        }
                        gameState.nextTurn();
                    }
                }
            }
        }else{ //level two
            if(phase==3){ //cannon shot from special effect
                if(!gameState.getCrewMinPlayer().equals(nickname)){
                    throw new InvalidActionException("This action must be invoked by crew min player");
                }
                Orientation orientation = cannonShots.get(currentShot).getOrientation();
                int direction = (orientation.isVertical() ? diceResult - 4 : diceResult - 5); //different dice result for every cannon shot
                gameState.cannonFireAttack(nickname, cannonShots.get(currentShot), direction, activateShield);
                if (currentShot == cannonShots.size() - 1) {
                    gameState.setGameState(State.CARD_PICKING);
                    currentShot = 0;
                    gameState.nextTurn();
                }else {
                    currentShot++;
                }
            }
        }
    }
    }










