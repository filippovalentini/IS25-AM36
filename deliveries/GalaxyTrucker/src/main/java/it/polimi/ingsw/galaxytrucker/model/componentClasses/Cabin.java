package it.polimi.ingsw.galaxytrucker.model.componentClasses;

public class Cabin extends Component {
    private int numberCrew;
    private boolean purpleAlien;
    private boolean brownAlien;

    public Cabin() {
        super();
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
