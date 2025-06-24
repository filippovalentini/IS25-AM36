package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

import java.util.List;

/**
 * This class represents the smugglers event card.
 */
public class Smugglers extends DayLossCard{
    private final List<Color> prizeGoods;       //goods that a player gains by defeating the smugglers
    private final int goodLoss;     //goods that a player loses if defeated by the smugglers
    private final int enemyStrength;        //strength required to defeat the smugglers
    private boolean defeated;       //set to true if a player has defeated the smugglers
    private boolean goodsExchangePhase;

    /**
     * Constructor for the Smugglers event card.
     * @param prizeGoods
     * @param goodLoss
     * @param enemyStrength
     * @param lostDays
     * @param imageID
     */
    public Smugglers(List<Color> prizeGoods, int goodLoss, int enemyStrength, int lostDays, int imageID) {  //constructor
        super(lostDays, imageID);
        this.prizeGoods = prizeGoods;
        this.goodLoss = goodLoss;
        this.enemyStrength = enemyStrength;
        this.defeated = false;
        this.goodsExchangePhase = false;
    }

    /**
     * Determines if a player has defeated the smugglers or not.
     * @return true if the smugglers have been defeated, false otherwise.
     */
    public boolean isDefeated() {   //determines if a player has defeated the smugglers or not
        return defeated;
    }

    /**
     * Sets the defeated status of the smugglers to true.
     */
    public void setDefeated() {      //invoked when a player defeats the smugglers
        defeated = true;
    }

    @Override
    /**
     * If a player leaves the game during the crew loss phase, the card resolution switches to the fight phase for the next player in turn
     * @param gameState the current game state
     * @param nickname the nickname of the player who is quitting
     *
     */
    public void manageGameQuit(GameState gameState, String nickname){
        if(nickname.equals(gameState.getTurnPlayer())){ //if the player who is quitting is the one in turn
            if(goodsExchangePhase){ //if the player is in the goods exchange phase, it must be set to false
                goodsExchangePhase = false;
            }
            if(gameState.isLastInTurn(nickname)){ //if the player is the last in turn, the game state must be set to card picking
                gameState.setGameState(State.CARD_PICKING);
            }
        }
    }

    /**
     * This method allows a player to defeat the smugglers by using batteries.
     * @param gameState
     * @param nickname
     * @param usedBatteries
     * @param loseDays
     * @throws InvalidActionException
     */
    @Override
    public void defeat(GameState gameState, String nickname, int usedBatteries, boolean loseDays) throws InvalidActionException{
        if(isDefeated() || goodsExchangePhase){ //if the smugglers have already been defeated or the player is in the goods exchange phase
            throw new InvalidActionException("Invalid action");
        }
        if(gameState.getNumberBatteries(nickname) < usedBatteries) { //if the player doesn't have enough batteries
            throw new NoBatteriesException("Too few batteries");
        }
        double cannonStrength = gameState.getCannonStrength(nickname, usedBatteries); //calculate the cannon strength of the player
        if(cannonStrength>this.enemyStrength){      //defeated smugglers
            if(loseDays){       //the player enters the goods exchange phase and has to lose flight days accordingly
                this.defeated = true;
                this.goodsExchangePhase = true;
                gameState.changePlayerPosition(nickname, - this.getLostDays());
            }
            else{       //if the player doesn't want to lose days, the smugglers are defeated and a new card must be picked
                gameState.setGameState(State.CARD_PICKING);
                gameState.updateTurns();
            }
        }
        else if(cannonStrength== this.enemyStrength){       //draw; nothing happens to the player in turn but the smugglers are not defeated
            if(gameState.isLastInTurn(nickname)) {
                gameState.setGameState(State.CARD_PICKING);
            }
            gameState.nextTurn();
        }
        else{
            //if cannonStrength<this.enemyStrength, the smugglers have defeated the player, which loses
            //the most precious goods on its ship board
            gameState.losePreciousGoods(nickname, this.goodLoss);
            if(gameState.isLastInTurn(nickname)) {
                gameState.setGameState(State.CARD_PICKING);
            }
            gameState.nextTurn();
        }
    }

    @Override
    /**
     * Substitute the cargo goods (specified by coordinates of component) of the player with the station goods
     * @param gameState the current game state
     * @param nickname the nickname of the player who is loading goods
     * @param x the x coordinates of the cargo hold where the goods will be loaded
     * @param y the y coordinates of the cargo hold where the goods will be loaded
     * @throws InvalidActionException if the action is invalid (e.g. not in goods exchange phase)
     * @throws UnsupportedCargoColorException if the goods to be loaded are not supported by the cargo hold
     * @throws FullCargoHoldException if the cargo hold is full and cannot accommodate more goods
     * @throws NoGoodsException if the number of goods to be loaded does not match the number of specified coordinates
     *
     */
    public void loadGoods(GameState gameState, String nickname, List<Integer> x, List<Integer> y) throws InvalidActionException, UnsupportedCargoColorException, FullCargoHoldException, NoGoodsException {
        if (isDefeated() || !goodsExchangePhase) { //if the smugglers have already been defeated or the player is not in the goods exchange phase
            throw new InvalidActionException("Invalid action");
        }
        if(x.size() != prizeGoods.size() || y.size() != prizeGoods.size()){ //if the number of coordinates does not match the number of goods to be loaded
            throw new NoGoodsException("Specify where to put EACH prize good");
        }
        gameState.loadGoods(nickname, x, y, prizeGoods); //load the goods in the specified coordinates
        gameState.changePlayerPosition(nickname, -this.lostDays); //the player loses flight days for defeating the smugglers
        setDefeated(); //set the smugglers as defeated
        gameState.setGameState(State.CARD_PICKING); //set the game state to card picking
        gameState.updateTurns();
    }
}
