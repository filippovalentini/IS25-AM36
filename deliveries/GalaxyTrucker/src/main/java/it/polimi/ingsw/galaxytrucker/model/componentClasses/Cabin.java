package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;

import java.util.List;

public class Cabin extends Component {
    private int numberCrew;     //number of crew members in the cabin
    private boolean purpleAlien;        //true if there is a purple alien in the cabin
    private boolean brownAlien;     //true if there is a brown alien in the cabin

    public Cabin(int imageID, List<Connector> sides) {     //constructor
        super(imageID, sides);
        numberCrew = 2; //each cabin when game starts has 2 members
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

    public void addCrew() throws FullCabinException {   //adds 2 crew members in the cabin (invoked after assembling phase)
        if(!purpleAlien && !brownAlien)
            numberCrew = 2;
        else
            throw new FullCabinException("Alien already in cabin");
    }
    public void addAlien(boolean isPurple) throws FullCabinException {      //adds alien in the cabin (invoked after assembling phase)
        if(numberCrew > 0){
            throw new FullCabinException("Crew already in cabin");
        }
        else if(brownAlien || purpleAlien){
            throw new FullCabinException("Alien already in cabin");
        }
        else{
            if(isPurple){
                purpleAlien = true;
            }
            else{
                brownAlien = true;
            }
        }
    }
    public void removeCrew(int lostCrew) throws NoCrewException {       //removes crew members from the cabin
        if(numberCrew < lostCrew){
            throw new NoCrewException("Not enough crew");
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
