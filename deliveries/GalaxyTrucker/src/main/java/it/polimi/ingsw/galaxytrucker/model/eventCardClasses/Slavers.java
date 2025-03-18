package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

//SLAVERS
public class Slavers extends AdvancedEnemies{
    private final int crewLoss;     //number of crew members that a ship board can lose if the card has effect on the corresponding player

    public Slavers(int prizeCredits, int enemyStrength, int crewLoss, int lostDays, int imageID) {
        super(prizeCredits, enemyStrength, lostDays, imageID);
        this.crewLoss = crewLoss;
    }

    @Override
    public void defeat(GameState gameState, String nickname, int usedBatteries, boolean loseDays) throws InvalidActionException{}
}
