package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;

import java.util.*;
//this class is used to describe all information associated to the shipboard of a player
public class ShipBoard {
    protected int imageID;
    protected int lostComponents;     //number of misplaced tiles and of components destroyed during the game
    protected List<List<Component>> assembledComponents;      //components assembled on the ship board
    protected List<Component> reservedComponents;     //components reserved during the assembling phase
    protected Component pickedComponent;      //component which has been picked by a player and brought to its ship board
    protected final Color color;      //color associated to the ship board (and to the player that owns it)

    public ShipBoard(Color color) {     //constructor
        this.color = color;
        this.lostComponents = 0;
        this.assembledComponents = new ArrayList<>();
        this.reservedComponents = new ArrayList<>();
        this.pickedComponent = null;
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
    public List<List<Component>> getAssembledComponents() { //return a copy of the assembled components
        List<List<Component>> retAssembledComponents = new ArrayList<>();
        for (List<Component> assembledComponent : this.assembledComponents) {
            List<Component> row = new ArrayList<>(assembledComponent);
            retAssembledComponents.add(row);
        }
        return retAssembledComponents;
    }
    public Component getAssembledComponent(int x, int y) { //return a copy of the assembled component in the given position
        return (assembledComponents.get(x).get(y)).clone();
    }
    public List<Component> getReservedComponents() { //return a copy of the reserved components
        return new ArrayList<>(this.reservedComponents);
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
        if(assembledComponents.get(x).get(y) != null){
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
        if(assembledComponents.get(x).get(y) == null){
            throw new AssembledComponentException("No component to be destroyed");
        }
        assembledComponents.get(x).set(y, null);
        lostComponents++;
    }
}
