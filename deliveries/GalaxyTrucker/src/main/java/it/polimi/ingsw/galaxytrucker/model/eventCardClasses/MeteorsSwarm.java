package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.Meteor;

import java.util.List;
//METEOR SWARM
public class MeteorsSwarm extends EventCard{
    private final List<Meteor> meteors;
    private int currentMeteor;

    public MeteorsSwarm(List<Meteor> meteors, int imageID) {       //constructor
        super(imageID);
        this.meteors = meteors;
        this.currentMeteor = 0;
    }
    public List<Meteor> getMeteors() {
        return meteors;
    }

    @Override
    public void hitShip(GameState gameState, String nickname, int diceResult, boolean activateShield, boolean activateCannon) throws InvalidActionException{}

}
