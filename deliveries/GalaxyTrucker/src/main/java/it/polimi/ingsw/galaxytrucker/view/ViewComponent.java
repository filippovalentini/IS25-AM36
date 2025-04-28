package it.polimi.ingsw.galaxytrucker.view;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;

public class ViewComponent {
    private String imageID;
    private Orientation orientation;
    private int batteries;
    private int crew;
    private boolean purpleAlien;
    private boolean brownAlien;

    public ViewComponent(String imageID) {
        this.imageID = imageID;
        this.orientation = Orientation.NORTH;
        this.batteries = 0;
        this.crew = 0;
        this.purpleAlien = false;
        this.brownAlien = false;
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
    public void updateBatteries(int change){
        batteries += change;
    }
    public int getBatteries() {
        return batteries;
    }
    public void updateCrew(int change){
        crew += change;
    }
    public int getCrew() {
        return crew;
    }
    public void updateAlien(boolean isPurple){
        if(isPurple){
            purpleAlien = !purpleAlien;
        }
        else{
            brownAlien = !brownAlien;
        }
    }
    public boolean isPurpleAlien() {
        return purpleAlien;
    }
    public boolean isBrownAlien() {
        return brownAlien;
    }
}
