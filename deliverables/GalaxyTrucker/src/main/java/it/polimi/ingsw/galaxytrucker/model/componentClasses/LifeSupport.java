package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a life support component in the game.
 */

public class LifeSupport extends Component {
    private final boolean isPurple;     //life supports for aliens can be purple or brown

    /**
     * Constructor for the LifeSupport class.
     * @param isPurple
     * @param imageID
     * @param sides
     */
    public LifeSupport(boolean isPurple, int imageID, List<Connector> sides) {     //constructor
        super(imageID, sides);
        this.isPurple = isPurple;
    }
    /**
     * Checks if the life support is purple.
     * @return true if the life support is purple, false otherwise.
     */
    public boolean isPurple() {
        return isPurple;    //returns true if the life support is purple, false otherwise
    }

    /**
     * Clones the LifeSupport component.
     * @return a new instance of LifeSupport with the same properties.
     */
    @Override
    public Component clone() {//return a copy of the component
        LifeSupport retComponent = new LifeSupport(isPurple, this.imageID, new ArrayList<>(this.sides)); //creates a new LifeSupport component with the same properties
        retComponent.orientation = this.orientation; //copies the orientation from the original component
        return retComponent; //returns the new component
    }

    /**
     * Checks if the life support supports a specific type of alien.
     * @param purpleAlien
     * @return true if the life support supports the specified type of alien, false otherwise.
     */
    @Override
    public boolean supportsAlien(boolean purpleAlien) {
        return this.isPurple == purpleAlien;
    }
}
