package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

import java.util.List;

//SMUGGLERS
public class Smugglers extends DayLossCard{
    private final List<Color> prizeGoods;       //goods that a player gains by defeating the smugglers
    private final int goodLoss;     //goods that a player loses if defeated by the smugglers
    private final int enemyStrength;        //strength required to defeat the smugglers
    private boolean defeated;       //set to true if a player has defeated the smugglers
    private boolean goodsExchangePhase;

    public Smugglers(List<Color> prizeGoods, int goodLoss, int enemyStrength, int lostDays, int imageID) {  //constructor
        super(lostDays, imageID);
        this.prizeGoods = prizeGoods;
        this.goodLoss = goodLoss;
        this.enemyStrength = enemyStrength;
        this.defeated = false;
        this.goodsExchangePhase = false;
    }

    public boolean isDefeated() {   //determines if a player has defeated the smugglers or not
        return defeated;
    }
    public void setDefeated() {      //invoked when a player defeats the smugglers
        defeated = true;
    }

    @Override
    public void defeat(GameState gameState, String nickname, int usedBatteries, boolean loseDays) throws InvalidActionException{
        if(isDefeated() || goodsExchangePhase){
            throw new InvalidActionException("Invalid action");
        }
        if(gameState.getNumberBatteries(nickname) < usedBatteries) {
            throw new NoBatteriesException("Too few batteries");
        }
        double cannonStrength = gameState.getCannonStrength(nickname, usedBatteries);
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
    //substitute the cargo goods (specified by coordinates of component) of the player with the station goods
    public void loadGoods(GameState gameState, String nickname, List<Integer> x, List<Integer> y) throws InvalidActionException, UnsupportedCargoColorException, FullCargoHoldException, NoGoodsException {
        if (isDefeated() || !goodsExchangePhase) {
            throw new InvalidActionException("Invalid action");
        }
        if(x.size() != prizeGoods.size() || y.size() != prizeGoods.size()){
            throw new NoGoodsException("Specify where to put EACH prize good");
        }
        gameState.loadGoods(nickname, x, y, prizeGoods);
        gameState.changePlayerPosition(nickname, -this.lostDays);
        setDefeated();
        gameState.setGameState(State.CARD_PICKING);
        gameState.updateTurns();
    }
}
