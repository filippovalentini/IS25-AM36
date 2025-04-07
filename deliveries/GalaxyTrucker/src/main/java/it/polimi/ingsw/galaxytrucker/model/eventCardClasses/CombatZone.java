package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoBatteriesException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoCrewException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.Player;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.CannonShot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//COMBAT ZONE
public class CombatZone extends EventCard{
    private final boolean levelOne;     //discriminates between level I and level II card
    private int phase;
    private double worstCannonStrength;
    private int worstEngineStrength;
    private String worstEnginePlayer;
    private String worstCannonPlayer;
    private List<CannonShot> cannonShots;
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
            if (phase != 3) {
                throw new InvalidActionException("Wrong phase of the combat zone");
            } else {
                String nickname = gameState.getCrewMinPlayer();
                gameState.updateTurns();

            }

        }
    }

    @Override
    //invoked when a player wants to declare its cannon strength or engine strength
    public void useBatteries(GameState gameState, String nickname, int usedBatteries) throws InvalidActionException, NoBatteriesException {
        if(usedBatteries < gameState.getNumberBatteries(nickname)){
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
                double cannonStrenght= gameState.getCannonStrength(nickname, usedBatteries);
                if(cannonStrenght < worstCannonStrength){
                    worstCannonPlayer = nickname;
                    worstCannonStrength = cannonStrenght;
                }
                if(gameState.isLastInTurn(nickname)){
                    gameState.setTurnPlayer(worstCannonPlayer);
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
            if (gameState.getCrewCount(nickname)<= 2) {
                gameState.removedCrewMember(nickname, x, y, z, gameState.getCrewCount(nickname));
                gameState.quitGame(nickname);
            }else{
                gameState.removedCrewMember(nickname, x, y, z, 2);
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
            if(phase==2) {
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
                    currentShot = 0;
                    gameState.nextTurn();
                }
                }
            }else {
                currentShot++;
            }
        }
    }
    }










