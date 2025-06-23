package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.List;
/**
 * This class represents an empty position on a ship board.
 * It is used to indicate free positions where no component is placed.
 */
//this class doesn't represent a real component, but is used to represent free positions on a ship board
public class Empty extends Component {
    /**
     * Constructor for the Empty class.
     * @param imageID the image ID of the empty position
     * @param sides the connectors of the empty position
     */
    public Empty(int imageID, List<Connector> sides) {
        super(imageID, sides);
    }
}
