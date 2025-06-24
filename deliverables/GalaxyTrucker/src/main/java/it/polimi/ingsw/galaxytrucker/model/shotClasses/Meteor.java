package it.polimi.ingsw.galaxytrucker.model.shotClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;

/**
 * this class describes the size and orientation of a meteor that can hit the ships during the game
 */
public class Meteor {
    private final boolean isLarge;      //size of the meteor
    private final Orientation orientation;      //direction of the meteor

    /**
     * Constructor for the Meteor class
     * @param isLarge
     * @param orientation
     */
    public Meteor(boolean isLarge, Orientation orientation) {       //constructor
        this.isLarge = isLarge;
        this.orientation = orientation;
    }

    /**
     * Method to check if the meteor is large
     * @return true if the meteor is large, false otherwise
     */
    public boolean isLarge() {
        return isLarge;
    }

    /**
     * Method to get the orientation of the meteor
     * @return the orientation of the meteor
     */
    public Orientation getOrientation() {
        return orientation;
    }
}
