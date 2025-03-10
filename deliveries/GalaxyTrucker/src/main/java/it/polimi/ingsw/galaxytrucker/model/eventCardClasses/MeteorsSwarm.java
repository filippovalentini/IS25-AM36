package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.Meteor;

import java.util.List;

public class MeteorsSwarm extends EventCard{
    private final List<Meteor> meteors;

    public MeteorsSwarm(List<Meteor> meteors, String imagePath) {
        super(imagePath);
        this.meteors = meteors;
    }
    public List<Meteor> getMeteors() {
        return meteors;
    }
    @Override
    public void solve(GameState gameState){}       //implements the effect of the card
}
