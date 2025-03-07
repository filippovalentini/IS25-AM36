package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.List;

public class Cabin extends Component {
    private int numberCrew;
    private boolean purpleAlien;
    private boolean brownAlien;

    public Cabin(String imagePath, List<Connector> sides) {
        super(imagePath, sides);
        numberCrew = 0;
        purpleAlien = false;
        brownAlien = false;
    }

    public int getNumberCrew() {
        return numberCrew;
    }
    public boolean isPurpleAlien() {
        return purpleAlien;
    }
    public boolean isBrownAlien() {
        return brownAlien;
    }
    public void addCrew() {
        numberCrew = 2;
    }
    public void addAlien(boolean isPurple) {
        if (isPurple) {
            purpleAlien = true;
        }
        else {
            brownAlien = true;
        }
    }
    public void removeCrew(int lostCrew) {
        numberCrew-=lostCrew;
    }
    public void removeAlien(boolean isPurple) {
        if (isPurple) {
            purpleAlien = false;
        }
        else {
            brownAlien = false;
        }
    }
}
