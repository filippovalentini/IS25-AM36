package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoBatteriesException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.Meteor;

import java.util.List;
//METEOR SWARM
public class MeteorsSwarm extends EventCard{
    private final List<Meteor> meteors;     //meteors that compose the swarm
    private int currentMeteor;          //position in list of the next meteor that will hit a player's ship

    public MeteorsSwarm(List<Meteor> meteors, int imageID) {       //constructor
        super(imageID);
        this.meteors = meteors;
        this.currentMeteor = 0;
    }
    public List<Meteor> getMeteors() {
        return meteors;
    }

  /*  @Override
    public void hitShip(GameState gameState, String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws InvalidActionException, NoBatteriesException {
        if((activateCannon || activateShield) && gameState.getNumberBatteries(nickname) == 0){
            throw new InvalidActionException("Too few batteries");
        }
        Orientation orientation = meteors.get(currentMeteor).getOrientation();
        int direction = (orientation.isVertical() ? diceResult-4 : diceResult-5);
        gameState.meteorAttack(nickname, meteors.get(currentMeteor), direction, activateShield, activateCannon);
        if(currentMeteor == meteors.size() - 1){
            if(gameState.isLastInTurn(nickname)) {
                gameState.setGameState(State.CARD_PICKING);
            }
            currentMeteor=0;
            gameState.nextTurn();
        }
        else{
            currentMeteor++;
        }
    }

   */

    @Override
    public void hitShip(GameState gameState, String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws InvalidActionException, NoBatteriesException {
        if ((activateCannon || activateShield) && gameState.getNumberBatteries(nickname) == 0) {
            throw new InvalidActionException("Too few batteries");
        }

        Orientation orientation = meteors.get(currentMeteor).getOrientation();
        int direction = (orientation.isVertical() ? diceResult - 4 : diceResult - 5);
        gameState.meteorAttack(nickname, meteors.get(currentMeteor), direction, activateShield, activateCannon);

        if (currentMeteor == meteors.size() - 1) {
            // If the crewMember are 0
            if (gameState.getCrewCount(nickname)==0) {
                System.out.println("You lost all your crew member.");
                gameState.quitGame(nickname);
            } else {
                // Ship is intact, proceed normally
                if (gameState.isLastInTurn(nickname)) {
                    gameState.setGameState(State.CARD_PICKING);
                }
                currentMeteor = 0;
                gameState.nextTurn();
            }
        } else {
            currentMeteor++;
        }
    }

}
