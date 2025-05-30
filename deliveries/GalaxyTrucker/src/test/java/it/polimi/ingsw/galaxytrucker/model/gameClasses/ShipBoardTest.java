package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.*;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.exceptions.*;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.CannonShot;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.Meteor;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipBoardTest {
    private ShipBoard shipBoard;
    private Component component1;
    private Component component2;
    private List<Connector> universalConnectorList;

    @BeforeEach
    void init(){
        shipBoard = new LevelTwoShipBoard("filippo", Color.RED); // shipboard of level two is used
        List<Connector> connectorList1 = new ArrayList<>();
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SMOOTH);
        List<Connector> connectorList2 = new ArrayList<>();
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SMOOTH);
        component1 = new Component(9, connectorList1);
        component2 = new Component(9, connectorList2);
        universalConnectorList = new ArrayList<>();
        universalConnectorList.add(Connector.UNIVERSAL);
        universalConnectorList.add(Connector.UNIVERSAL);
        universalConnectorList.add(Connector.UNIVERSAL);
        universalConnectorList.add(Connector.UNIVERSAL);
    }

    @Test
    void testShouldConstructShipBoardWithCorrespondingInitCabin(){
        ShipBoard shipBoardBlue = new LevelTwoShipBoard("filippo", Color.BLUE);
        assertEquals(Color.BLUE, shipBoardBlue.getColor());
        Cabin blueCabin = new Cabin(318, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        assertEquals(shipBoardBlue.assembledComponents.get(2).get(3), blueCabin);

        ShipBoard shipBoardGreen = new LevelTwoShipBoard("filippo", Color.GREEN);
        assertEquals(Color.GREEN, shipBoardGreen.getColor());
        Cabin greenCabin = new Cabin(319, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        assertEquals(shipBoardGreen.assembledComponents.get(2).get(3), greenCabin);

        ShipBoard shipBoardRed = new LevelTwoShipBoard("filippo", Color.RED);
        assertEquals(Color.RED, shipBoardRed.getColor());
        Cabin redCabin = new Cabin(320, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        assertEquals(shipBoardRed.assembledComponents.get(2).get(3), redCabin);

        ShipBoard shipBoardYellow = new LevelTwoShipBoard("filippo", Color.YELLOW);
        assertEquals(Color.YELLOW, shipBoardYellow.getColor());
        Cabin yellowCabin = new Cabin(321, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        assertEquals(shipBoardYellow.assembledComponents.get(2).get(3), yellowCabin);
    }

    @Test
    void testGetImageID(){
        assertEquals(2, shipBoard.getImageID()); // image of level two shipboard
    }

    @Test
    void testAddListener(){
        //create a virtual view client with same nick and color of the player in the game state
        ClientRMI clientRMI;
        String nick = "filippo";
        try {
            clientRMI = null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        shipBoard.addListener(nick, clientRMI);
        assertTrue(shipBoard.getClients().containsKey(nick));
        assertEquals(clientRMI, shipBoard.getClients().get(nick));
    }

    @Test
    void testPickAndReleaseComponent() {
        shipBoard.pickComponent(component1); //pick
        assertEquals(component1, shipBoard.getPickedComponent());
        Component releasedComponent = shipBoard.releaseComponent(); //release
        assertEquals(component1, releasedComponent);
        assertNull(shipBoard.getPickedComponent());
    }

    @Test
    void testShouldNotPickComponentIfAlreadyPicked(){
        shipBoard.pickComponent(component1);
        assertEquals(component1, shipBoard.getPickedComponent());
        assertThrows(PickedComponentException.class, () -> {shipBoard.pickComponent(component2);});
    }

    @Test
    void testShouldNotReleaseComponentIfNotPicked(){
        assertThrows(PickedComponentException.class, () -> {shipBoard.releaseComponent();});
    }

    @Test
    void testShouldNotReserveNullComponent(){
        assertThrows(PickedComponentException.class, () -> {shipBoard.reserveComponent();});
    }

    @Test
    void testShouldNotReserveComponentIfReservedAreFull(){
        List<Connector> connectorList3 = new ArrayList<>();
        connectorList3.add(Connector.SINGLE);
        connectorList3.add(Connector.SINGLE);
        connectorList3.add(Connector.SINGLE);
        connectorList3.add(Connector.SMOOTH);
        Component component3 = new Component(9, connectorList3);
        shipBoard.pickComponent(component1);
        shipBoard.reserveComponent();
        shipBoard.pickComponent(component2);
        shipBoard.reserveComponent();
        shipBoard.pickComponent(component3);
        assertThrows(ReservedComponentException.class, () -> shipBoard.reserveComponent());
    }

    @Test
    void testPickReservedComponent() {
        shipBoard.pickComponent(component1);
        shipBoard.reserveComponent();
        shipBoard.pickReservedComponent(0);
        assertEquals(component1, shipBoard.getPickedComponent());
    }

    @Test
    void testShouldNotPickReservedComponentIfInvalidPosition(){
        shipBoard.pickComponent(component1);
        shipBoard.reserveComponent();
        assertThrows(ReservedComponentException.class, () -> {shipBoard.pickReservedComponent(1);});
    }

    @Test
    void testShouldNotPickReservedComponentIfAlreadyPickedOne(){
        shipBoard.pickComponent(component1);
        shipBoard.reserveComponent();
        shipBoard.pickComponent(component2);
        assertThrows(PickedComponentException.class, () -> {shipBoard.pickReservedComponent(0);});
    }

    @Test
    void testAssembleComponent() {
        shipBoard.pickComponent(component1);
        shipBoard.assembleComponent(1, 3); //above the initial cabin
        assertEquals(component1, shipBoard.getAssembledComponent(1, 3));
    }

    @Test
    void testShouldNotAssembleOutside(){
        shipBoard.pickComponent(component1);
        assertThrows(AssembledComponentException.class, () -> {shipBoard.assembleComponent(0,0);});
    }

    @Test
    void testShouldNotAssembleOccupied(){
        shipBoard.pickComponent(component1);
        shipBoard.assembleComponent(1,3);
        shipBoard.pickComponent(component2);
        assertThrows(AssembledComponentException.class, () -> {shipBoard.assembleComponent(1,3);});
    }

    @Test
    void testShouldNotAssembleIfNotPickedComponent(){
        assertThrows(PickedComponentException.class, () -> {shipBoard.assembleComponent(1,3);});
    }

    @Test
    void testAssembleComponentGivenInParam(){
        Engine engineNorthUniversal = new Engine(false, 709, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.DOUBLE, Connector.SMOOTH, Connector.SMOOTH)));
        assertEquals(709, engineNorthUniversal.getImageID());
        shipBoard.assembleComponent(engineNorthUniversal,3,3);
        assertEquals(engineNorthUniversal, shipBoard.assembledComponents.get(3).get(3));
        assertTrue(shipBoard.isCorrect());
    }

    @Test
    void testDestroyComponent() {
        shipBoard.pickComponent(component1);
        shipBoard.assembleComponent(1, 3); //above the initial cabin
        assertFalse(shipBoard.isEmptyComponent(1,3));
        shipBoard.destroyComponent(1, 3);
        assertEquals(1, shipBoard.getLostComponents());
        assertTrue(shipBoard.isEmptyComponent(1, 3));
    }

    @Test
    void testAddAlien(){
        //add a cabin
        Cabin cabinForAlien = new Cabin(9, universalConnectorList);
        shipBoard.pickComponent(cabinForAlien);
        shipBoard.assembleComponent(1,3); // over the initial cabin
        //add life support
        LifeSupport lifeSupport = new LifeSupport(true, -1, universalConnectorList);
        shipBoard.pickComponent(lifeSupport);
        shipBoard.assembleComponent(1,4); // right to the cabinForAlien
        shipBoard.addAlien(true,1,3); //add alien
        assertTrue(shipBoard.getAssembledComponent(1,3).hasMembers());
        assertTrue(((Cabin)shipBoard.getAssembledComponent(1, 3)).hasPurpleAlien());
    }

    @Test
    void testShouldNotAddAlienSupportIsMissing(){
        Cabin cabinForAlien = new Cabin(9, universalConnectorList);
        shipBoard.pickComponent(cabinForAlien);
        shipBoard.assembleComponent(1,3);
        assertThrows(NoLifeSupportException.class, () -> shipBoard.addAlien(true, 1,3));
        assertEquals(0, shipBoard.getNumberCrew());
    }

    @Test
    void testShouldNotAddAlienInInitialCabin(){
        assertThrows(InvalidPositionException.class, () -> shipBoard.addAlien(true, 2,3));
        assertEquals(0, shipBoard.getNumberCrew());
    }

    @Test
    void testAddBatteries(){
        Battery battery = new Battery(true,-1, universalConnectorList);
        shipBoard.pickComponent(battery);
        shipBoard.assembleComponent(1,3);
        shipBoard.addBatteries(1,3);
        assertEquals(2, shipBoard.getNumberBatteries());
    }

    @Test
    void testUpdateCorrectnessFloatingComponents(){
        assertTrue(shipBoard.isCorrect());
        List<Connector> connectorList1 = new ArrayList<>();
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SINGLE);
        connectorList1.add(Connector.SMOOTH);
        component1 = new Component(9, connectorList1);
        shipBoard.pickComponent(component1);
        shipBoard.assembleComponent(1,1);
        assertFalse(shipBoard.isCorrect());
    }

    @Test
    void testShouldNotRotateComponentIfNotPicked(){
        assertThrows(PickedComponentException.class, () -> {shipBoard.rotatePickedComponent();});
    }

    @Test
    void testDestroyNorth(){
        assertEquals(320, shipBoard.getAssembledComponent(2,3).getImageID());
        shipBoard.destroyNorth(3);
        assertEquals(0, shipBoard.getAssembledComponent(2,3).getImageID());
    }

    @Test
    void testDestroySouth(){
        assertEquals(320, shipBoard.getAssembledComponent(2,3).getImageID());
        shipBoard.destroySouth(3);
        assertEquals(0, shipBoard.getAssembledComponent(2,3).getImageID());
    }

    @Test
    void testDestroyEast(){
        assertEquals(320, shipBoard.getAssembledComponent(2,3).getImageID());
        shipBoard.destroyEast(2);
        assertEquals(0, shipBoard.getAssembledComponent(2,3).getImageID());
    }

    @Test
    void testDestroyWest(){
        assertEquals(320, shipBoard.getAssembledComponent(2,3).getImageID());
        shipBoard.destroyWest(2);
        assertEquals(0, shipBoard.getAssembledComponent(2,3).getImageID());
    }

    @Test
    void testShouldNotDestroyComponentIfEmpty(){
        assertThrows(AssembledComponentException.class, () -> {shipBoard.destroyComponent(1,3);});
    }

    @Test
    void testLoseReservedComponents(){
        shipBoard.pickComponent(component1);
        shipBoard.reserveComponent();
        assertEquals(0, shipBoard.getLostComponents());
        shipBoard.loseReservedComponents();
        assertEquals(1, shipBoard.getLostComponents());
    }

    @Test
    void testSmoothSide() {
        Component shield = new Shield(44, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        shipBoard.pickComponent(shield);
        shipBoard.rotatePickedComponent();
        shipBoard.rotatePickedComponent();
        shipBoard.assembleComponent(1, 3);
        Component battery = new Battery(true, 45, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        shipBoard.pickComponent(battery);
        shipBoard.assembleComponent(2,2);
        boolean smooth = shipBoard.smoothSide(Orientation.WEST, 1);
        assertTrue(smooth);
    }

    @Test
    void testHasAllCabinsBatteriesFull(){
        // add cabin
        Cabin cabin = new Cabin(9, universalConnectorList);
        shipBoard.pickComponent(cabin);
        shipBoard.assembleComponent(1,3);
        // add battery container
        Battery battery = new Battery(true,9, universalConnectorList);
        shipBoard.pickComponent(battery);
        shipBoard.assembleComponent(1,4);
        //fill
        shipBoard.addCrew(2,3);
        shipBoard.addCrew(1,3);
        shipBoard.addBatteries(1,4);
        assertTrue(shipBoard.hasAllCabinsBatteriesFull());
    }

    @Test
    void testCountExposedConnectors(){
        Component struct = new Structural(101, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component engine = new Engine(false, 101, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.SMOOTH, Connector.SMOOTH, Connector.SMOOTH)));
        shipBoard.assembleComponent(struct, 1,3);
        shipBoard.assembleComponent(struct, 1,2);
        shipBoard.assembleComponent(struct, 1,4);
        shipBoard.assembleComponent(struct, 1,1);
        shipBoard.assembleComponent(struct, 1,5);
        shipBoard.assembleComponent(engine, 2,1);
        shipBoard.assembleComponent(engine, 2,5);
        assertEquals(12, shipBoard.countExposedConnectors()); // exposed connectors of initial cabin
    }

    @Test
    void testEpidemicEffect(){
        List<Connector> universalConnectorList = new ArrayList<>();
        universalConnectorList.add(Connector.UNIVERSAL);
        universalConnectorList.add(Connector.UNIVERSAL);
        universalConnectorList.add(Connector.UNIVERSAL);
        universalConnectorList.add(Connector.UNIVERSAL);
        Component cabin = new Cabin(0, universalConnectorList);
        shipBoard.pickComponent(cabin);
        shipBoard.assembleComponent(1, 3); // assemble cabin near the initial cabin
        shipBoard.addCrew(2,3);
        shipBoard.addCrew(1,3);
        assertEquals(4, shipBoard.getNumberCrew());
        shipBoard.epidemicEffect();
        assertEquals(2, shipBoard.getNumberCrew());
    }

    @Test
    void testProtectedShipBoardWithoutShields(){
        Orientation orientationN = Orientation.NORTH;
        assertFalse(shipBoard.protectedShipBoard(orientationN));
    }

    @Test
    void testProtectedShipBoardWithShields(){
        Orientation orientationN = Orientation.NORTH;
        Shield shieldN = new Shield(901, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SINGLE, Connector.UNIVERSAL, Connector.SINGLE)));
        shipBoard.pickComponent(shieldN);
        shipBoard.assembleComponent(1,3); //above the init cabin
        assertTrue(shipBoard.protectedShipBoard(orientationN));
    }

    @Test
    void testArmedShipBoard(){
        Orientation orientationN = Orientation.NORTH;
        assertFalse(shipBoard.armedShipBoard(false, orientationN, 0));

        Cannon cannonDouble = new Cannon(true, 428,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SINGLE, Connector.UNIVERSAL, Connector.SMOOTH)));
        shipBoard.pickComponent(cannonDouble);
        shipBoard.assembleComponent(2,2); //left to the init cabin
        assertTrue(shipBoard.armedShipBoard(true, orientationN, 2));

        Cannon cannonSingle = new Cannon(false, 401,  new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SMOOTH, Connector.SINGLE, Connector.SMOOTH)));
        shipBoard.pickComponent(cannonSingle);
        shipBoard.assembleComponent(1,3); //above the init cabin
        assertTrue(shipBoard.armedShipBoard(false, orientationN, 3));
    }

    @Test
    void testRemoveCrewMembers(){
        shipBoard.addCrew(2,3);
        assertEquals(2, shipBoard.getNumberCrew());
        //remove one crew member from init cabin
        shipBoard.removeCrewMembers(List.of(2), List.of(3), List.of(1), 1);
        assertEquals(1, shipBoard.getNumberCrew());
    }

    @Test
    void testSubstituteGoods(){
        CargoHold cargoHold = new CargoHold(true, -1, universalConnectorList);
        shipBoard.pickComponent(cargoHold);
        shipBoard.assembleComponent(2,2);
        CargoSpecial cargoSpecial = new CargoSpecial(true, -1, universalConnectorList);
        shipBoard.pickComponent(cargoSpecial);
        shipBoard.assembleComponent(2,1);
        //cargo hold at the end should have blue and yellow
        shipBoard.substituteGoods(2,2,Color.BLUE,0);
        shipBoard.substituteGoods(2,2,Color.BLUE,1);
        shipBoard.substituteGoods(2,2,Color.YELLOW,1);
        assertEquals(Color.BLUE,shipBoard.getAssembledComponent(2,2).getGoods().get(0));
        assertEquals(Color.YELLOW,shipBoard.getAssembledComponent(2,2).getGoods().get(1));
        //cargo special at the end should have 2 reds
        shipBoard.substituteGoods(2,1,Color.RED,0);
        shipBoard.substituteGoods(2,1,Color.BLUE,1);
        shipBoard.substituteGoods(2,1,Color.RED,1);
        assertEquals(Color.RED, shipBoard.getAssembledComponent(2,1).getGoods().get(0));
        assertEquals(Color.RED, shipBoard.getAssembledComponent(2,1).getGoods().get(1));
    }

    @Test
    void testMeteorAttack(){
        Meteor meteor = new Meteor(false, Orientation.SOUTH);
        shipBoard.meteorAttack(meteor, 3, false, false); //destroys init cabin
        assertEquals(1, shipBoard.getLostComponents());
        assertFalse(shipBoard.getAssembledComponent(2,3).isNotEmpty());
    }

    @Test
    void testCannonFireAttack(){
        CannonShot cannonShot = new CannonShot(false, Orientation.EAST);
        shipBoard.cannonFireAttack(cannonShot, 2, false); //destroys init cabin
        assertEquals(1, shipBoard.getLostComponents());
        assertFalse(shipBoard.getAssembledComponent(2,3).isNotEmpty());
    }

    @Test
    void testGetNumberGoods(){
        CargoHold cargoHold = new CargoHold(true, -1, universalConnectorList);
        shipBoard.pickComponent(cargoHold);
        shipBoard.assembleComponent(2,2);
        //cargo hold has blue and yellow
        shipBoard.substituteGoods(2,2,Color.BLUE,0);
        assertEquals(1, shipBoard.getNumberGoods());
        shipBoard.substituteGoods(2,2,Color.YELLOW,1);
        assertEquals(2, shipBoard.getNumberGoods());
    }

    @Test
    void testRemoveSpecificGoods(){
        CargoHold cargoHold = new CargoHold(true, -1, universalConnectorList);
        shipBoard.pickComponent(cargoHold);
        shipBoard.assembleComponent(2,2);
        shipBoard.substituteGoods(2,2,Color.BLUE,0);
        assertEquals(1, shipBoard.getNumberGoods());
        assertEquals(1, shipBoard.removeSpecificGoods(Color.RED, 1)); //one red is missing
        assertEquals(0, shipBoard.removeSpecificGoods(Color.BLUE, 1)); //one blue is removed
        assertEquals(0, shipBoard.getNumberGoods());
    }

    @Test
    void testLosePreciousGoods(){
        CargoHold cargoHold = new CargoHold(true, -1, universalConnectorList);
        shipBoard.pickComponent(cargoHold);
        shipBoard.assembleComponent(2,2);
        shipBoard.substituteGoods(2,2,Color.BLUE,0);
        shipBoard.substituteGoods(2,2,Color.YELLOW,1);
        shipBoard.losePreciousGoods(1); //yellow must be lost
        assertEquals(1, shipBoard.getNumberGoods());
        assertEquals(Color.BLUE,shipBoard.getAssembledComponent(2,2).getGoods().getFirst());
    }

    @Test
    void testGetNumberDoubleEngines(){
        Engine dEngine1 = new Engine(true, -1, universalConnectorList);
        shipBoard.pickComponent(dEngine1);
        shipBoard.assembleComponent(2,2);
        Engine dEngine2 = new Engine(true, -1, universalConnectorList);
        shipBoard.pickComponent(dEngine2);
        shipBoard.assembleComponent(2,4);
        Engine engine = new Engine(false, -1, universalConnectorList);
        shipBoard.pickComponent(engine);
        shipBoard.assembleComponent(3,3);
        assertEquals(2, shipBoard.getNumberDoubleEngines());
    }

    @Test
    void testGetNumberSingleEngines(){
        Engine dEngine = new Engine(true, -1, universalConnectorList);
        shipBoard.pickComponent(dEngine);
        shipBoard.assembleComponent(2,2);
        Engine engine1 = new Engine(false, -1, universalConnectorList);
        shipBoard.pickComponent(engine1);
        shipBoard.assembleComponent(2,4);
        Engine engine2 = new Engine(false, -1, universalConnectorList);
        shipBoard.pickComponent(engine2);
        shipBoard.assembleComponent(3,3);
        assertEquals(2, shipBoard.getNumberSingleEngines());
    }

    @Test
    void testGetNumberDoubleCannons(){
        Cannon dCannon1 = new Cannon(true, -1, universalConnectorList);
        shipBoard.pickComponent(dCannon1);
        shipBoard.assembleComponent(2,2);
        Cannon dCannon2 = new Cannon(true, -1, universalConnectorList);
        shipBoard.pickComponent(dCannon2);
        shipBoard.assembleComponent(2,4);
        Cannon cannon = new Cannon(false, -1, universalConnectorList);
        shipBoard.pickComponent(cannon);
        shipBoard.assembleComponent(1,3);
        assertEquals(2, shipBoard.getNumberDoubleCannons());
    }

    @Test
    void testGetNumberSingleCannons(){
        Cannon dCannon1 = new Cannon(true, -1, universalConnectorList);
        shipBoard.pickComponent(dCannon1);
        shipBoard.assembleComponent(2,2);
        Cannon cannon1 = new Cannon(false, -1, universalConnectorList);
        shipBoard.pickComponent(cannon1);
        shipBoard.assembleComponent(2,4);
        Cannon cannon2 = new Cannon(false, -1, universalConnectorList);
        shipBoard.pickComponent(cannon2);
        shipBoard.assembleComponent(1,3);
        assertEquals(2, shipBoard.getNumberSingleCannons());
    }

    @Test
    void testGetNumberForwardDoubleCannons(){
        //shipboard has one double cannon forward and the other horizontal
        Cannon dCannon1 = new Cannon(true, -1, universalConnectorList);
        shipBoard.pickComponent(dCannon1);
        shipBoard.assembleComponent(2,2);
        Cannon dCannon2 = new Cannon(true, -1, universalConnectorList);
        shipBoard.pickComponent(dCannon2);
        shipBoard.rotatePickedComponent();
        shipBoard.assembleComponent(2,4);
        assertEquals(1, shipBoard.getNumberForwardDoubleCannons());
    }

    @Test
    void testGetNumberForwardSingleCannons(){
        //shipboard has one single cannon forward and the other horizontal
        Cannon cannon1 = new Cannon(false, -1, universalConnectorList);
        shipBoard.pickComponent(cannon1);
        shipBoard.assembleComponent(2,2);
        Cannon cannon2 = new Cannon(false, -1, universalConnectorList);
        shipBoard.pickComponent(cannon2);
        shipBoard.rotatePickedComponent();
        shipBoard.assembleComponent(2,4);
        assertEquals(1, shipBoard.getNumberForwardSingleCannons());
    }

    @Test
    void testGetCannonStrength(){
        //shipboard has one double cannon forward and a single cannon horizontal
        Cannon dCannon = new Cannon(true, -1, universalConnectorList);
        shipBoard.pickComponent(dCannon);
        shipBoard.assembleComponent(2,2);
        Cannon cannon = new Cannon(false, -1, universalConnectorList);
        shipBoard.pickComponent(cannon);
        shipBoard.rotatePickedComponent();
        shipBoard.assembleComponent(2,4);
        assertEquals(0.5, shipBoard.getCannonStrength(0));
    }

    @Test
    void testGetEngineStrength(){
        //shipboard has one double engine and one single engine
        Engine dEngine = new Engine(true, -1, universalConnectorList);
        shipBoard.pickComponent(dEngine);
        shipBoard.assembleComponent(2,2);
        Engine engine = new Engine(false, -1, universalConnectorList);
        shipBoard.pickComponent(engine);
        shipBoard.assembleComponent(2,4);
        assertEquals(1, shipBoard.getEngineStrength(0));
    }

    @Test
    void testGetGoodsPrice(){
        //shipboard has 1 blue, 2 yellow and 1 red
        CargoHold cargoHold = new CargoHold(false, -1, universalConnectorList);
        shipBoard.pickComponent(cargoHold);
        shipBoard.assembleComponent(2,2);
        CargoSpecial cargoSpecial = new CargoSpecial(true, -1, universalConnectorList);
        shipBoard.pickComponent(cargoSpecial);
        shipBoard.assembleComponent(2,1);
        shipBoard.substituteGoods(2,2,Color.BLUE,0);
        shipBoard.substituteGoods(2,2,Color.YELLOW,1);
        shipBoard.substituteGoods(2,2,Color.YELLOW,2);
        shipBoard.substituteGoods(2,1,Color.RED,0);
        assertEquals(11, shipBoard.getGoodsPrice());
    }

    @Test
    void testLoadGoods(){
        Component cargo1 = new CargoHold(false, 408, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component cargo2 = new CargoHold(false, 408, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component cargo3 = new CargoHold(false, 408, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component cargo4 = new CargoHold(false, 408, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));

        shipBoard.assembleComponent(cargo1, 2, 2);
        shipBoard.assembleComponent(cargo2, 2, 4);
        shipBoard.assembleComponent(cargo3, 1, 3);
        shipBoard.assembleComponent(cargo4, 3, 3);
        List<Integer> x = new ArrayList<>(Arrays.asList(2,2,1,0));
        List<Integer> y = new ArrayList<>(Arrays.asList(2,4,3,0));
        List<Color> planetOneGoods = new ArrayList<>();
        planetOneGoods.add(Color.YELLOW);
        planetOneGoods.add(Color.GREEN);
        planetOneGoods.add(Color.BLUE);
        planetOneGoods.add(Color.BLUE);
        shipBoard.loadGoods(x,y, planetOneGoods);

        assertEquals(Color.YELLOW, shipBoard.getAssembledComponent(2,2).getGoods().get(0));
        assertEquals(Color.GREEN, shipBoard.getAssembledComponent(2,4).getGoods().get(0));
        assertEquals(Color.BLUE, shipBoard.getAssembledComponent(1,3).getGoods().get(0));
        assertEquals(0, shipBoard.getAssembledComponent(3,3).getGoods().size());

    }
}