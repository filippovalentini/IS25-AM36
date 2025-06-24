package it.polimi.ingsw.galaxytrucker.model.shotClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;

/**
 * this class describes the size and orientation of a cannon shot that can hit the ships during the game
 */
public class CannonShot {
    private final boolean isLarge;      //size of the cannon shot
    private final Orientation orientation;      //direction of the cannon shot

    /**
     * Constructor for CannonShot
     * @param isLarge
     * @param orientation
     */
    public CannonShot(boolean isLarge, Orientation orientation) {       //constructor
        this.isLarge = isLarge;
        this.orientation = orientation;
    }

    /**
     * Method to check if the cannon shot is large
     * @return true if the cannon shot is large, false otherwise
     */
    public boolean isLarge() {
        return isLarge;
    }

    /**
     * Method to get the orientation of the cannon shot
     * @return the orientation of the cannon shot
     */
    public Orientation getOrientation() {
        return orientation;
    }
}
