package it.polimi.ingsw.galaxytrucker.model.componentClasses;


import it.polimi.ingsw.galaxytrucker.model.enumerations.*;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;

import java.util.ArrayList;
import java.util.*;

/**
 * This class represents a generic component used to assemble the players' ships.
 * This class describes a generic component used to assemble the players' ships. Each subclass of "Component" represents instead a specific type of component
 */


public class Component {
    protected final int imageID;       //path for the image describing the component
    protected List<Connector> sides;        //list of 4 connectors associated to the 4 sides of the component tile
    protected Orientation orientation;      //orientation of the connector in position 0 of "sides"

    /**
     * Constructor for the Component class.
     * @param imageID
     * @param sides
     */
    public Component(int imageID, List<Connector> sides) {     //connector
        this.imageID = imageID; //image ID of the component
        this.sides = sides; //list of connectors associated to the 4 sides of the component
        this.orientation = Orientation.NORTH; //default orientation
    }
    /**
     * Copy constructor for the Component class.
     */
    public Component clone(){ //return a copy of the component
        Component retComponent = new Component(this.imageID, new ArrayList<>(this.sides)); //creates a new component with the same imageID and sides
        retComponent.orientation = this.orientation; //sets the orientation of the new component to the same as the original
        return retComponent;
    }
    /**
     * Returns the orientation associated to  the component.
     * @return the orientation of the component
     */
    public Orientation getOrientation() {
        return orientation;
    }
    /**
     * Sets the orientation of the component to NORTH.
     */
    public void setOrientation() {
        this.orientation = Orientation.NORTH;
    }
    /**
     * Returns the imageID of the component.
     * @return imageID of the component
     */
    public int getImageID() {
        return imageID;
    }

    /**
     * Returns a boolean indicating whether the component is equal to this component.
     * @param component
     * @return true if the component is equal to this component, false otherwise
     */
    @Override
    public boolean equals(Object component) {
        if(this == component) return true; //if the two components are the same object, they are equal
        if(component == null) return false; //if the component is null, it is not equal to this component
        return this.imageID == ((Component) component).getImageID(); //if the imageID of the two components is the same, they are equal
    }
    /**
     * Rotates the component left by 90 degrees.
     *
     */
    public void rotateLeft() {
        if(orientation == Orientation.NORTH) { //if the orientation is NORTH, it becomes WEST
            orientation = Orientation.WEST;
        }
        else if(orientation == Orientation.WEST) { //if the orientation is WEST, it becomes SOUTH
            orientation = Orientation.SOUTH;
        }
        else if(orientation == Orientation.SOUTH) { //if the orientation is SOUTH, it becomes EAST
            orientation = Orientation.EAST;
        }
        else { //if the orientation is EAST, it becomes NORTH
            orientation = Orientation.NORTH;
        }
    }
    /**
     * Returns the north-oriented side (connector) of the component
     *
     */
    public Connector getNorthSide(){
        if(orientation == Orientation.NORTH) { //returns the first connector in the list of sides
            return sides.getFirst();
        }
        else if(orientation == Orientation.EAST) {
            return sides.get(3);
        }
        else if(orientation == Orientation.SOUTH) {
            return sides.get(2);
        }
        else {
            return sides.get(1);
        }
    }
    /**
     * Returns the east-oriented side (connector) of the component
     *
     */
    public Connector getEastSide(){
        if(orientation == Orientation.NORTH) {
            return sides.get(1);
        }
        else if(orientation == Orientation.EAST) {
            return sides.getFirst();
        }
        else if(orientation == Orientation.SOUTH) {
            return sides.get(3);
        }
        else {
            return sides.get(2);
        }
    }
    /**
     * Returns the south-oriented side (connector) of the component
     *
     */
    public Connector getSouthSide(){
        if(orientation == Orientation.NORTH) {
            return sides.get(2);
        }
        else if(orientation == Orientation.EAST) {
            return sides.get(1);
        }
        else if(orientation == Orientation.SOUTH) {
            return sides.getFirst();
        }
        else {
            return sides.get(3);
        }
    }
    /**
     * Returns the west-oriented side (connector) of the component
     *
     */
    public Connector getWestSide(){
        if(orientation == Orientation.NORTH) {
            return sides.get(3);
        }
        else if(orientation == Orientation.EAST) {
            return sides.get(2);
        }
        else if(orientation == Orientation.SOUTH) {
            return sides.get(1);
        }
        else {
            return sides.getFirst();
        }
    }

    /**
     * Returns a boolean indicating whether the component is empty or not.
     * @return true if the component is not empty, false otherwise
     */
    public boolean isNotEmpty(){
        Component empty = new Empty(0, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH)));
        return !this.equals(empty);
    }
    /**
     * Returns a boolean indicating whether the component is a space or not.
     * @return true if the component is a space, false otherwise
     */
    public boolean isSpace(){
        Component space = new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH)));
        return this.equals(space);
    }
    /**
     * Returns a boolean indicating whether the component belongs to a ship or not.
     * @return true if the component belongs to a ship, false otherwise
     */
    public boolean belongsToShip(){
        Component space = new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH)));
        return !this.equals(space);
    }
    /**
     * Determines if the component has placement conflict given from adjacent components provided as method params.
     * @param cNorth the component to the north of this component
     *@param cEast the component to the east of this component
     *@param cSouth the component to the south of this component
     *@param cWest the component to the west of this component
     *@return true if there is a placement conflict, false otherwise
     */
    public boolean hasAdjacentPlacementConflict(Component cNorth, Component cEast, Component cSouth, Component cWest){
        return false;
    }
    /**
     * Returns a boolean indicating whether the component is well oriented or not.
     * @return true if the component is well oriented, false otherwise
     */
    public boolean isWellOriented(){
        return true;
    }

    /**
     * Adds a good to the cargo hold of the component.
     * @param good
     * @throws FullCargoHoldException
     * @throws UnsupportedCargoColorException
     * @throws AssembledComponentException
     */
    public void addGood(Color good) throws FullCargoHoldException, UnsupportedCargoColorException, AssembledComponentException {
        throw new AssembledComponentException("Can't add a good outside a cargo hold");
    }
    /**
     * Returns a boolean indicating whether the component has members or not.
     * @return true if the component has members, false otherwise
     */
    public boolean hasMembers(){ return false;}
    /**
     * Returns a boolean indicating whether the component has a double engine or not.
     * @return true if the component has a double engine, false otherwise
     */
    public boolean hasDoubleEngines() { return false;}
    /**
     * Returns a boolean indicating whether the component has a single engine or not.
     * @return true if the component has a single engine, false otherwise
     */
    public boolean hasSingleEngine() { return false;}
    /**
     * Returns a boolean indicating whether the component has double cannons or not.
     * @return true if the component has double cannons, false otherwise
     */
    public boolean hasDoubleCannons() { return false;}
    /**
     * Returns a boolean indicating whether the component has a single cannon or not.
     * @return true if the component has a single cannon, false otherwise
     */
    public boolean hasSingleCannon() { return false;}

    /**
     * Adds a crew member to the component.
     * @throws FullCabinException
     * @throws AssembledComponentException
     */
    public void addCrew() throws FullCabinException, AssembledComponentException {
        throw new AssembledComponentException("Can't add crew outside a cabin");
    }
/**
     * Adds an alien to the component.
     * @param isPurple
     * @throws FullCabinException
     * @throws AssembledComponentException
     */
    public void addAlien(boolean isPurple) throws FullCabinException, AssembledComponentException {
        throw new AssembledComponentException("Can't add an alien outside a cabin");
    }
/**
     * Adds batteries to the component.
     * @return the number of batteries added
     * @throws NoBatteriesException
     * @throws AssembledComponentException
     */
    public int addBatteries() throws NoBatteriesException, AssembledComponentException {
        throw new AssembledComponentException("Can't add batteries outside a battery component");
    }
    /**
     * Removes a member from the component.
     *
     */
    public void removeMember(){}
    /**
     * Removes a crew member from the component.
     * @param i
     * @throws NoCrewException
     */
    public void removeCrew(int i) throws NoCrewException {
        throw new NoCrewException("Can't remove crew from outside a cabin");
    }

    /**
     * Returns the number of crew members in the component.
     * @return 0
     */
    public int getNumberCrew() {
        return 0;
    }

    /**
     * Returns the list of goods.
     * @return an empty list
     */
    public List<Color> getGoods(){ return new ArrayList<>();}
    /**
     * Returns the number of batteries in the component.
     * @return 0
     */
    public int getNumberBatteries(){ return 0;}
    /**
     * Returns the number of aliens in the component.
     * @return 0
     */
    public int getNumberGoods() { return 0;}
    /**
     * Returns the number of goods of a specific color in the component.
     * @param color
     * @return 0
     */
    public int getNumberGoods(Color color){ return 0;}
    /**
     * Returns a boolean indicating whether the component is full of goods or not.
     * @return true
     */
    public boolean isFullOfGoods() { return true;}
    /**
     * Removes a specific number of goods of a specific color from the component.
     * @param color
     * @param numberGoods
     */
    public void removeSpecificGoods(Color color, int numberGoods){}
    /**
     * Substitutes a good in the component at a specific position.
     * @param good
     * @param position
     * @throws FullCargoHoldException
     * @throws UnsupportedCargoColorException
     * @throws AssembledComponentException
     */
    public void substituteGood(Color good, int position) throws FullCargoHoldException, UnsupportedCargoColorException, AssembledComponentException {
        throw new AssembledComponentException("Can't substitute good outside a cargo hold");
    }
    /**
     * Uses a specific number of batteries in the component.
     * @param batteriesToUse
     * @throws NoBatteriesException
     */
    public void useBatteries(int batteriesToUse) throws NoBatteriesException{}
    /**
     * Returns a boolean indicating whether the component protects from a specific orientation or not.
     * @param orientation
     * @return false
     */
    public boolean protects(Orientation orientation){ return false;}
    /**
     * Returns a boolean indicating whether the component points forward or not.
     * @return false
     */
    public boolean pointsForward(){ return false;}
    /**
     * Returns the price of the goods in the component.
     * @return 0
     */
    public int goodsPrice(){ return 0;}
    /**
     * Returns a boolean indicating whether the component is full or not.
     * @return true
     */
    public boolean isFull(){ return true;}
    /**
     * Returns a boolean indicating whether the component supports an alien or not.
     * @param purpleAlien
     * @return false
     */
    public boolean supportsAlien(boolean purpleAlien){ return false;}
    /**
     * Returns a boolean indicating whether the component has an alien or not.
     * @param isPurple
     * @return false
     */
    public boolean hasAlien(boolean isPurple){ return false;}

}
