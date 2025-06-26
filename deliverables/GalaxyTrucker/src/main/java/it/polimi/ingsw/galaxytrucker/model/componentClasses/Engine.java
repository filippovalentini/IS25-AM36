package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an engine component in the game.
 */
public class Engine extends ConfigurableComponent {
    /**
     * Constructor for the Engine class.
     * @param isDouble
     * @param imageID
     * @param sides
     */
    public Engine(boolean isDouble, int imageID, List<Connector> sides) {      //constructor
        super(isDouble, imageID, sides);
    }

    /**
     * Checks if the engine is well oriented.
     * @return true if the engine is oriented north, false otherwise.
     */
    @Override
    public boolean isWellOriented(){        //the third side of engine components must be south-oriented (therefore, the side in position zero must be north-oriented)
        return orientation == Orientation.NORTH;
    }

    /**
     * Checks if the engine has double engines.
     * @return true if the engine is double, false otherwise.
     */
    @Override
    public boolean hasDoubleEngines() {
        return isDouble();
    }

    /**
     * Checks if the engine has a single engine.
     * @return true if the engine is single, false otherwise.
     */
    @Override
    public boolean hasSingleEngine() {
        return !isDouble();
    }

    /**
     * Clones the Engine component, creating a new instance with the same properties.
     * @return a new Engine instance with the same properties as the original.
     */
    @Override
    public Component clone(){//return a copy of the component
        Engine retComponent = new Engine(isDouble,this.imageID, new ArrayList<>(this.sides)); // create a new Engine instance
        retComponent.orientation = this.orientation; // copy the orientation from the original component
        return retComponent;
    }

    /**
     * Checks if the engine has an adjacent placement conflict.
     * @param cNorth the component to the north of this component
     * @param cEast the component to the east of this component
     * @param cSouth the component to the south of this component
     * @param cWest the component to the west of this component
     * @return true if there is an adjacent placement conflict, false otherwise
     */
    @Override
    public boolean hasAdjacentPlacementConflict(Component cNorth, Component cEast, Component cSouth, Component cWest){
        return cSouth != null && cSouth.isNotEmpty() && cSouth.belongsToShip();
    }
}
