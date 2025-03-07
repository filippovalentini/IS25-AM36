package it.polimi.ingsw.galaxytrucker.model.shots;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;

public class Meteor {
    private final boolean isLarge;
    private final Orientation orientation;

    public Meteor(boolean isLarge, Orientation orientation) {
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
