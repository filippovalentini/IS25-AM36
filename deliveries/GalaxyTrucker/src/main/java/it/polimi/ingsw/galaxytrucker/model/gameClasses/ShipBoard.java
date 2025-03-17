package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.*;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;

import java.util.*;
//this class is used to describe all information associated to the shipboard of a player
public class ShipBoard {
    protected int imageID;
    protected int lostComponents;     //number of misplaced tiles and of components destroyed during the game
    protected List<List<Component>> assembledComponents;     //components assembled on the ship board
    protected List<Component> reservedComponents;     //components reserved during the assembling phase
    protected Component pickedComponent;      //component which has been picked by a player and brought to its ship board
    protected final Color color;      //color associated to the ship board (and to the player that owns it)
    protected boolean correct;      //determines if the ship is correctly assembled

    public ShipBoard(Color color) {     //constructor
        this.color = color;
        this.lostComponents = 0;
        this.assembledComponents = new ArrayList<>();
        this.reservedComponents = new ArrayList<>();
        this.pickedComponent = null;
        this.correct = true;

    }
    /*
    public ShipBoard clone() { //return a copy of the ShipBoard

        ShipBoard retShipBoard = new ShipBoard(this.color);
        retShipBoard.lostComponents = this.lostComponents;
        retShipBoard.assembledComponents = new ArrayList<>();
        for(int i=0; i<this.assembledComponents.size(); i++){
            List<Component> row = new ArrayList<>(this.assembledComponents.get(i));
            retShipBoard.assembledComponents.add(row);
        }
        retShipBoard.reservedComponents = new ArrayList<>(this.reservedComponents);
        if(this.pickedComponent != null){
            retShipBoard.pickedComponent = this.pickedComponent.clone();
        }
        return retShipBoard;
    }
    */

    public Component getAssembledComponent(int x, int y) { //return a copy of the assembled component in the given position
        return (assembledComponents.get(x).get(y)).clone();
    }
    public boolean isEmptyComponent(int x, int y) { //should have used instead of getAssembledComponent for empty check
        return assembledComponents.get(x).get(y).getClass() == Empty.class;
    }
    public Component getPickedComponent() { //return the actual picked component
        return pickedComponent;
    }
    public Color getColor() {
        return color;
    }
    public int getImageID() {
        return imageID;
    }
    public int getLostComponents() {
        return lostComponents;
    }
    public boolean isCorrect() {
        return correct;
    }
    //invoked when the owner of the ship board picks a component from the table
    public void pickComponent(Component component) throws PickedComponentException {
        if(pickedComponent!=null){
            throw new PickedComponentException("Already one component");
        }
        else {
            this.pickedComponent = component;
        }
    }
    //the picked component is released, therefore returned so that it can be shown to the other players
    public Component releaseComponent() throws PickedComponentException {
        if(pickedComponent==null){
            throw new PickedComponentException("No picked component");
        }
        else {
            Component c = pickedComponent;
            pickedComponent = null;
            return c;
        }
    }
    //the picked component is added to the reserved components for the ship board
    public void reserveComponent() throws PickedComponentException, ReservedComponentException {
        if(pickedComponent==null){
            throw new PickedComponentException("No picked component");
        }
        else {
            if(reservedComponents.size() == 2){
                throw new ReservedComponentException("Too many reserved components");
            }
            else {
                reservedComponents.add(pickedComponent);
                pickedComponent = null;
            }
        }
    }
    //invoked when a player picks a specific component among the ones reserved for its ship board
    public void pickReservedComponent(int position) throws ReservedComponentException, PickedComponentException {
        if(position < 0 || position >= reservedComponents.size()){
            throw new ReservedComponentException("Invalid reserved component position");
        }
        else if(pickedComponent!=null){
            throw new PickedComponentException("Already one component");
        }
        else {
            pickedComponent = reservedComponents.remove(position);
        }
    }
    //assembles the component picked by a player in the specified cell (x,y) of its ship board
    public void assembleComponent(int x, int y) throws AssembledComponentException, PickedComponentException {
        Empty emptySpace = new Empty(0, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH)));
        if(!assembledComponents.get(x).get(y).equals(emptySpace)){
            throw new AssembledComponentException("Already assembled component");
        }
        else if(pickedComponent==null){
            throw new PickedComponentException("No picked component");
        }
        else {
            assembledComponents.get(x).set(y, pickedComponent);
            pickedComponent = null;
        }
    }
    //removes an assembled component from the ship board
    public void destroyComponent(int x, int y) throws AssembledComponentException {
        Empty emptySpace = new Empty(0, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH)));
        if(assembledComponents.get(x).get(y).equals(emptySpace)){
            throw new AssembledComponentException("No component to be destroyed");
        }
        assembledComponents.get(x).set(y, emptySpace);
        lostComponents++;
    }
    //remove the specified crew members from each cabin in the ship board
    public void removeCrewMembers(List<Integer> x, List<Integer> y, List<Integer> eachCabinCrew, int numberCrewToRemove) throws NoCrewException {
        int sumRemovedCrewMembers = 0;
        for(int i=0; i<x.size(); i++){
            if(assembledComponents.get(x.get(i)).get(y.get(i)).getClass() != Cabin.class){
                throw new NoCrewException("Invalid component, it must be a cabin");
            }
            else {
                ((Cabin) assembledComponents.get(x.get(i)).get(y.get(i))).removeCrew(eachCabinCrew.get(i));
                sumRemovedCrewMembers++;
            }
        }
        if(sumRemovedCrewMembers != numberCrewToRemove){
            throw new NoCrewException("Wrong number of crew members to remove");
        }
    }

    //get the number of crew members in the ship board
    public int getNumberCrew() {
        int numberCrew = 0;
        for (List<Component> assembledComponent : assembledComponents) {
            for (Component component : assembledComponent) {
                if (component.getClass() == Cabin.class) {
                    numberCrew += ((Cabin) component).getNumberCrew();
                }
            }
        }
        return numberCrew;
    }

}
