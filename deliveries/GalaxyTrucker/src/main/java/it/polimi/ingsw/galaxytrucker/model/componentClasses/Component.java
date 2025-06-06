package it.polimi.ingsw.galaxytrucker.model.componentClasses;


import it.polimi.ingsw.galaxytrucker.model.enumerations.*;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;

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
    public void setOrientation() {
        this.orientation = Orientation.NORTH;
    }
    public int getImageID() {
        return imageID;
    }

    @Override
    public boolean equals(Object component) {
        if(this == component) return true;
        if(component == null) return false;
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

    public boolean isNotEmpty(){
        Component empty = new Empty(0, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH)));
        return !this.equals(empty);
    }
    public boolean belongsToShip(){
        Component space = new Space(3, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH)));
        return !this.equals(space);
    }

    public boolean isWellOriented(){        //determines if the component is oriented correctly (true by default, it will be overrided by subclasses for which the orientation is important)
        return true;
    }
    public void addGood(Color good) throws FullCargoHoldException, UnsupportedCargoColorException, AssembledComponentException {
        throw new AssembledComponentException("Can't add a good outside a cargo hold");
    }
    public boolean hasMembers(){ return false;}
    public boolean hasDoubleEngines() { return false;}
    public boolean hasSingleEngine() { return false;}
    public boolean hasDoubleCannons() { return false;}
    public boolean hasSingleCannon() { return false;}
    public void addCrew() throws FullCabinException, AssembledComponentException {
        throw new AssembledComponentException("Can't add crew outside a cabin");
    }
    public void addAlien(boolean isPurple) throws FullCabinException, AssembledComponentException {
        throw new AssembledComponentException("Can't add an alien outside a cabin");
    }
    public int addBatteries() throws NoBatteriesException, AssembledComponentException {
        throw new AssembledComponentException("Can't add batteries outside a battery component");
    }
    public void removeMember(){}
    public void removeCrew(int i) throws NoCrewException {
        throw new NoCrewException("Can't remove crew from outside a cabin");
    }
    public int getNumberCrew() {
        return 0;
    }
    public List<Color> getGoods(){ return new ArrayList<>();}
    public int getNumberBatteries(){ return 0;}
    public int getNumberGoods() { return 0;}
    public int getNumberGoods(Color color){ return 0;}
    public boolean isFullOfGoods() { return true;}
    public void removeSpecificGoods(Color color, int numberGoods){}
    public void substituteGood(Color good, int position) throws FullCargoHoldException, UnsupportedCargoColorException, AssembledComponentException {
        throw new AssembledComponentException("Can't substitute good outside a cargo hold");
    }
    public void useBatteries(int batteriesToUse) throws NoBatteriesException{}
    public boolean protects(Orientation orientation){ return false;}
    public boolean pointsForward(){ return false;}
    public int goodsPrice(){ return 0;}
    public boolean isFull(){ return true;}
    public boolean supportsAlien(boolean purpleAlien){ return false;}

}
