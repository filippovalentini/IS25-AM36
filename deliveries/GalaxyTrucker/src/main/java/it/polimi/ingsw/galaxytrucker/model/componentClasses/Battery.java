package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoBatteriesException;

import java.util.ArrayList;
import java.util.List;

public class Battery extends ConfigurableComponent {
    private int numberBatteries;        //number of batteries hold by the component

    public Battery(boolean isDouble, int imageID, List<Connector> sides) { //constructor
        super(isDouble, imageID, sides);
        this.numberBatteries = 0;
    }

    @Override
    public int getNumberBatteries() {
        return numberBatteries;
    }
    @Override
    public void useBatteries(int batteriesToUse) throws NoBatteriesException {      //removes one or more batteries from the battery component
        if(batteriesToUse > numberBatteries) {
            throw new NoBatteriesException("Not enough batteries");
        }
        numberBatteries -= batteriesToUse;
    }
    @Override
    public Component clone(){//return a copy of the component
        Battery retComponent = new Battery(isDouble,this.imageID, new ArrayList<>(this.sides));
        retComponent.orientation = this.orientation;
        return retComponent;
    }
    @Override
    public boolean isFull(){
        if(isDouble && getNumberBatteries() == 2) {
            return true;
        }
        else return !isDouble && getNumberBatteries() == 3;
    }
    @Override
    public int addBatteries() throws NoBatteriesException{
        if(isFull()){
            throw new NoBatteriesException("Battery component is full");
        }
        if (isDouble) {
            this.numberBatteries = 2;
            return 2;
        }
        else {
            this.numberBatteries = 3;
            return 3;
        }
    }
}
