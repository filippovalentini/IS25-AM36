package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.*;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.CannonShot;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.Meteor;

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

        for (int i = 0; i < 5; i++) {       //at the beginning of the assembling phase, all assembled components are set to empty
            List<Component> row = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                row.add(new Empty(0, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH))));
            }
            assembledComponents.add(row);
        }

        //based on the color associated to the ship board, a specific starting cabin is assembled in the middle
        if(color == Color.BLUE){
            assembledComponents.get(2).set(3, new Cabin(318, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))));
        }
        else if(color == Color.GREEN){
            assembledComponents.get(2).set(3, new Cabin(319, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))));
        }
        else if(color == Color.RED){
            assembledComponents.get(2).set(3, new Cabin(320, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))));
        }
        else{
            assembledComponents.get(2).set(3, new Cabin(321, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL))));

        }

    }

    public void assembleComponent(Component component, int x, int y) {
        assembledComponents.get(x).set(y, component);
        updateCorrectness();
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
    public Component reserveComponent() throws PickedComponentException, ReservedComponentException {
        if(pickedComponent==null){
            throw new PickedComponentException("No picked component");
        }
        else {
            if(reservedComponents.size() == 2){
                throw new ReservedComponentException("Too many reserved components");
            }
            else {
                reservedComponents.add(pickedComponent);
                Component c = pickedComponent;
                pickedComponent = null;
                return c;
            }
        }
    }
    //invoked when a player picks a specific component among the ones reserved for its ship board
    public Component pickReservedComponent(int position) throws ReservedComponentException, PickedComponentException {
        if(position < 0 || position >= reservedComponents.size()){
            throw new ReservedComponentException("Invalid reserved component position");
        }
        else if(pickedComponent!=null){
            throw new PickedComponentException("Already one component");
        }
        else {
            pickedComponent = reservedComponents.remove(position);
            return pickedComponent;
        }
    }
    //assembles the component picked by a player in the specified cell (x,y) of its ship board
    public Component assembleComponent(int x, int y) throws AssembledComponentException, PickedComponentException {
        Component c;
        Component emptySpace = new Empty(0, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH)));
        if(!assembledComponents.get(x).get(y).equals(emptySpace)){
            throw new AssembledComponentException("Already assembled component");
        }
        else if(pickedComponent==null){
            throw new PickedComponentException("No picked component");
        }
        else {
            assembledComponents.get(x).set(y, pickedComponent);
            c = pickedComponent;
            pickedComponent = null;
        }
        updateCorrectness();
        return c;

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
        if(this.hasMultipleRegions()){
            correct = false;
        }else {
            correct = correctness;
        }

    }
    //increases the number of lost components for the number of unused reserved components
    public void loseReservedComponents(){
        lostComponents+=reservedComponents.size();
    }
    //checks if the ship board has multiple regions (floating group of components)
    private boolean hasMultipleRegions(){
        boolean[][] visitedComponents = new boolean[assembledComponents.size()][assembledComponents.get(0).size()];
        int regionCount = 0;
        for(int i=0; i<assembledComponents.size(); i++){
            for(int j=0; j<assembledComponents.get(i).size(); j++){
                Component analyzedComponent = assembledComponents.get(i).get(j);
                if(!analyzedComponent.isNotEmpty() || !analyzedComponent.belongsToShip()){
                    continue;
                }
                if(!visitedComponents[i][j]){
                    if(regionCount >= 1){
                        return true;
                    }
                    dfs(assembledComponents, i, j, visitedComponents);
                    regionCount++;
                }
            }
        }
        return regionCount > 1;
    }
    //Deep-First-Search for multiple regions check
    private static void dfs(List<List<Component>> components, int row, int col, boolean[][] visited){
        if(row < 0 || col < 0 || row >= components.size() || col >= components.get(row).size()){
            return;
        }
        Component analyzedComponent = components.get(row).get(col);
        if(visited[row][col]){
            return;
        }
        if(!analyzedComponent.isNotEmpty() || !analyzedComponent.belongsToShip()){ // empty and space are considered as wall
            return;
        }
        visited[row][col] = true;
        //all adjacent directions
        dfs(components, row+1, col, visited);
        dfs(components, row-1, col, visited);
        dfs(components, row, col+1, visited);
        dfs(components, row, col-1, visited);
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
    //determines whether a side of the ship board is protected by a shield and (if yes) activates it by using
    //a battery
    public boolean protectedShipBoard(Orientation orientation) {
        boolean protection = false;
        for (List<Component> componentRow : assembledComponents) {
            for (Component component : componentRow) {
                if(component.protects(orientation)){
                    protection = true;
                    break;
                }
            }
            if(protection){
                break;
            }
        }

        if(protection){
            removeBatteries(1);
        }

        return protection;
    }
    //determines whether a row/column of a ship board is armed (has a cannon) in a specific direction; it can
    //also activate a double cannon if specified and if necessary
    public boolean armedShipBoard(boolean activatedCannon, Orientation orientation, int direction) {
        for (int i = 0; i < assembledComponents.size(); i++) {
            for (int j = 0; j < assembledComponents.get(i).size(); j++) {
                Component c = assembledComponents.get(i).get(j);
                if(c.getOrientation() != orientation){
                    continue;
                }
                if(c.hasSingleCannon()){
                    if(     (orientation == Orientation.NORTH && j == direction) ||
                            (orientation == Orientation.SOUTH && j == direction) ||
                            (orientation == Orientation.EAST && i == direction) ||
                            (orientation == Orientation.WEST && i == direction)){
                        return true;
                    }
                }
                if(c.hasDoubleCannons() && activatedCannon){
                    if(     (orientation == Orientation.NORTH && j == direction) ||
                            (orientation == Orientation.SOUTH && j == direction) ||
                            (orientation == Orientation.EAST && i == direction) ||
                            (orientation == Orientation.WEST && i == direction)){
                        removeBatteries(1);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    //determines whether the ship board exposes a smooth side in a specific direction
    public boolean smoothSide(Orientation orientation, int direction){
        Component c;
        if(orientation == Orientation.NORTH){
            for (List<Component> componentRow : assembledComponents) {
                c = componentRow.get(direction);
                if (c.isNotEmpty() && c.belongsToShip()) {
                    if (c.getNorthSide() == Connector.SMOOTH) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        else if(orientation == Orientation.SOUTH){
            for(int i=assembledComponents.size()-1; i>=0; i--){
                c = assembledComponents.get(i).get(direction);
                if(c.isNotEmpty() && c.belongsToShip()){
                    if(c.getSouthSide()==Connector.SMOOTH){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
            }
        }
        else if(orientation == Orientation.WEST){
            for(int j=0; j<assembledComponents.get(direction).size(); j++){
                c = assembledComponents.get(direction).get(j);
                if(c.isNotEmpty() && c.belongsToShip()){
                    if(c.getWestSide()==Connector.SMOOTH){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
            }
        }
        else{
            for(int j=assembledComponents.get(direction).size()-1; j>=0; j--){
                c = assembledComponents.get(direction).get(j);
                if(c.isNotEmpty() && c.belongsToShip()){
                    if(c.getEastSide()==Connector.SMOOTH){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
            }
        }
        return true;
    }
    //destroys the first component of the ship from north, in the specified column
    public void destroyNorth(int column){
        for(int i=0; i<assembledComponents.size(); i++){
            Component c = assembledComponents.get(i).get(column);
            if(c.isNotEmpty() && c.belongsToShip()){
                destroyComponent(i,column);
                break;
            }
        }
    }
    //destroys the first component of the ship from south, in the specified column
    public void destroySouth(int column){
        for(int i=assembledComponents.size()-1; i>=0; i--){
            Component c = assembledComponents.get(i).get(column);
            if(c.isNotEmpty() && c.belongsToShip()){
                destroyComponent(i,column);
                break;
            }
        }
    }
    //destroys the first component of the ship from east, in the specified row
    public void destroyEast(int row){
        for(int j=assembledComponents.get(row).size() - 1; j>=0; j--){
            Component c = assembledComponents.get(row).get(j);
            if(c.isNotEmpty() && c.belongsToShip()){
                destroyComponent(row,j);
            }
        }
    }
    //destroys the first component of the ship from west, in the specified row
    public void destroyWest(int row){
        for(int j=0; j< assembledComponents.get(row).size(); j++){
            Component c = assembledComponents.get(row).get(j);
            if(c.isNotEmpty() && c.belongsToShip()){
                destroyComponent(row,j);
            }
        }
    }
    //remove the specified crew members from the specified cabins in the ship board
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
    //substitutes the cargo good at the given coordinates with the good given in input
    public void substituteGoods(int cargo_row, int cargo_col, Color good, int pos){
        if(good==Color.RED){ //special cargo needed
            ((CargoSpecial)(assembledComponents).get(cargo_row).get(cargo_col)).substituteGood(good, pos);
        }else {
            ((CargoHold) (assembledComponents.get(cargo_row).get(cargo_col))).substituteGood(good, pos);
        }
    }
    //invoked when a meteor/cannon shot hits the ship board
    public void meteorAttack(Meteor meteor, int direction, boolean activateShield, boolean activateCannon){
        Orientation orientation = meteor.getOrientation();
        //in this case the meteor doesn't hit the ship board
        if(direction < 0){
            return;
        }
        //in this case the meteor doesn't hit the ship board
        if((orientation.isVertical() && direction > 6) || (orientation.isHorizontal() && direction > 4)){
            return;
        }
        //if the meteor is small, if an appropriate shield is activated or if the meteor hits a smooth side, then
        //nothing is destroyed
        if(!meteor.isLarge()){
            if((activateShield && protectedShipBoard(orientation)) || smoothSide(orientation, direction)){
                return;
            }
        }
        //if the meteor is large, if an appropriate cannon points towards the meteor, then nothing is destroyed
        else {
            if(armedShipBoard(activateCannon, orientation, direction)){
                return;
            }
        }
        //if the ship board is not safe the meteor destroys a component
        if(orientation == Orientation.NORTH){
            destroyNorth(direction);
        }else if(orientation == Orientation.SOUTH){
            destroySouth(direction);
        }else if(orientation == Orientation.WEST){
            destroyWest(direction);
        }else{
            destroyEast(direction);
        }
    }
    //invoked when a cannon shot hits the ship board
    public void cannonFireAttack(CannonShot cannonFire, int direction, boolean activateShield){
        Orientation orientation = cannonFire.getOrientation();
        //in this case the cannon fire doesn't hit the ship board
        if(direction < 0){
            return;
        }
        //in this case the cannon fire doesn't hit the ship board
        if((orientation.isVertical() && direction > 6) || (orientation.isHorizontal() && direction > 4)){
            return;
        }
        //if the cannon fire is small, if an appropriate shield is activated, then nothing is destroyed
        if(!cannonFire.isLarge()){
            if(activateShield && protectedShipBoard(orientation)){
                return;
            }
        }
        //if the ship board is not safe or if the cannon fire is big, the meteor destroys a component
        if(orientation == Orientation.NORTH){
            destroyNorth(direction);
        }else if(orientation == Orientation.SOUTH){
            destroySouth(direction);
        }else if(orientation == Orientation.WEST){
            destroyWest(direction);
        }else{
            destroyEast(direction);
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
    //removes batteries from the ship board
    public void removeBatteries(int batteries) throws NoBatteriesException{
        int toRemove = batteries;
        int componentBatteries;
        for (List<Component> componentRow : assembledComponents) {
            for (Component component : componentRow) {
                componentBatteries = component.getNumberBatteries();
                if(componentBatteries > 0){
                    if(componentBatteries >= toRemove){
                        component.useBatteries(toRemove);
                        toRemove = 0;
                        break;
                    }
                    else{
                        component.useBatteries(componentBatteries);
                        toRemove-=componentBatteries;
                    }
                }
            }
            if(toRemove ==0){
                break;
            }
        }
    }
    //returns the number of goods on the ship board
    public int getNumberGoods() {
        int numberGoods = 0;
        for (List<Component> componentRow : assembledComponents) {
            for (Component component : componentRow) {
                numberGoods += component.getNumberGoods();
            }
        }
        return numberGoods;
    }
    //this method removes numberGoods goods of a specific color from the ship board; if there aren't enough
    //goods of that color, it returns the number of missing goods, otherwise it returns 0
    public int removeSpecificGoods(Color color, int numberGoods){
        int toRemove = numberGoods;
        int componentGoods;
        for (List<Component> componentRow : assembledComponents) {
            for (Component component : componentRow) {
                componentGoods = component.getNumberGoods(color);
                if(componentGoods > 0){
                    if(componentGoods >= toRemove){
                        component.removeSpecificGoods(color, toRemove);
                        toRemove = 0;
                        break;
                    }
                    else{
                        component.removeSpecificGoods(color, componentGoods);
                        toRemove-= componentGoods;
                    }
                }
            }
            if(toRemove ==0){
                break;
            }
        }
        return toRemove;
    }
    //this method removes the numberGoods-most precious goods from the  ship board
    public void losePreciousGoods(int numberGoods){
        int toRemove = numberGoods;
        int componentGoods;
        toRemove = removeSpecificGoods(Color.RED, toRemove);
        toRemove = removeSpecificGoods(Color.YELLOW, toRemove);
        toRemove = removeSpecificGoods(Color.GREEN, toRemove);
        toRemove = removeSpecificGoods(Color.BLUE, toRemove);
        if(toRemove > 0){
            removeBatteries(toRemove);
        }
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
    //returns the number of double cannons on the ship board
    public int getNumberForwardDoubleCannons() {
        int numberDoubleCannons = 0;
        for (List<Component> componentRow : assembledComponents) {
            for (Component component : componentRow) {
                if(component.hasDoubleCannons() && component.pointsForward()){
                    numberDoubleCannons++;
                }
            }
        }
        return numberDoubleCannons;
    }
    //returns the number of single cannons on the ship board
    public int getNumberForwardSingleCannons() {
        int numberSingleCannons = 0;
        for (List<Component> componentRow : assembledComponents) {
            for (Component component : componentRow) {
                if(component.hasSingleCannon() && component.pointsForward()){
                    numberSingleCannons++;
                }
            }
        }
        return numberSingleCannons;
    }
    //returns the cannon strength of the ship board, removing the given batteries in order to activate double cannons
    public double getCannonStrength(int usedBatteries) throws NoBatteriesException{
        int activatedDoubleCannons = Math.min(getNumberDoubleCannons(), usedBatteries);
        removeBatteries(activatedDoubleCannons);
        int forwardDoubleCannons = Math.min(getNumberForwardDoubleCannons(), activatedDoubleCannons);
        int lateralDoubleCannons = ((activatedDoubleCannons > forwardDoubleCannons) ? activatedDoubleCannons-forwardDoubleCannons : 0);
        int forwardSingleCannons = getNumberForwardSingleCannons();
        int lateralSingleCannons = getNumberSingleCannons() - forwardSingleCannons;
        return lateralDoubleCannons + forwardDoubleCannons*2 + lateralSingleCannons*0.5 + forwardSingleCannons;
    }
    //returns the engine strength of the ship board, removing the given batteries in order to activate double engines
    public int getEngineStrength(int usedBatteries) throws NoBatteriesException {
        int activatedDoubleEngines = Math.min(getNumberDoubleEngines(), usedBatteries);
        removeBatteries(activatedDoubleEngines);
        return activatedDoubleEngines*2 + getNumberSingleEngines();
    }
    //returns the overall price for all the goods carried by the player's ship
    public int getGoodsPrice(){
        int goodsPrice = 0;
        for(List<Component> componentRow : assembledComponents){
            for(Component component : componentRow){
                goodsPrice+=component.goodsPrice();
            }
        }
        return goodsPrice;
    }


    public void printShip() {
                int righe = 5;
                int colonne = 7;
                int altezzaCella = 3; // altezza interna
                int larghezzaCella = 10; // larghezza interna

                for (int r = 0; r < righe; r++) {
                    // Riga superiore delle celle
                    for (int c = 0; c < colonne; c++) {
                        System.out.print("┌");
                        for (int i = 0; i < larghezzaCella; i++) {
                            System.out.print("─");
                        }
                        System.out.print("┐");
                    }
                    System.out.println();

                    // Parte centrale
                    for (int i = 0; i < altezzaCella; i++) {
                        for (int c = 0; c < colonne; c++) {
                            System.out.print("│");
                            for (int j = 0; j < larghezzaCella; j++) {
                                System.out.print(" ");

                            }
                            System.out.print("│");
                        }
                        System.out.println();
                    }

                    // Riga inferiore delle celle
                    for (int c = 0; c < colonne; c++) {
                        System.out.print("└");
                        for (int i = 0; i < larghezzaCella; i++) {
                            System.out.print("─");
                        }
                        System.out.print("┘");
                    }
                    System.out.println();
                }
            }
        }




