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
        Component emptySpace = new Empty(0, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH)));
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
        updateCorrectness();

    }
    //rotates the picked component left
    public void rotatePickedComponent() throws PickedComponentException {
        if(pickedComponent==null){
            throw new PickedComponentException("No picked component");
        }
        else {
            pickedComponent.rotateLeft();
        }
    }
    //removes an assembled component from the ship board
    public void destroyComponent(int x, int y) throws AssembledComponentException {
        Component emptySpace = new Empty(0, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH)));
        if(assembledComponents.get(x).get(y).equals(emptySpace)){
            throw new AssembledComponentException("No component to be destroyed");
        }
        assembledComponents.get(x).set(y, emptySpace);
        lostComponents++;
        updateCorrectness();
    }
    //determines if the ship board is correctly assembled
    public void updateCorrectness(){
        boolean correctness = true;

        for (int i = 0; i < assembledComponents.size(); i++) {
            for(int j = 0; j < assembledComponents.get(i).size(); j++){
                Component c = assembledComponents.get(i).get(j);
                if(c.isNotEmpty() && c.belongsToShip()){
                    if(!c.isWellOriented()){
                        correctness = false;
                        break;
                    }
                    if(i>0){
                        Component c1 = assembledComponents.get(i-1).get(j);
                        if(!c.getNorthSide().compatibleWith(c1.getSouthSide()) && c1.isNotEmpty() && c1.belongsToShip()){
                            correctness = false;
                            break;
                        }
                    }
                    if(i<assembledComponents.size()-1){
                        Component c1 = assembledComponents.get(i+1).get(j);
                        if(!c.getSouthSide().compatibleWith(c1.getNorthSide()) && c1.isNotEmpty() && c1.belongsToShip()){
                            correctness = false;
                            break;
                        }
                    }
                    if(j>0){
                        Component c1 = assembledComponents.get(i).get(j-1);
                        if(!c.getWestSide().compatibleWith(c1.getEastSide()) && c1.isNotEmpty() && c1.belongsToShip()){
                            correctness = false;
                            break;
                        }
                    }
                    if(j<assembledComponents.get(i).size()-1){
                        Component c1 = assembledComponents.get(i).get(j+1);
                        if(!c.getEastSide().compatibleWith(c1.getWestSide()) && c1.isNotEmpty() && c1.belongsToShip()){
                            correctness = false;
                            break;
                        }
                    }
                }
            }
            if(!correctness){
                break;
            }
        }

        correct = correctness;

    }
    //counts the number of exposed connectors of the ship board
    public int countExposedConnectors() {
        int exposedConnectors = 0;

        for (int i = 0; i < assembledComponents.size(); i++) {
            for(int j = 0; j < assembledComponents.get(i).size(); j++){
                Component c = assembledComponents.get(i).get(j);
                if(c.isNotEmpty() && c.belongsToShip()){
                    if(i>0){
                        Component c1 = assembledComponents.get(i-1).get(j);
                        if(c.getNorthSide()!=Connector.SMOOTH && (!c1.isNotEmpty() || !c1.belongsToShip())){
                            exposedConnectors++;
                        }
                    }
                    if(i<assembledComponents.size()-1){
                        Component c1 = assembledComponents.get(i+1).get(j);
                        if(c.getNorthSide()!=Connector.SMOOTH && (!c1.isNotEmpty() || !c1.belongsToShip())){
                            exposedConnectors++;
                        }
                    }
                    if(j>0){
                        Component c1 = assembledComponents.get(i).get(j-1);
                        if(c.getNorthSide()!=Connector.SMOOTH && (!c1.isNotEmpty() || !c1.belongsToShip())){
                            exposedConnectors++;
                        }
                    }
                    if(j<assembledComponents.get(i).size()-1){
                        Component c1 = assembledComponents.get(i).get(j+1);
                        if(c.getNorthSide()!=Connector.SMOOTH && (!c1.isNotEmpty() || !c1.belongsToShip())){
                            exposedConnectors++;
                        }
                    }
                }
            }
        }
        return exposedConnectors;
    }
    //removes a member (human or alien) from each cabin that is directly connected with another busy cabin
    public void epidemicEffect() {
        List<int[]> hitCabins = new ArrayList<>();

        for (int i = 0; i < assembledComponents.size(); i++) {
            for(int j = 0; j < assembledComponents.get(i).size(); j++){
                Component c = assembledComponents.get(i).get(j);
                if(c.hasMembers()){
                    if(i>0){
                        Component c1 = assembledComponents.get(i-1).get(j);
                        if(c1.hasMembers()){
                            hitCabins.add(new int[]{i,j});
                            break;
                        }
                    }
                    if(i<assembledComponents.size()-1){
                        Component c1 = assembledComponents.get(i+1).get(j);
                        if(c1.hasMembers()){
                            hitCabins.add(new int[]{i,j});
                            break;
                        }
                    }
                    if(j>0){
                        Component c1 = assembledComponents.get(i).get(j-1);
                        if(c1.hasMembers()){
                            hitCabins.add(new int[]{i,j});
                            break;
                        }
                    }
                    if(j<assembledComponents.get(i).size()-1){
                        Component c1 = assembledComponents.get(i).get(j+1);
                        if(c1.hasMembers()){
                            hitCabins.add(new int[]{i,j});
                            break;
                        }
                    }
                }
            }
        }

        for (int[] hitCabin : hitCabins) {
            Component c = assembledComponents.get(hitCabin[0]).get(hitCabin[1]);
            c.removeMember();
        }
    }
    //returns the number of crew members in the ship board
    public int getNumberCrew() {
        int numberCrew = 0;
        for (List<Component> componentRow : assembledComponents) {
            for (Component component : componentRow) {
                numberCrew += component.getNumberCrew();
            }
        }
        return numberCrew;
    }
    //returns the number of batteries on the ship board
    public int getNumberBatteries() {
        int numberBatteries = 0;
        for (List<Component> componentRow : assembledComponents) {
            for (Component component : componentRow) {
                numberBatteries += component.getNumberBatteries();
            }
        }
        return numberBatteries;
    }
    //returns the number of double engines on the ship board
    public int getNumberDoubleEngines() {
        int numberDoubleEngines = 0;
        for (List<Component> componentRow : assembledComponents) {
            for (Component component : componentRow) {
                if(component.hasDoubleEngines()){
                    numberDoubleEngines++;
                }
            }
        }
        return numberDoubleEngines;
    }
    //returns the number of single engines on the ship board
    public int getNumberSingleEngines() {
        int numberSingleEngines = 0;
        for (List<Component> componentRow : assembledComponents) {
            for (Component component : componentRow) {
                if(component.hasSingleEngine()){
                    numberSingleEngines++;
                }
            }
        }
        return numberSingleEngines;
    }
    //returns the number of double cannons on the ship board
    public int getNumberDoubleCannons() {
        int numberDoubleCannons = 0;
        for (List<Component> componentRow : assembledComponents) {
            for (Component component : componentRow) {
                if(component.hasDoubleCannons()){
                    numberDoubleCannons++;
                }
            }
        }
        return numberDoubleCannons;
    }
    //returns the number of single cannons on the ship board
    public int getNumberSingleCannons() {
        int numberSingleCannons = 0;
        for (List<Component> componentRow : assembledComponents) {
            for (Component component : componentRow) {
                if(component.hasSingleCannon()){
                    numberSingleCannons++;
                }
            }
        }
        return numberSingleCannons;
    }
    //returns the cannon strength of the ship board, removing the given batteries in order to activate double cannons
    public float getCannonStrength(int usedBatteries){
        return 1;
    }
    //returns the engine strength of the ship board, removing the given batteries in order to activate double engines
    public float getEngineStrength(int usedBatteries) throws NoBatteriesException {
        int activatedDoubleEngines = Math.min(getNumberDoubleEngines(), usedBatteries);
        int batteries = activatedDoubleEngines;
        int componentBatteries;
        for (List<Component> componentRow : assembledComponents) {
            for (Component component : componentRow) {
                componentBatteries = component.getNumberBatteries();
                if(componentBatteries > 0){
                    if(componentBatteries > batteries){
                        component.useBatteries(batteries);
                        batteries = 0;
                        break;
                    }
                    else{
                        component.useBatteries(componentBatteries);
                        batteries-=componentBatteries;
                    }
                }
            }
            if(batteries ==0){
                break;
            }
        }
        return activatedDoubleEngines*2 + getNumberSingleEngines();
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
                sumRemovedCrewMembers += eachCabinCrew.get(i);
            }
        }
        if(sumRemovedCrewMembers != numberCrewToRemove){
            throw new NoCrewException("Wrong number of crew members to remove");
        }
    }
    //substitute cargo goods at the given coordinates with the goods given in input
    public void substituteCargoGoodGivenGood(int cargo_row, int cargo_col, Color good, int pos){
        if(color==Color.RED){ //special cargo needed
            ((CargoSpecial)(assembledComponents).get(cargo_row).get(cargo_col)).substituteGood(good, pos);
        }else {
            ((CargoHold) (assembledComponents.get(cargo_row).get(cargo_col))).substituteGood(good, pos);
        }
    }
}
