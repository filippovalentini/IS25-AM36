package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.shots.Meteor;

import java.util.List;

public class MeteorsSwarm extends EventCard{
    private final List<Meteor> meteors;

    public MeteorsSwarm(List<Meteor> meteors) {
        this.meteors = meteors;
    }
    public List<Meteor> getMeteors() {
        return meteors;
    }
    @Override
    public void solve(){}       //implements the effect of the card
}
