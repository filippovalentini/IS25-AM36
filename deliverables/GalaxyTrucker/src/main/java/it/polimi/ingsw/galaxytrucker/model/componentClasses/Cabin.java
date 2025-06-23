package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;

import java.util.ArrayList;
import java.util.List;
/**
 * Cabin class represents a cabin component in the game.
 */
public class Cabin extends Component {
    private int numberCrew;     //number of crew members in the cabin
    private boolean purpleAlien;        //true if there is a purple alien in the cabin
    private boolean brownAlien;     //true if there is a brown alien in the cabin
    /**
     * Constructor for Cabin class.
     * @param imageID the image ID of the cabin
     * @param sides the connectors of the cabin
     */
    public Cabin(int imageID, List<Connector> sides) {     //constructor
        super(imageID, sides);
        numberCrew = 0; //each cabin is empty at the beginning
        purpleAlien = false;
        brownAlien = false;
    }

    /**
     * returns purple alien if isPurple is true, otherwise returns brown alien
     * @param isPurple
     * @return true if the cabin has the specified alien, false otherwise
     */
    @Override
    public boolean hasAlien(boolean isPurple) {
        if(isPurple){
            return this.purpleAlien;
        }else{
            return this.brownAlien;
        }
    }

    /**
     * Removes crew members from the cabin.
     * @param lostCrew
     * @throws NoCrewException
     */

    @Override
    public void removeCrew(int lostCrew) throws NoCrewException {       //removes crew members from the cabin
        if(numberCrew < lostCrew){
            throw new NoCrewException("Not enough crew");
        }
        numberCrew-=lostCrew;
    }

    /**
     * Removes an alien from the cabin.
     * @param isPurple
     */
    public void removeAlien(boolean isPurple) {
        if (isPurple) {
            purpleAlien = false;
        }
        else {
            brownAlien = false;
        }
    }

    /**
     * Checks if the cabin has any members (crew or aliens).
     * @return
     */
    @Override
    public boolean hasMembers() {
        return numberCrew > 0 || brownAlien || purpleAlien;
    }

    /**
     * Adds crew members to the cabin.
     * @throws FullCabinException
     */
    @Override
    public void addCrew() throws FullCabinException {   //adds 2 crew members in the cabin (invoked after assembling phase)
        if(!hasMembers())
            numberCrew = 2;
        else
            throw new FullCabinException("The cabin is full");
    }

    /**
     * Adds an alien to the cabin.
     * @param isPurple
     * @throws FullCabinException
     */
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

    /**
     * Removes a member from the cabin.
     */
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

    /**
     * Returns the number of crew members in the cabin.
     * @return the number of crew members
     */
    @Override
    public int getNumberCrew() {
        return numberCrew;
    }

    /**
     * Checks if the cabin is full.
     * @return true if the cabin is full, false otherwise
     */
    @Override
    public boolean isFull() {
        return brownAlien || purpleAlien || (numberCrew == 2); //the cabin is full if it has 2 crew members or an alien
    }

    /**
     * Clones the Cabin component.
     * @return a new Cabin component with the same properties
     */
    @Override
    public Component clone(){
        //return a copy of the component
        Cabin retComponent = new Cabin(this.imageID, new ArrayList<>(this.sides));
        retComponent.orientation = this.orientation;
        retComponent.numberCrew = this.numberCrew;
        retComponent.purpleAlien = this.purpleAlien;
        retComponent.brownAlien = this.brownAlien;
        return retComponent;
    }
}
