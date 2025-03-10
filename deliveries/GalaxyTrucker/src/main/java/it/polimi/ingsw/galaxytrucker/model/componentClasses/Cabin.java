package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.exceptions.FullCabinException;

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
    public void addCrew() throws FullCabinException {
        if(purpleAlien == false && brownAlien == false)
            numberCrew = 2;
        else
            throw new FullCabinException("Alien already in cabin");
    }
    public void addAlien(boolean isPurple) throws FullCabinException {
        if(numberCrew > 0){
            throw new FullCabinException("Crew already in cabin");
        }
        else if (isPurple) {
            purpleAlien = true;
        }
        else {
            brownAlien = true;
        }
    }
    public void removeCrew(int lostCrew) {
        if(numberCrew < lostCrew){

        }
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
