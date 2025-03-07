package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.List;

public class Battery extends ConfigurableComponent {
    private int numberBatteries;

    public Battery(boolean isDouble, String imagePath, List<Connector> sides) {
        super(isDouble, imagePath, sides);
        if (isDouble) {
            this.numberBatteries = 2;
        }
        else {
            this.numberBatteries = 3;
        }
    }
    public int getNumberBatteries() {
        return numberBatteries;
    }
    public void useBatteries(int batteriesToUse) {
        numberBatteries -= batteriesToUse;
    }
}
