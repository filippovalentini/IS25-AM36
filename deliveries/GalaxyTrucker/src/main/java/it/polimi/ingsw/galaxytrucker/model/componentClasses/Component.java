package it.polimi.ingsw.galaxytrucker.model.componentClasses;


import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;

import java.util.List;

public class Component {
    protected final String imagePath;
    protected List<Connector> sides;
    protected Orientation orientation;

    public Component(String imagePath, List<Connector> sides) {
        this.imagePath = imagePath;
        this.sides = sides;
        this.orientation = Orientation.NORTH;
    }
    public Orientation getOrientation() {
        return orientation;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void rotateRight() {
        if(orientation == Orientation.NORTH) {
            orientation = Orientation.EAST;
        }
        else if(orientation == Orientation.EAST) {
            orientation = Orientation.SOUTH;
        }
        else if(orientation == Orientation.SOUTH) {
            orientation = Orientation.WEST;
        }
        else {
            orientation = Orientation.NORTH;
        }
    }
    public void rotateLeft() {
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
    public Connector getNorthSide(){
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
    public Connector getEastSide(){
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
    public Connector getSouthSide(){
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
            return sides.get(0);
        }
    }
    public boolean isWellOriented(){
        return true;
    }
}
