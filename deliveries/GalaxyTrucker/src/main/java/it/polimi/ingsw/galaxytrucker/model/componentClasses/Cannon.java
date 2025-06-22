package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;

import java.util.ArrayList;
import java.util.List;
/**
 * Cannon class represents a cannon component in the game.
 */
public class Cannon extends ConfigurableComponent { //constructor
    /**
     * Constructor for Cannon class.
     * @param isDouble true if the cannon is double, false otherwise
     * @param imageID the image ID of the cannon
     * @param sides the connectors of the cannon
     */
    public Cannon(boolean isDouble, int imageID, List<Connector> sides) {
        super(isDouble, imageID, sides);
    }

    /**
     * Checks if the cannon has double cannons.
     * @return true if the cannon has double cannons, false otherwise
     */
    @Override
    public boolean hasDoubleCannons() { return isDouble();}

    /**
     * Returns the sides of the cannon.
     * @return the list of connectors of the cannon
     */
    public List<Connector> getSides() { return sides; } //getter for sides

    /**
     * Checks if the cannon has a single cannon.
     * @return true if the cannon has a single cannon, false otherwise
     */
    @Override
    public boolean hasSingleCannon() { return !isDouble();}

    /**
     * Checks if the cannon points forward based on its orientation.
     * @return true if the cannon points forward, false otherwise
     */
    @Override
    public boolean pointsForward(){
        return this.orientation == Orientation.NORTH;
    }

    /**
     * Clones the cannon component.
     * @return a new Cannon component with the same properties
     */
    @Override
    public Component clone(){ //return a copy of the component
        Cannon retComponent = new Cannon(isDouble,this.imageID, new ArrayList<>(this.sides));
        retComponent.orientation = this.orientation;
        return retComponent;
    }

    /**
     * Checks if the cannon has an adjacent placement conflict.
     * @param cNorth the component to the north of this component
     * @param cEast the component to the east of this component
     * @param cSouth the component to the south of this component
     * @param cWest the component to the west of this component
     * @return true if there is an adjacent placement conflict, false otherwise
     */
    @Override //return if violates the rule constraint of empty component over the cannon
    public boolean hasAdjacentPlacementConflict(Component cNorth, Component cEast, Component cSouth, Component cWest){
        if(orientation == Orientation.NORTH && (cNorth == null || !cNorth.isNotEmpty() || cNorth.isSpace())){
            return false;
        }else if(orientation == Orientation.EAST && (cEast == null || !cEast.isNotEmpty() || cEast.isSpace())){
            return false;
        }else if(orientation == Orientation.SOUTH && (cSouth == null || !cSouth.isNotEmpty() || cSouth.isSpace())){
            return false;
        }else if(orientation == Orientation.WEST && (cWest == null || !cWest.isNotEmpty() || cWest.isSpace())){
            return false;
        }else{
            return true;
        }
    }
}
