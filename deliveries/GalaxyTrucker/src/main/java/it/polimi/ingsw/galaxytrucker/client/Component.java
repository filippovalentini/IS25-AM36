package it.polimi.ingsw.galaxytrucker.client;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;

public class Component {
    private String imageID;
    private Orientation orientation;

    public Component(String imageID) {
        this.imageID = imageID;
        this.orientation = Orientation.NORTH;
    }

    public String getImageID() {
        return imageID;
    }
    public Orientation getOrientation() {
        return orientation;
    }
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }
    public void rotateLeft(){
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
}
