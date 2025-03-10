package it.polimi.ingsw.galaxytrucker.model.shotClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
//this class describes the size and orientation of a meteor that can hit the ships during the game
public class Meteor {
    private final boolean isLarge;      //size of the meteor
    private final Orientation orientation;      //direction of the meteor

    public Meteor(boolean isLarge, Orientation orientation) {       //constructor
        this.isLarge = isLarge;
        this.orientation = orientation;
    }
    public boolean isLarge() {
        return isLarge;
    }
    public Orientation getOrientation() {
        return orientation;
    }
}
