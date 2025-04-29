package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;

import java.util.ArrayList;
import java.util.List;

public class Cabin extends Component {
    private int numberCrew;     //number of crew members in the cabin
    private boolean purpleAlien;        //true if there is a purple alien in the cabin
    private boolean brownAlien;     //true if there is a brown alien in the cabin

    public Cabin(int imageID, List<Connector> sides) {     //constructor
        super(imageID, sides);
        numberCrew = 0; //each cabin is empty at the beginning
        purpleAlien = false;
        brownAlien = false;
    }

    public boolean hasPurpleAlien() {
        return purpleAlien;
    }
    public boolean hasBrownAlien() {
        return brownAlien;
    }

@Override
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

    @Override
    public boolean hasMembers() {
        return numberCrew > 0 || brownAlien || purpleAlien;
    }
    @Override
    public void addCrew() throws FullCabinException {   //adds 2 crew members in the cabin (invoked after assembling phase)
        if(!hasMembers())
            numberCrew = 2;
        else
            throw new FullCabinException("The cabin is full");
    }
    @Override
    public void addAlien(boolean isPurple) throws FullCabinException {      //adds alien in the cabin (invoked after assembling phase)
        if(hasMembers()){
            throw new FullCabinException("The cabin is full");
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
    @Override
    public void removeMember() {
        try{
            removeCrew(1);
        }
        catch(NoCrewException e){
            removeAlien(true);
            removeAlien(false);
        }
    }
    @Override
    public int getNumberCrew() {
        return numberCrew;
    }
    @Override
    public boolean isFull() {
        return brownAlien || purpleAlien || (numberCrew == 2);
    }
    @Override
    public Component clone(){
        //return a copy of the component
        Cabin retComponent = new Cabin(this.imageID, new ArrayList<>(this.sides));
        retComponent.orientation = this.orientation;
        return retComponent;
    }
}
