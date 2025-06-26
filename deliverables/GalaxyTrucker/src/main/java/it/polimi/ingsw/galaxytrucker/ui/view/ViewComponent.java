package it.polimi.ingsw.galaxytrucker.ui.view;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;

import java.util.ArrayList;
import java.util.List;


/**
 * ViewComponent class represents a simplified version (client-side) of the Component class (server-side)
 */
public class ViewComponent {
    private final String imageID;     //game ID of the reference game
    private Orientation orientation;    //orientation of the component
    private int batteries;      //number of batteries contained
    private int crew;       //number of crew members contained
    private boolean purpleAlien;      //true if the component contains a purple alien
    private boolean brownAlien;     //true if the component contains a brown alien
    private List<Color> goods;      //list of goods contained
    /**
     * Constructor for a ViewComponent.
     * @param imageID the identifier of the component's image
     */
    public ViewComponent(String imageID) {
        this.imageID = imageID;
        this.orientation = Orientation.NORTH;
        this.batteries = 0;
        this.crew = 0;
        this.purpleAlien = false;
        this.brownAlien = false;
        this.goods = new ArrayList<>();
    }

    /**
     * Returns the identifier of the component's image.
     * @return the identifier of the component's image
     */
    public String getImageID() {
        return imageID;
    }

    /**
     * Returns the sides of the component.
     * @return the sides of the component
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Sets the orientation of the component.
     * @param orientation
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * Rotates the component to the right (clockwise).
     */
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

    /**
     * Rotates the component to the left (counter-clockwise).
     *
     * @param change
     */
    public void updateBatteries(int change){
        batteries += change;
    }

    /**
     * Returns the number of batteries of the component.
     * @return the number of batteries of the component
     */
    public int getBatteries() {
        return batteries;
    }

    /**
     * Updates the number of crew members of the component.
     * @param change
     */
    public void updateCrew(int change){
        crew += change;
    }

    /**
     * Returns the number of crew members of the component.
     * @return the number of crew members of the component
     */
    public int getCrew() {
        return crew;
    }

    /**
     * Updates the alien status of the component.
     * @param isPurple
     */
    public void updateAlien(boolean isPurple){
        if(isPurple){
            purpleAlien = !purpleAlien;
        }
        else{
            brownAlien = !brownAlien;
        }
    }

    /**
     * Returns whether the component has a purple alien.
     * @return true if the component has a purple alien, false otherwise
     */
    public boolean isPurpleAlien() {
        return purpleAlien;
    }

    /**
     * Returns whether the component has a brown alien.
     * @return true if the component has a brown alien, false otherwise
     */
    public boolean isBrownAlien() {
        return brownAlien;
    }

    /**
     * Loads a good onto the component.
     * @param good
     */
    public void loadGood(Color good){
        goods.add(good);
    }

    /**
     * Removes a good from the component.
     * @param good
     */
    public void removeGood(Color good){
        for(Color g : goods){
            if(g == good){
                goods.remove(g);
                break;
            }
        }
    }

    /**
     * Returns the list of goods loaded onto the component.
     * @return a list of goods loaded onto the component
     */
    public List<Color> getGoods() {
        return new ArrayList<>(goods);
    }

    /**
     * Returns the number of goods loaded onto the component.
     * @return the number of goods loaded onto the component
     */
    public int getNumberGoods() {
        return goods.size();
    }

    /**
     * Returns a string representation of the component's image ID.
     * @return a string representation of the component's image ID
     */
    @Override
    public String toString(){
        return ImageIDToStringConverter.imageIDtoEID(this.imageID);
    }
}
