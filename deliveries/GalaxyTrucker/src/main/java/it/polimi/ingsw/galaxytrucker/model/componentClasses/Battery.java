package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoBatteriesException;

import java.util.List;

public class Battery extends ConfigurableComponent {
    private int numberBatteries;        //number of batteries hold by the component

    public Battery(boolean isDouble, int imageID, List<Connector> sides) { //constructor
        super(isDouble, imageID, sides);
        if (isDouble) {
            this.numberBatteries = 2;
        }
        else {
            this.numberBatteries = 3;
        }
    }

    @Override
    public int getNumberBatteries() {
        return numberBatteries;
    }

    public void useBatteries(int batteriesToUse) throws NoBatteriesException {      //removes one or more batteries from the battery component
        if(batteriesToUse > numberBatteries) {
            throw new NoBatteriesException("Not enough batteries");
        }
        numberBatteries -= batteriesToUse;
    }
}
