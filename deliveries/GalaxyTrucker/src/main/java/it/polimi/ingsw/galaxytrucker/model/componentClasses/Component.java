package it.polimi.ingsw.galaxytrucker.model.componentClasses;


import it.polimi.ingsw.galaxytrucker.model.enumerations.*;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoBatteriesException;

import java.util.ArrayList;
import java.util.*;

//this class describes a generic component used to assemble the players' ships. Each subclass of "Component"
//represents instead a specific type of component
public class Component {
    protected final int imageID;       //path for the image describing the component
    protected List<Connector> sides;        //list of 4 connectors associated to the 4 sides of the component tile
    protected Orientation orientation;      //orientation of the connector in position 0 of "sides"

    public Component(int imageID, List<Connector> sides) {     //connector
        this.imageID = imageID;
        this.sides = sides;
        this.orientation = Orientation.NORTH;
    }
    public Component clone(){ //return a copy of the component
        Component retComponent = new Component(this.imageID, new ArrayList<>(this.sides));
        retComponent.orientation = this.orientation;
        return retComponent;
    }
    public Orientation getOrientation() {
        return orientation;
    }
    public int getImageID() {
        return imageID;
    }

    @Override
    public boolean equals(Object component) {
        if(this == component) return true;
        if(component == null || getClass() != component.getClass()) return false;
        return this.imageID == ((Component) component).getImageID();
    }

    public void rotateLeft() {      //rotates the component (left) by 90 degrees
        if(orientation == Orientation.NORTH) {
            orientation = Orientation.WEST;
        }
        else if(orientation == Orientation.WEST) {
            orientation = Orientation.SOUTH;
        }
        else if(orientation == Orientation.SOUTH) {
            orientation = Orientation.EAST;
        }
        else {
            orientation = Orientation.NORTH;
        }
    }
    public Connector getNorthSide(){        //returns the north-oriented side (connector) of the component
        if(orientation == Orientation.NORTH) {
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
    public Connector getEastSide(){     //returns the east-oriented side (connector) of the component
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
    public Connector getSouthSide(){        //returns the south-oriented side (connector) of the component
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
    public Connector getWestSide(){     //returns the south-oriented side (connector) of the component
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

    public boolean isWellOriented(){        //determines if the component is oriented correctly (true by default, it will be overrided by subclasses for which the orientation is important)
        return true;
    }
    public boolean hasMembers(){ return false;}
    public boolean hasDoubleEngines() { return false;}
    public boolean hasSingleEngine() { return false;}
    public boolean hasDoubleCannons() { return false;}
    public boolean hasSingleCannon() { return false;}
    public void removeMember(){}
    public int getNumberCrew() {
        return 0;
    }
    public int getNumberBatteries(){ return 0;}
    public void useBatteries(int batteriesToUse) throws NoBatteriesException{}
    public boolean protects(Orientation orientation){ return false;}
    public boolean isNotEmpty(){
        Component empty = new Empty(0, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH)));
        return !this.equals(empty);
    }
    public boolean belongsToShip(){
        Component space = new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH)));
        return !this.equals(space);
    }
}
