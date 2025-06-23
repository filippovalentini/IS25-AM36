package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents a structural component.
 * This class extends the Component class and is used to create structural components
 * with specific image IDs and connector sides.
 */
public class Structural extends Component {
    /**
     * Constructor for the Structural class.
     *
     * @param imageID The ID of the image representing this structural component.
     * @param sides   A list of connectors.
     */
    public Structural(int imageID, List<Connector> sides) {        //constructor
        super(imageID, sides);
    }

    /**
     * Creates a copy of the Structural component.
     * @return A new Structural component that is a clone of this one.
     */
    @Override
    public Component clone() {//return a copy of the component
        Structural retComponent = new Structural(this.imageID, new ArrayList<>(this.sides)); // create a new Structural component with the same imageID and sides
        retComponent.orientation = this.orientation; // copy the orientation
        return retComponent;
    }
}