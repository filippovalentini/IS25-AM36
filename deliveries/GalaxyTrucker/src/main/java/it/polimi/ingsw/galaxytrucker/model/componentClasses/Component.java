package it.polimi.ingsw.galaxytrucker.model.componentClasses;


import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;

import java.util.ArrayList;
import java.util.List;

//this class describes a generic component used to assemble the players' ships. Each subclass of "Component"
//represents instead a specific type of component
public class Component {
    protected final String imagePath;       //path for the image describing the component
    protected List<Connector> sides;        //list of 4 connectors associated to the 4 sides of the component tile
    protected Orientation orientation;      //orientation of the connector in position 0 of "sides"

    public Component(String imagePath, List<Connector> sides) {     //connector
        this.imagePath = imagePath;
        this.sides = sides;
        this.orientation = Orientation.NORTH;
    }
    public Component clone(){ //return a copy of the component
        Component retComponent = new Component(this.imagePath, new ArrayList<>(this.sides));
        retComponent.orientation = this.orientation;
        return retComponent;
    }
    public Orientation getOrientation() {
        return orientation;
    }
    public String getImagePath() {
        return imagePath;
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
            return sides.get(0);
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
            return sides.get(0);
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
            return sides.get(0);
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
            return sides.get(0);
        }
    }
    public boolean isWellOriented(){        //determines if the component is oriented correctly (true by default, it will be overrided by subclasses for which the orientation is important)
        return true;
    }
}
