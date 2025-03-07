package it.polimi.ingsw.galaxytrucker.model.componentClasses;

public class Battery extends ConfigurableComponent {
    private int numberBatteries;

    public Battery(boolean isDouble) {
        super(isDouble);
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
