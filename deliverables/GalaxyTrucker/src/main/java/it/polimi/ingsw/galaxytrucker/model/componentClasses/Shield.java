package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;

import java.util.ArrayList;
import java.util.List;
/**
 * This class represents a shield component in the game.
 * It extends the Component class and provides functionality to check if it protects against a given orientation.
 */
public class Shield extends Component {
    /**
     * Constructor for the Shield class.
     * @param imageID The ID of the image representing the shield.
     * @param sides A list of connectors representing the sides of the shield.
     */
    public Shield(int imageID, List<Connector> sides) {    //constructor
        super(imageID, sides);
    }

    /**
     * Checks if the shield protects against a given orientation.
     * @param o The orientation to check protection against.
     * @return true if the shield protects against the given orientation, false otherwise.
     */
    @Override
    public boolean protects(Orientation o) {
        if(orientation == o){ //if the orientation of the shield is the same as the one we are checking, it protects
            return true;
        }
        if(orientation == Orientation.NORTH && o == Orientation.EAST){
            return true;
        }
        if(orientation == Orientation.EAST && o == Orientation.SOUTH){
            return true;
        }
        if(orientation == Orientation.SOUTH && o == Orientation.WEST){
            return true;
        }
        if(orientation == Orientation.WEST && o == Orientation.NORTH){
            return true;
        }
        return false;
    }
    /**
     * Clones the Shield component, creating a new instance with the same properties.
     * @return A new Shield instance with the same imageID, sides, and orientation.
     */
    @Override
    public Component clone(){//return a copy of the component
        Shield retComponent = new Shield(this.imageID, new ArrayList<>(this.sides)); //create a new Shield with the same imageID and sides
        retComponent.orientation = this.orientation; //set the orientation of the new Shield to the same as the original
        return retComponent;
    }
}
