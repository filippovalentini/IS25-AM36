package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.List;
/**
 * This class represents a space on the ship board where no components can be assembled.
 * It is used to indicate positions that are not occupied by any other component.
 */
public class Space extends Component {
    /**
     * Constructor for the Space class.
     * @param imageID The image ID representing the space.
     * @param sides A list of connectors indicating the sides of the space.
     */
    public Space(int imageID, List<Connector> sides) {        //constructor
        super(imageID, sides);
    }
}
