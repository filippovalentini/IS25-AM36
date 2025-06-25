package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.*;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.CannonShot;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.Meteor;
import it.polimi.ingsw.galaxytrucker.network.VirtualView;

import java.util.*;

/**
 *
 * This class is used to describe all information associated to the shipboard of a player
 */
public class ShipBoard {
    protected final String nickname;
    protected int imageID;
    protected int lostComponents;     //number of misplaced tiles and of components destroyed during the game
    protected List<List<Component>> assembledComponents;     //components assembled on the ship board
    protected List<Component> reservedComponents;     //components reserved during the assembling phase
    protected Component pickedComponent;      //component which has been picked by a player and brought to its ship board
    protected final Color color;      //color associated to the ship board (and to the player that owns it)
    protected boolean correct;      //determines if the ship is correctly assembled
    protected Map<String, VirtualView> clients;     //list of observers (clients of the game)

    /**
     * Constructor of the ship board
     * @param nickname
     * @param color
     */
    public ShipBoard(String nickname, Color color) {     //constructor
        this.nickname = nickname;
        this.color = color;
        this.lostComponents = 0;
        this.assembledComponents = new ArrayList<>();
        this.reservedComponents = new ArrayList<>();
        this.pickedComponent = null;
        this.correct = true;
        this.clients = new HashMap<>();

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

    //EXTRA METHODS NEEDED FOR TESTING

    /**
     * Returns clients
     * @return a map of clients
     */
    public Map<String, VirtualView> getClients(){
        return new HashMap<>(this.clients);
    }

    /**
     * Assembles a component in the specified position of the ship board
     * @param component
     * @param x
     * @param y
     */
    public void assembleComponent(Component component, int x, int y) {
        assembledComponents.get(x).set(y, component);
        updateCorrectness();
    }


    /**
     * Returns the assembled component in the specified position of the ship board
     * @param x
     * @param y
     * @return a component
     */
    public Component getAssembledComponent(int x, int y) { //return a copy of the assembled component in the given position
        return (assembledComponents.get(x).get(y)).clone();
    }

    /**
     * Checks if the component in the specified position of the ship board is empty
     * @param x
     * @param y
     * @return true if the component is empty, false otherwise
     */
    public boolean isEmptyComponent(int x, int y) { //should have used instead of getAssembledComponent for empty check
        return assembledComponents.get(x).get(y).getClass() == Empty.class;
    }

    /**
     * Returns the actual picked component
     * @return the picked component
     */
    public Component getPickedComponent() { //return the actual picked component
        return pickedComponent;
    }

    /**
     * Returns the color
     * @return the color of the ship board
     */
    public Color getColor() {
        return color;
    }

    /**
     * Return the image ID
     * @return the image ID
     */
    public int getImageID() {
        return imageID;
    }

    /**
     * Return the lost components
     * @return the number of lost components
     */
    public int getLostComponents() {
        return lostComponents;
    }

    /**
     * Return if the component is correctly assembled
     * @return true if it is correctly assembled, false otherwise
     */
    public boolean isCorrect() {
         return correct;
    }

    /**
     * Returns the number of reserved components
     * @return the number of reserved components
     */
    public int getNumberReservedComponents() {
        return reservedComponents.size();
    }

    /**
     * Method that adds a listener to the map of listeners of the ship board
     * @param nickname
     * @param client
     */
    public void addListener(String nickname, VirtualView client) {
        clients.put(nickname, client);
    }

    /**
     * Method invoked when the owner of the ship board picks a component from the table
     * @param component
     * @throws PickedComponentException
     */
    public void pickComponent(Component component) throws PickedComponentException {
        if(pickedComponent!=null){
            throw new PickedComponentException("Already one component");
        }
        else {
            this.pickedComponent = component;
        }
    }

    /**
     * Mathod that release he picked component, therefore returned so that it can be shown to the other players
     * @return the picked component
     * @throws PickedComponentException
     */
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

    /**
     * The picked component is added to the reserved components for the ship board
     * @return the picked component
     * @throws PickedComponentException
     * @throws ReservedComponentException
     */
    public Component reserveComponent() throws PickedComponentException, ReservedComponentException {
        if(pickedComponent==null){
            throw new PickedComponentException("No picked component");
        }
        else {
            if(reservedComponents.size() == 2){
                throw new ReservedComponentException("Too many reserved components");
            }
            else {
                pickedComponent.setOrientation();
                reservedComponents.add(pickedComponent);
                Component c = pickedComponent;
                pickedComponent = null;
                return c;
            }
        }
    }

    /**
     * Method invoked when a player picks a specific component among the ones reserved for its ship board
     * @param position
     * @return the picked reserved component
     * @throws ReservedComponentException
     * @throws PickedComponentException
     */
    public Component pickReservedComponent(int position) throws ReservedComponentException, PickedComponentException {
        if(position < 0 || position >= reservedComponents.size()){ //checks if the position is valid
            throw new ReservedComponentException("Invalid reserved component position");
        }
        else if(pickedComponent!=null){ //checks if the player has already picked a component
            throw new PickedComponentException("Already one component");
        }
        else {
            pickedComponent = reservedComponents.remove(position); //removes the component from the reserved components
            return pickedComponent;
        }
    }

    /**
     * Method that determines if a position on the ship board is adjacent to an assembled component, in order to determine if the position is available for component placement
     * @param x
     * @param y
     * @return true if the position is isolated, false otherwise
     */
    public boolean isolatedPosition(int x, int y){
        Component c;
        if(x>0){
            c = assembledComponents.get(x-1).get(y);
            if(c.isNotEmpty() && c.belongsToShip()){
                return false;
            }
        }
        if(y>0){
            c = assembledComponents.get(x).get(y-1);
            if(c.isNotEmpty() && c.belongsToShip()){
                return false;
            }
        }
        if(x < assembledComponents.size()-1){
            c = assembledComponents.get(x+1).get(y);
            if(c.isNotEmpty() && c.belongsToShip()){
                return false;
            }
        }
        if(y < assembledComponents.get(x).size()-1){
            c = assembledComponents.get(x).get(y+1);
            if(c.isNotEmpty() && c.belongsToShip()){
                return false;
            }
        }
        return true;
    }

    /**
     * Method that assembles the component picked by a player in the specified cell (x,y) of its ship board
     * @param x
     * @param y
     * @return the assembled component
     * @throws AssembledComponentException
     * @throws PickedComponentException
     */
    public Component assembleComponent(int x, int y) throws AssembledComponentException, PickedComponentException {
        Component c;
        if(!assembledComponents.get(x).get(y).belongsToShip()){
            throw new AssembledComponentException("Can't assemble component outside the ship");
        }
        if(assembledComponents.get(x).get(y).isNotEmpty()){
            throw new AssembledComponentException("Already assembled component");
        }
        if(isolatedPosition(x,y)){
            throw new AssembledComponentException("The assembled component can't be disconnected");
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

    /**
     * Method that rotates the picked component to the left
     * @throws PickedComponentException
     */
    public void rotatePickedComponent() throws PickedComponentException {
        if(pickedComponent==null){
            throw new PickedComponentException("No picked component");
        }
        else {
            pickedComponent.rotateLeft();
        }
    }

    /**
     * Method that removes an assembled component from the ship board
     * @param x
     * @param y
     * @throws AssembledComponentException
     */
    public void destroyComponent(int x, int y) throws AssembledComponentException {
        Component emptySpace = new Empty(0, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH)));
        if(!assembledComponents.get(x).get(y).isNotEmpty()){
            throw new AssembledComponentException("No component to be destroyed");
        }
        if(!assembledComponents.get(x).get(y).belongsToShip()){
            throw new AssembledComponentException("Can't destroy component outside the ship");
        }
        assembledComponents.get(x).set(y, emptySpace);
        lostComponents++;
        updateCorrectness();

        for(VirtualView view: clients.values()){
            try{view.updateDestroyedComponent(nickname, x, y);}
            catch(Exception e){System.out.println("Error during remote method invocation on client");}
        }
    }

    /**
     * Method that initializes a cabin with 2 human crew members
     * @param x
     * @param y
     * @throws AssembledComponentException
     * @throws FullCabinException
     */
    public void addCrew(int x, int y) throws AssembledComponentException, FullCabinException {
        assembledComponents.get(x).get(y).addCrew();
    }

    /**
     * Method that initializes a cabin with an alien of the specified type, checking whether there is life support for it
     * @param isPurple
     * @param x
     * @param y
     * @throws AssembledComponentException
     * @throws FullCabinException
     * @throws NoLifeSupportException
     */
    public void addAlien(boolean isPurple, int x, int y) throws AssembledComponentException, FullCabinException, NoLifeSupportException {
        if(x==2 && y==3){
            throw new InvalidPositionException("Cannot add alien in the initial cabin");
        }
        boolean lifeSupport = false;
        if(x!=0){
            if(assembledComponents.get(x-1).get(y).supportsAlien(isPurple)){
                lifeSupport = true;
            }
        }
        if(y!=0){
            if(assembledComponents.get(x).get(y-1).supportsAlien(isPurple)){
                lifeSupport = true;
            }
        }
        if(x!=4){
            if(assembledComponents.get(x+1).get(y).supportsAlien(isPurple)){
                lifeSupport = true;
            }
        }
        if(y!=6){
            if(assembledComponents.get(x).get(y+1).supportsAlien(isPurple)){
                lifeSupport = true;
            }
        }
        if(!lifeSupport){
            throw new NoLifeSupportException("Life support is missing");
        }
        assembledComponents.get(x).get(y).addAlien(isPurple);
    }

    /**
     * Method that invoked when the player wants to initialize a battery container with batteries
     * @param x
     * @param y
     * @return the number of batteries added
     * @throws AssembledComponentException
     * @throws NoBatteriesException
     */
    public int addBatteries(int x, int y) throws AssembledComponentException, NoBatteriesException{
        return assembledComponents.get(x).get(y).addBatteries();
    }

    /**
     * Method that determines if the ship board is correctly assembled
     */
    public void updateCorrectness(){
        boolean correctness = true; //assumes that the ship board is correctly assembled

        for (int i = 0; i < assembledComponents.size(); i++) { //iterates through the rows of the ship board
            for(int j = 0; j < assembledComponents.get(i).size(); j++){ //iterates through the columns of the ship board
                Component c = assembledComponents.get(i).get(j); //gets the component in the current position
                if(c.isNotEmpty() && c.belongsToShip()){ //if the component is not empty and belongs to the ship
                    if(!c.isWellOriented()){ //engines need to be well oriented
                        correctness = false;
                        break;
                    }
                    Component cNorth = null, cEast = null, cSouth = null, cWest = null; //adjacent components
                    if(i>0){ // checks the component above
                        Component c1 = assembledComponents.get(i-1).get(j); // gets the component above
                        if(!c.getNorthSide().compatibleWith(c1.getSouthSide()) && c1.isNotEmpty() && c1.belongsToShip()){
                            correctness = false;
                            break;
                        }
                        cNorth = assembledComponents.get(i-1).get(j);
                    }
                    if(i<assembledComponents.size()-1){ // checks the component below
                        Component c1 = assembledComponents.get(i+1).get(j);
                        if(!c.getSouthSide().compatibleWith(c1.getNorthSide()) && c1.isNotEmpty() && c1.belongsToShip()){
                            correctness = false;
                            break;
                        }
                        cSouth = assembledComponents.get(i+1).get(j);
                    }
                    if(j>0){ // checks the component on the left
                        Component c1 = assembledComponents.get(i).get(j-1);
                        if(!c.getWestSide().compatibleWith(c1.getEastSide()) && c1.isNotEmpty() && c1.belongsToShip()){
                            correctness = false;
                            break;
                        }
                        cWest = assembledComponents.get(i).get(j-1);
                    }
                    if(j<assembledComponents.get(i).size()-1){
                        Component c1 = assembledComponents.get(i).get(j+1);
                        if(!c.getEastSide().compatibleWith(c1.getWestSide()) && c1.isNotEmpty() && c1.belongsToShip()){
                            correctness = false;
                            break;
                        }
                        cEast = assembledComponents.get(i).get(j+1);
                    }
                    if(c.hasAdjacentPlacementConflict(cNorth, cEast, cSouth, cWest)){
                        correctness = false;
                        break;
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
    /**
     * Method that increases the number of lost components for the number of unused reserved components
     */
    public void loseReservedComponents(){
        lostComponents+=reservedComponents.size();
    }

    /**
     * Method that checks if the ship board has multiple regions (floating group of components)
     * @return true if the ship board has multiple regions, false otherwise
     */
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

    /**
     * Method that performs a depth-first search on the ship board to find all components that belong to the same ship
     * @param components
     * @param row
     * @param col
     * @param visited
     */
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
        //all explorable adjacent directions
        if(analyzedComponent.getNorthSide() != Connector.SMOOTH){
            dfs(components, row+1, col, visited);
        }
        if (analyzedComponent.getSouthSide() != Connector.SMOOTH) {
            dfs(components, row-1, col, visited);
        }
        if (analyzedComponent.getEastSide() != Connector.SMOOTH) {
            dfs(components, row, col+1, visited);
        }
        if (analyzedComponent.getWestSide() != Connector.SMOOTH) {
            dfs(components, row, col-1, visited);
        }
    }

    /**
     * Method that counts the number of exposed connectors of the ship board
     * @return the number of exposed connectors
     */
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
                        if(c.getSouthSide()!=Connector.SMOOTH && (!c1.isNotEmpty() || !c1.belongsToShip())){
                            exposedConnectors++;
                        }
                    }
                    if(j>0){
                        Component c1 = assembledComponents.get(i).get(j-1);
                        if(c.getWestSide()!=Connector.SMOOTH && (!c1.isNotEmpty() || !c1.belongsToShip())){
                            exposedConnectors++;
                        }
                    }
                    if(j<assembledComponents.get(i).size()-1){
                        Component c1 = assembledComponents.get(i).get(j+1);
                        if(c.getEastSide()!=Connector.SMOOTH && (!c1.isNotEmpty() || !c1.belongsToShip())){
                            exposedConnectors++;
                        }
                    }
                }
            }
        }
        return exposedConnectors;
    }

    /**
     * Method that removes a member (human or alien) from each cabin that is directly connected with another busy cabin
     */
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

            for(VirtualView view: clients.values()){
                try{view.updateCrewChange(nickname, hitCabin[0], hitCabin[1], -1);}
                catch(Exception e){System.out.println("Error during remote method invocation on client");}
            }
        }
    }

    /**
     * Method that determines whether a side of the ship board is protected by a shield and (if yes) activates it by using a battery
     * @param orientation
     * @return true if the ship board is protected, false otherwise
     */
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

    /**
     * Method that determines whether a row/column of a ship board is armed (has a cannon) in a specific direction; it can also activate a double cannon if specified and if necessary
     * @param activatedCannon
     * @param orientation
     * @param direction
     * @return true if the ship board is armed in the specified direction, false otherwise
     */
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

    /**
     * Method that determines whether the ship board exposes a smooth side in a specific direction
     * @param orientation
     * @param direction
     * @return
     */
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

    /**
     * Method that determines whether the shipBoard has all the cabins and battery containers full
     * @return
     */
    public boolean hasAllCabinsBatteriesFull(){
        for (List<Component> componentRow : assembledComponents) {
            for (Component component : componentRow) {
                if(component.belongsToShip() && component.isNotEmpty()){
                    if(!component.isFull()){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Method that destroys the first component of the ship from north, in the specified column
     * @param column
     */
    public void destroyNorth(int column){
        for(int i=0; i<assembledComponents.size(); i++){
            Component c = assembledComponents.get(i).get(column);
            if(c.isNotEmpty() && c.belongsToShip()){
                destroyComponent(i,column);
                break;
            }
        }
    }

    /**
     * Method that destroys the first component of the ship from south, in the specified column
     * @param column
     */
    public void destroySouth(int column){
        for(int i=assembledComponents.size()-1; i>=0; i--){
            Component c = assembledComponents.get(i).get(column);
            if(c.isNotEmpty() && c.belongsToShip()){
                destroyComponent(i,column);
                break;
            }
        }
    }

    /**
     * Method that destroys the first component of the ship from east, in the specified row
     * @param row
     */
    public void destroyEast(int row){
        for(int j=assembledComponents.get(row).size() - 1; j>=0; j--){
            Component c = assembledComponents.get(row).get(j);
            if(c.isNotEmpty() && c.belongsToShip()){
                destroyComponent(row,j);
                break;
            }
        }
    }

    /**
     * Method that destroys the first component of the ship from west, in the specified row
     * @param row
     */
    public void destroyWest(int row){
        for(int j=0; j< assembledComponents.get(row).size(); j++){
            Component c = assembledComponents.get(row).get(j);
            if(c.isNotEmpty() && c.belongsToShip()){
                destroyComponent(row,j);
                break;
            }
        }
    }

    /**
     * Method that verifies that in all the specified positions there are cabins that contain at least the specified number of crew
     * @param x
     * @param y
     * @param crewInEachCabin
     * @return true if all the specified cabins have enough crew, false otherwise
     */
    public boolean availableCabins(List<Integer> x, List<Integer> y, List<Integer> crewInEachCabin){
        Component c;
        for(int i=0; i<x.size(); i++){
            c = assembledComponents.get(x.get(i)).get(y.get(i));
            if(c.getNumberCrew() < crewInEachCabin.get(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method that verifies that in all the specified positions there are cargo holds that are not full
     * @param x
     * @param y
     * @return
     */
    public boolean availableCargoHolds(List<Integer> x, List<Integer> y){
        for(int i=0; i<x.size(); i++){
            if(x.get(i)==0 && y.get(i)==0){
                continue;
            }
            Component c = assembledComponents.get(x.get(i)).get(y.get(i));
            if(c.isFullOfGoods()){
                return false;
            }
        }
        return true;
    }

    /**
     * Method that removes the specified crew members from the specified cabins in the ship board
     * @param x
     * @param y
     * @param crewInEachCabin
     * @param numberCrewToRemove
     * @throws NoCrewException
     */
    public void removeCrewMembers(List<Integer> x, List<Integer> y, List<Integer> crewInEachCabin, int numberCrewToRemove) throws NoCrewException {
        int sumRemovedCrewMembers = crewInEachCabin.stream().mapToInt(Integer::intValue).sum();
        if(sumRemovedCrewMembers != numberCrewToRemove){
            throw new NoCrewException("Wrong number of crew members to remove");
        }
        if(!availableCabins(x, y, crewInEachCabin)){
            throw new NoCrewException("All specified cabins must have sufficient crew members");
        }
        for(int i=0; i<x.size(); i++){
            assembledComponents.get(x.get(i)).get(y.get(i)).removeCrew(crewInEachCabin.get(i));
            for(VirtualView view: clients.values()){
                try{view.updateCrewChange(nickname, x.get(i), y.get(i), -crewInEachCabin.get(i));}
                catch(Exception e){System.out.println("Error during remote method invocation on client");}
            }
        }
    }

    /**
     * Method that substitutes the cargo good at the given coordinates with the good given in input
     * @param cargo_row
     * @param cargo_col
     * @param good
     * @param posInCargo
     * @throws FullCargoHoldException
     * @throws UnsupportedCargoColorException
     */
    public void substituteGoods(int cargo_row, int cargo_col, Color good, int posInCargo) throws FullCargoHoldException, UnsupportedCargoColorException {
        assembledComponents.get(cargo_row).get(cargo_col).substituteGood(good, posInCargo);
    }

    /**
     *  Method that adds a set of goods in specific cargo holds of the player's ship board; discards the good if the specified coordinates are (0,0)
     * @param x
     * @param y
     * @param goods
     * @throws UnsupportedCargoColorException
     * @throws FullCargoHoldException
     */
    public void loadGoods(List<Integer> x, List<Integer> y, List<Color> goods) throws UnsupportedCargoColorException, FullCargoHoldException{
        if(!availableCargoHolds(x,y)){
            throw new NoCrewException("All specified cargo holds must have sufficient space");
        }
        for(int i=0; i<x.size(); i++){
            if(x.get(i) == 0 && y.get(i) == 0){
                continue;
            }

            assembledComponents.get(x.get(i)).get(y.get(i)).addGood(goods.get(i));

            for(VirtualView view: clients.values()){
                try{view.updateLoadedGood(nickname, x.get(i), y.get(i), goods.get(i));}
                catch(Exception e){System.out.println("Error during remote method invocation on client");}
            }
        }
    }

    /**
     * Method that invokes when a meteor/cannon shot hits the ship board
     * @param meteor
     * @param direction
     * @param activateShield
     * @param activateCannon
     */
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

    /**
     * Method invoked when a cannon shot hits the ship board
     * @param cannonFire
     * @param direction
     * @param activateShield
     */
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

    /**
     * Method that returns the number of crew members in the ship board
     * @return the number of crew members
     */
    public int getNumberCrew() {
        int numberCrew = 0;
        for (List<Component> componentRow : assembledComponents) {
            for (Component component : componentRow) {
                numberCrew += component.getNumberCrew();
            }
        }
        return numberCrew;
    }

    /**
     * Method that returns the number of batteries on the ship board
     * @return the number of batteries
     */
    public int getNumberBatteries() {
        int numberBatteries = 0;
        for (List<Component> componentRow : assembledComponents) {
            for (Component component : componentRow) {
                numberBatteries += component.getNumberBatteries();
            }
        }
        return numberBatteries;
    }

    /**
     * Method that removes a specified number of batteries from the ship board; if there aren't enough batteries, it removes all the batteries available
     * @param batteries
     * @throws NoBatteriesException
     */
    public void removeBatteries(int batteries) throws NoBatteriesException{
        int toRemove = batteries;
        int componentBatteries;
        for (int i = 0; i < assembledComponents.size(); i++) {
            List<Component> componentRow = assembledComponents.get(i);
            for (int j = 0; j < componentRow.size(); j++) {
                Component component = componentRow.get(j);
                componentBatteries = component.getNumberBatteries();
                if (componentBatteries > 0) {
                    if (componentBatteries >= toRemove) {
                        component.useBatteries(toRemove);
                        for(VirtualView view: clients.values()){
                            try{view.updateBatteries(nickname, i, j, -toRemove);}
                            catch(Exception e){System.out.println("Error during remote method invocation on client");}
                        }
                        toRemove = 0;
                        break;
                    } else {
                        component.useBatteries(componentBatteries);
                        for(VirtualView view: clients.values()){
                            try{view.updateBatteries(nickname, i, j, -componentBatteries);}
                            catch(Exception e){System.out.println("Error during remote method invocation on client");}
                        }
                        toRemove -= componentBatteries;
                    }
                }
            }
            if (toRemove == 0) {
                break;
            }
        }
    }

    /**
     * Method that returns the number of goods on the ship board
     * @return the number of goods
     */
    public int getNumberGoods() {
        int numberGoods = 0;
        for (List<Component> componentRow : assembledComponents) {
            for (Component component : componentRow) {
                numberGoods += component.getNumberGoods();
            }
        }
        return numberGoods;
    }

    /**
     * Method that returns the number of aliens on the ship board
     * @param purpleAliens
     * @return the number of aliens
     */
    public int getNumberAliens(boolean purpleAliens){
        int numberAliens = 0;
        for (List<Component> componentRow : assembledComponents) {
            for (Component component : componentRow) {
                if(component.hasAlien(purpleAliens)){
                    numberAliens++;
                }
            }
        }
        return numberAliens;
    }

    /**
     *  Method that removes numberGoods goods of a specific color from the ship board; if there aren't enough goods of that color, it returns the number of missing goods, otherwise it returns 0
     * @param color
     * @param numberGoods
     * @return the number of missing goods
     */
    public int removeSpecificGoods(Color color, int numberGoods){
        int toRemove = numberGoods;
        int componentGoods;
        for (int i = 0; i < assembledComponents.size(); i++) {
            List<Component> componentRow = assembledComponents.get(i);
            for (int j = 0; j < componentRow.size(); j++) {
                Component component = componentRow.get(j);
                componentGoods = component.getNumberGoods(color);
                if (componentGoods > 0) {
                    if (componentGoods >= toRemove) {
                        component.removeSpecificGoods(color, toRemove);

                        for (VirtualView view : clients.values()) {
                            try {view.updateRemovedGoods(nickname, i, j, color, toRemove);}
                            catch (Exception e) {System.out.println("Error during remote method invocation on client");}
                        }

                        toRemove = 0;
                        break;
                    } else {
                        component.removeSpecificGoods(color, componentGoods);

                        for (VirtualView view : clients.values()) {
                            try {view.updateRemovedGoods(nickname, i, j, color, componentGoods);}
                            catch (Exception e) {System.out.println("Error during remote method invocation on client");}
                        }

                        toRemove -= componentGoods;
                    }
                }
            }
            if (toRemove == 0) {
                break;
            }
        }
        return toRemove;
    }

    /**
     * Method that removes a specified number of goods from the ship board, removing them in order of color (red, yellow, green, blue) and then batteries if necessary
     * @param numberGoods
     */
    public void losePreciousGoods(int numberGoods){
        int toRemove = numberGoods;
        toRemove = removeSpecificGoods(Color.RED, toRemove);
        toRemove = removeSpecificGoods(Color.YELLOW, toRemove);
        toRemove = removeSpecificGoods(Color.GREEN, toRemove);
        toRemove = removeSpecificGoods(Color.BLUE, toRemove);
        if(toRemove > 0){
            removeBatteries(toRemove);
        }
    }

    /**
     * Method that returns the number of double engines on the ship board
     * @return the number of double engines
     */
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

    /**
     * Method that returns the number of single engines on the ship board
     * @return the number of single engines
     */
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

    /**
     * Method that returns the number of double cannons on the ship board
     * @return the number of double cannons
     */
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

    /**
     * Method that returns the number of single cannons on the ship board
     * @return the number of single cannons
     */
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

    /**
     * Method that returns the number of double cannons that point forward on the ship board
     * @return the number of forward double cannons
     */
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

    /**
     * Method that returns the number of single cannons that point forward on the ship board
     * @return the number of forward single cannons
     */
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

    /**
     * Method that returns the strength of the cannons on the ship board, removing the batteries used to activate double cannons
     * @param usedBatteries
     * @return the strength of the cannons
     * @throws NoBatteriesException
     */
    public double getCannonStrength(int usedBatteries) throws NoBatteriesException{
        int activatedDoubleCannons = Math.min(getNumberDoubleCannons(), usedBatteries);
        removeBatteries(activatedDoubleCannons);
        int forwardDoubleCannons = Math.min(getNumberForwardDoubleCannons(), activatedDoubleCannons);
        int lateralDoubleCannons = ((activatedDoubleCannons > forwardDoubleCannons) ? activatedDoubleCannons-forwardDoubleCannons : 0);
        int forwardSingleCannons = getNumberForwardSingleCannons();
        int lateralSingleCannons = getNumberSingleCannons() - forwardSingleCannons;
        int purpleAliens = getNumberAliens(true);
        double cannonStrength = lateralDoubleCannons + forwardDoubleCannons*2 + lateralSingleCannons*0.5 + forwardSingleCannons;
        if(cannonStrength > 0){
            return cannonStrength + 2*purpleAliens;
        }else{
            return cannonStrength;
        }
    }

    /**
     * Method that returns the engine strength of the ship board, removing the given batteries in order to activate double engines
     * @param usedBatteries
     * @return the engine strength
     * @throws NoBatteriesException
     */
    public int getEngineStrength(int usedBatteries) throws NoBatteriesException {
        int activatedDoubleEngines = Math.min(getNumberDoubleEngines(), usedBatteries);
        removeBatteries(activatedDoubleEngines);
        int brownAliens = getNumberAliens(false);
        int engineStrength = activatedDoubleEngines*2 + getNumberSingleEngines();
        if(engineStrength > 0){
            return engineStrength + 2*brownAliens;
        }else{
            return engineStrength;
        }
    }

    /**
     * Method that returns the overall price for all the goods carried by the player's ship
     * @return the overall price of the goods
     */
    public int getGoodsPrice(){
        int goodsPrice = 0;
        for(List<Component> componentRow : assembledComponents){
            for(Component component : componentRow){
                goodsPrice+=component.goodsPrice();
            }
        }
        return goodsPrice;
    }


}




