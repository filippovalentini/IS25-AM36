package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoBatteriesException;

import java.util.ArrayList;
import java.util.List;

/**
 * Battery class represents a battery component in the game.
 */
public class Battery extends ConfigurableComponent {
    private int numberBatteries;        //number of batteries hold by the component

    /**
     * Constructor for Battery class.
     * @param isDouble
     * @param imageID
     * @param sides
     */
    public Battery(boolean isDouble, int imageID, List<Connector> sides) { //constructor
        super(isDouble, imageID, sides);
        this.numberBatteries = 0;
    }

    /**
     * Returns the number of batteries in the battery component.
     * @return the number of batteries
     */
    @Override
    public int getNumberBatteries() {
        return numberBatteries;
    }

    /**
     * Removes one or more batteries from the battery component
     * @param batteriesToUse
     * @throws NoBatteriesException
     */
    @Override
    public void useBatteries(int batteriesToUse) throws NoBatteriesException {
        if(batteriesToUse > numberBatteries) {
            throw new NoBatteriesException("Not enough batteries");
        }
        numberBatteries -= batteriesToUse;
    }

    /**
     * Clones the  component.
     * @return a new Battery component with the same properties
     */
    @Override
    public Component clone(){//return a copy of the component
        Battery retComponent = new Battery(isDouble,this.imageID, new ArrayList<>(this.sides)); //create a new component with the same properties
        retComponent.orientation = this.orientation; //copy the orientation
        retComponent.numberBatteries = this.numberBatteries; //copy the number of batteries
        return retComponent;
    }

    /**
     * Checks if the battery component is full.
     * @return true if the battery component is full, false otherwise
     */
    @Override
    public boolean isFull(){
        if(isDouble && getNumberBatteries() == 2) { //if the battery is double, it can hold 2 batteries
            return true;
        }
        else return !isDouble && getNumberBatteries() == 3; //if the battery is not double, it can hold 3 batteries
    }

    /**
     * Adds batteries to the battery component.
     * @return the number of batteries added
     * @throws NoBatteriesException
     */
    @Override
    public int addBatteries() throws NoBatteriesException{
        if(isFull()){ //if the battery is full, it cannot hold more batteries
            throw new NoBatteriesException("Battery component is full");
        }
        if (isDouble) { //if the battery is double, it can hold 2 batteries
            this.numberBatteries = 2;
            return 2;
        }
        else { //if the battery is not double, it can hold 3 batteries
            this.numberBatteries = 3;
            return 3;
        }
    }
}
