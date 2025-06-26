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

/**
 * Test class for ShipBoard functionality in the Galaxy Trucker game.
 * Tests ship construction, component management, combat mechanics, and game rule validation.
 */
class ShipBoardTest {
    private ShipBoard shipBoard;
    private Component component1;
    private Component component2;
    private List<Connector> universalConnectorList;
    private List<Connector> universalCannonConnectorList;

    /**
     * Initializes test fixtures before each test.
     * Sets up a level two shipboard, test components, and connector lists.
     */
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
        universalCannonConnectorList = new ArrayList<>();
        universalCannonConnectorList.add(Connector.SMOOTH);
        universalCannonConnectorList.add(Connector.UNIVERSAL);
        universalCannonConnectorList.add(Connector.UNIVERSAL);
        universalCannonConnectorList.add(Connector.UNIVERSAL);
    }

    /**
     * Tests that shipboards are constructed with the correct color-specific initial cabins.
     * Verifies that each color (BLUE, GREEN, RED, YELLOW) gets the proper cabin with correct image ID.
     */
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

    /**
     * Tests that the shipboard returns the correct image ID for level two shipboards.
     */
    @Test
    void testGetImageID(){
        assertEquals(2, shipBoard.getImageID()); // image of level two shipboard
    }

    /**
     * Tests adding a client listener to the shipboard.
     * Verifies that the client is properly stored and can be retrieved.
     */
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

    /**
     * Tests the pick and release component functionality.
     * Verifies that components can be picked up and released properly.
     */
    @Test
    void testPickAndReleaseComponent() {
        shipBoard.pickComponent(component1); //pick
        assertEquals(component1, shipBoard.getPickedComponent());
        Component releasedComponent = shipBoard.releaseComponent(); //release
        assertEquals(component1, releasedComponent);
        assertNull(shipBoard.getPickedComponent());
    }

    /**
     * Tests that picking a component when one is already picked throws an exception.
     */
    @Test
    void testShouldNotPickComponentIfAlreadyPicked(){
        shipBoard.pickComponent(component1);
        assertEquals(component1, shipBoard.getPickedComponent());
        assertThrows(PickedComponentException.class, () -> {shipBoard.pickComponent(component2);});
    }

    /**
     * Tests that releasing a component when none is picked throws an exception.
     */
    @Test
    void testShouldNotReleaseComponentIfNotPicked(){
        assertThrows(PickedComponentException.class, () -> {shipBoard.releaseComponent();});
    }

    /**
     * Tests that reserving a null component throws an exception.
     */
    @Test
    void testShouldNotReserveNullComponent(){
        assertThrows(PickedComponentException.class, () -> {shipBoard.reserveComponent();});
    }

    /**
     * Tests that reserving components when the reserved slots are full throws an exception.
     * Verifies the 2-component reservation limit.
     */
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

    /**
     * Tests picking a reserved component from the reservation area.
     */
    @Test
    void testPickReservedComponent() {
        shipBoard.pickComponent(component1);
        shipBoard.reserveComponent();
        shipBoard.pickReservedComponent(0);
        assertEquals(component1, shipBoard.getPickedComponent());
    }

    /**
     * Tests that picking a reserved component from an invalid position throws an exception.
     */
    @Test
    void testShouldNotPickReservedComponentIfInvalidPosition(){
        shipBoard.pickComponent(component1);
        shipBoard.reserveComponent();
        assertThrows(ReservedComponentException.class, () -> {shipBoard.pickReservedComponent(1);});
    }

    /**
     * Tests that picking a reserved component when already holding one throws an exception.
     */
    @Test
    void testShouldNotPickReservedComponentIfAlreadyPickedOne(){
        shipBoard.pickComponent(component1);
        shipBoard.reserveComponent();
        shipBoard.pickComponent(component2);
        assertThrows(PickedComponentException.class, () -> {shipBoard.pickReservedComponent(0);});
    }

    /**
     * Tests assembling a component on the shipboard.
     * Verifies that the component is placed at the correct position.
     */
    @Test
    void testAssembleComponent() {
        shipBoard.pickComponent(component1);
        shipBoard.assembleComponent(1, 3); //above the initial cabin
        assertEquals(component1, shipBoard.getAssembledComponent(1, 3));
    }

    /**
     * Tests that a cannon violates rule constraints when placed incorrectly.
     * Cannons should not be placed below the initial cabin.
     */
    @Test
    void testAssembleComponentCannonViolatesRuleConstraint(){
        Cannon c = new Cannon(false, -1, universalCannonConnectorList);
        shipBoard.pickComponent(c);
        shipBoard.assembleComponent(3, 3);
        assertEquals(c, shipBoard.getAssembledComponent(3, 3)); // cannon must be assembled under init cabin
        assertFalse(shipBoard.isCorrect()); // then ship board should be incorrect
    }

    /**
     * Tests that a cannon follows rule constraints when placed correctly.
     * Cannons should be placed above the initial cabin.
     */
    @Test
    void testAssembleComponentCannonFollowRuleConstraint(){
        Cannon c = new Cannon(false, -1, universalCannonConnectorList);
        shipBoard.pickComponent(c);
        shipBoard.assembleComponent(1, 3);
        assertEquals(c, shipBoard.getAssembledComponent(1, 3)); // cannon must be assembled above init cabin
        assertTrue(shipBoard.isCorrect()); // then ship board should be correct
    }

    /**
     * Tests that assembling a component outside the board boundaries throws an exception.
     */
    @Test
    void testShouldNotAssembleOutside(){
        shipBoard.pickComponent(component1);
        assertThrows(AssembledComponentException.class, () -> {shipBoard.assembleComponent(0,0);});
    }

    /**
     * Tests that assembling a component on an occupied space throws an exception.
     */
    @Test
    void testShouldNotAssembleOccupied(){
        shipBoard.pickComponent(component1);
        shipBoard.assembleComponent(1,3);
        shipBoard.pickComponent(component2);
        assertThrows(AssembledComponentException.class, () -> {shipBoard.assembleComponent(1,3);});
    }

    /**
     * Tests that assembling without a picked component throws an exception.
     */
    @Test
    void testShouldNotAssembleIfNotPickedComponent(){
        assertThrows(PickedComponentException.class, () -> {shipBoard.assembleComponent(1,3);});
    }

    /**
     * Tests assembling a component by directly passing it as a parameter.
     * Verifies that the engine is placed correctly and the ship remains valid.
     */
    @Test
    void testAssembleComponentGivenInParam(){
        Engine engineNorthUniversal = new Engine(false, 709, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.DOUBLE, Connector.SMOOTH, Connector.SMOOTH)));
        assertEquals(709, engineNorthUniversal.getImageID());
        shipBoard.assembleComponent(engineNorthUniversal,3,3);
        assertEquals(engineNorthUniversal, shipBoard.assembledComponents.get(3).get(3));
        assertTrue(shipBoard.isCorrect());
    }

    /**
     * Tests destroying a component from the shipboard.
     * Verifies that the component is removed and lost component count increases.
     */
    @Test
    void testDestroyComponent() {
        shipBoard.pickComponent(component1);
        shipBoard.assembleComponent(1, 3); //above the initial cabin
        assertFalse(shipBoard.isEmptyComponent(1,3));
        shipBoard.destroyComponent(1, 3);
        assertEquals(1, shipBoard.getLostComponents());
        assertTrue(shipBoard.isEmptyComponent(1, 3));
    }

    /**
     * Tests adding an alien crew member to a cabin.
     * Verifies that life support is required and the alien is properly added.
     */
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
        assertTrue(((Cabin)shipBoard.getAssembledComponent(1, 3)).hasAlien(true));
    }

    /**
     * Tests that adding an alien without life support throws an exception.
     */
    @Test
    void testShouldNotAddAlienSupportIsMissing(){
        Cabin cabinForAlien = new Cabin(9, universalConnectorList);
        shipBoard.pickComponent(cabinForAlien);
        shipBoard.assembleComponent(1,3);
        assertThrows(NoLifeSupportException.class, () -> shipBoard.addAlien(true, 1,3));
        assertEquals(0, shipBoard.getNumberCrew());
    }

    /**
     * Tests that adding an alien to the initial cabin throws an exception.
     */
    @Test
    void testShouldNotAddAlienInInitialCabin(){
        assertThrows(InvalidPositionException.class, () -> shipBoard.addAlien(true, 2,3));
        assertEquals(0, shipBoard.getNumberCrew());
    }

    /**
     * Tests adding batteries to a battery component.
     * Verifies that the battery count increases correctly.
     */
    @Test
    void testAddBatteries(){
        Battery battery = new Battery(true,-1, universalConnectorList);
        shipBoard.pickComponent(battery);
        shipBoard.assembleComponent(1,3);
        shipBoard.addBatteries(1,3);
        assertEquals(2, shipBoard.getNumberBatteries());
    }

    /**
     * Tests that rotating a component without picking one first throw an exception.
     */
    @Test
    void testShouldNotRotateComponentIfNotPicked(){
        assertThrows(PickedComponentException.class, () -> {shipBoard.rotatePickedComponent();});
    }

    /**
     * Tests destroying components from the north direction.
     * Verifies that the component is destroyed and its image ID is reset.
     */
    @Test
    void testDestroyNorth(){
        assertEquals(320, shipBoard.getAssembledComponent(2,3).getImageID());
        shipBoard.destroyNorth(3);
        assertEquals(0, shipBoard.getAssembledComponent(2,3).getImageID());
    }

    /**
     * Tests destroying components from the south direction.
     * Verifies that the component is destroyed and its image ID is reset.
     */
    @Test
    void testDestroySouth(){
        assertEquals(320, shipBoard.getAssembledComponent(2,3).getImageID());
        shipBoard.destroySouth(3);
        assertEquals(0, shipBoard.getAssembledComponent(2,3).getImageID());
    }

    /**
     * Tests destroying components from the east direction.
     * Verifies that the component is destroyed and its image ID is reset.
     */
    @Test
    void testDestroyEast(){
        assertEquals(320, shipBoard.getAssembledComponent(2,3).getImageID());
        shipBoard.destroyEast(2);
        assertEquals(0, shipBoard.getAssembledComponent(2,3).getImageID());
    }

    /**
     * Tests destroying components from the west direction.
     * Verifies that the component is destroyed and its image ID is reset.
     */
    @Test
    void testDestroyWest(){
        assertEquals(320, shipBoard.getAssembledComponent(2,3).getImageID());
        shipBoard.destroyWest(2);
        assertEquals(0, shipBoard.getAssembledComponent(2,3).getImageID());
    }

    /**
     * Tests that destroying an empty component slot throws an exception.
     */
    @Test
    void testShouldNotDestroyComponentIfEmpty(){
        assertThrows(AssembledComponentException.class, () -> {shipBoard.destroyComponent(1,3);});
    }

    /**
     * Tests losing all reserved components.
     * Verifies that the lost component count increases appropriately.
     */
    @Test
    void testLoseReservedComponents(){
        shipBoard.pickComponent(component1);
        shipBoard.reserveComponent();
        assertEquals(0, shipBoard.getLostComponents());
        shipBoard.loseReservedComponents();
        assertEquals(1, shipBoard.getLostComponents());
    }

    /**
     * Tests checking for smooth connectors on adjacent components.
     * Verifies that smooth-sided components connect properly.
     */
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

    /**
     * Tests checking if all cabins and batteries are at full capacity.
     * Verifies the completion status of ship resources.
     */
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

    /**
     * Tests counting exposed connectors on the shipboard.
     * Verifies that unconnected component sides are properly counted.
     */
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

    /**
     * Tests the epidemic effect on crew members.
     * Verifies that crew count is reduced by half during epidemic events.
     */
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

    /**
     * Tests ship protection without shields.
     * Verifies that unprotected ships return false for protection checks.
     */
    @Test
    void testProtectedShipBoardWithoutShields(){
        Orientation orientationN = Orientation.NORTH;
        assertFalse(shipBoard.protectedShipBoard(orientationN));
    }

    /**
     * Tests ship protection with shields.
     * Verifies that ships with shields provide directional protection.
     */
    @Test
    void testProtectedShipBoardWithShields(){
        Orientation orientationN = Orientation.NORTH;
        Shield shieldN = new Shield(901, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.SINGLE, Connector.UNIVERSAL, Connector.SINGLE)));
        shipBoard.pickComponent(shieldN);
        shipBoard.assembleComponent(1,3); //above the init cabin
        assertTrue(shipBoard.protectedShipBoard(orientationN));
    }

    /**
     * Tests ship armament capabilities.
     * Verifies that ships with cannons can engage in combat from specific directions.
     */
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

    /**
     * Tests removing specific crew members from cabins.
     * Verifies that crew can be removed from specific positions.
     */
    @Test
    void testRemoveCrewMembers(){
        shipBoard.addCrew(2,3);
        assertEquals(2, shipBoard.getNumberCrew());
        //remove one crew member from init cabin
        shipBoard.removeCrewMembers(List.of(2), List.of(3), List.of(1), 1);
        assertEquals(1, shipBoard.getNumberCrew());
    }

    /**
     * Tests substituting goods in cargo holds.
     * Verifies that goods can be replaced in cargo containers.
     */
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

    /**
     * Tests meteor attack mechanics.
     * Verifies that meteor impacts destroy components and increase lost component count.
     */
    @Test
    void testMeteorAttack(){
        Meteor meteor = new Meteor(false, Orientation.SOUTH);
        shipBoard.meteorAttack(meteor, 3, false, false); //destroys init cabin
        assertEquals(1, shipBoard.getLostComponents());
        assertFalse(shipBoard.getAssembledComponent(2,3).isNotEmpty());
    }

    /**
     * Tests cannon fire attack mechanics.
     * Verifies that cannon shots destroy components and increase lost component count.
     */
    @Test
    void testCannonFireAttack(){
        CannonShot cannonShot = new CannonShot(false, Orientation.EAST);
        shipBoard.cannonFireAttack(cannonShot, 2, false); //destroys init cabin
        assertEquals(1, shipBoard.getLostComponents());
        assertFalse(shipBoard.getAssembledComponent(2,3).isNotEmpty());
    }

    /**
     * Tests counting the total number of goods on the ship.
     * Verifies that goods in cargo holds are properly counted.
     */
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

    /**
     * Tests removing specific colored goods from cargo holds.
     * Verifies that goods are removed and missing goods are properly tracked.
     */
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

    /**
     * Tests losing precious goods (highest value goods lost first).
     * Verifies that yellow goods are prioritized for removal.
     */
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

    /**
     * Tests counting the number of double engines on the ship.
     * Verifies that double engines are properly distinguished from single engines.
     */
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

    /**
     * Tests counting the number of single engines on the ship.
     * Verifies that single engines are properly distinguished from double engines.
     */
    @Test
    void testGetNumberSingleEngines() {
        Engine dEngine = new Engine(true, -1, universalConnectorList);
        shipBoard.pickComponent(dEngine);
        shipBoard.assembleComponent(2, 2);
        Engine engine1;
    }

    /**
     * Test method to verify the counting of double cannons on the ship board.
     * This test creates two double cannons and one single cannon, then verifies
     * that the method correctly identifies and counts only the double cannons.
     * Creates 2 double cannons at positions (2,2) and (2,4), and 1 single cannon at (1,3).
     * The getNumberDoubleCannons() method should return 2.
     */
    @Test
    void testGetNumberDoubleCannons(){
        // Create first double cannon (true indicates double cannon)
        Cannon dCannon1 = new Cannon(true, -1, universalConnectorList);
        shipBoard.pickComponent(dCannon1);
        shipBoard.assembleComponent(2,2);

        // Create second double cannon
        Cannon dCannon2 = new Cannon(true, -1, universalConnectorList);
        shipBoard.pickComponent(dCannon2);
        shipBoard.assembleComponent(2,4);

        // Create single cannon (false indicates single cannon)
        Cannon cannon = new Cannon(false, -1, universalConnectorList);
        shipBoard.pickComponent(cannon);
        shipBoard.assembleComponent(1,3);

        // Verify that only the 2 double cannons are counted
        assertEquals(2, shipBoard.getNumberDoubleCannons());
    }

    /**
     * Test method to verify the counting of single cannons on the ship board.
     * This test creates one double cannon and two single cannons, then verifies
     * that the method correctly identifies and counts only the single cannons.
     * Creates 1 double cannon at position (2,2) and 2 single cannons at (2,4) and (1,3).
     * The getNumberSingleCannons() method should return 2.
     */
    @Test
    void testGetNumberSingleCannons(){
        // Create double cannon
        Cannon dCannon1 = new Cannon(true, -1, universalConnectorList);
        shipBoard.pickComponent(dCannon1);
        shipBoard.assembleComponent(2,2);

        // Create first single cannon
        Cannon cannon1 = new Cannon(false, -1, universalConnectorList);
        shipBoard.pickComponent(cannon1);
        shipBoard.assembleComponent(2,4);

        // Create second single cannon
        Cannon cannon2 = new Cannon(false, -1, universalConnectorList);
        shipBoard.pickComponent(cannon2);
        shipBoard.assembleComponent(1,3);

        // Verify that only the 2 single cannons are counted
        assertEquals(2, shipBoard.getNumberSingleCannons());
    }

    /**
     * Test method to verify the counting of forward-facing double cannons.
     * This test creates two double cannons with different orientations:
     * one facing forward (default) and one rotated horizontally.
     * Creates 2 double cannons, one forward-facing and one rotated horizontally.
     * The getNumberForwardDoubleCannons() method should return 1.
     */
    @Test
    void testGetNumberForwardDoubleCannons(){
        // Create first double cannon (default forward orientation)
        Cannon dCannon1 = new Cannon(true, -1, universalConnectorList);
        shipBoard.pickComponent(dCannon1);
        shipBoard.assembleComponent(2,2);

        // Create second double cannon and rotate it to horizontal orientation
        Cannon dCannon2 = new Cannon(true, -1, universalConnectorList);
        shipBoard.pickComponent(dCannon2);
        shipBoard.rotatePickedComponent(); // Rotate to horizontal
        shipBoard.assembleComponent(2,4);

        // Verify that only 1 forward-facing double cannon is counted
        assertEquals(1, shipBoard.getNumberForwardDoubleCannons());
    }

    /**
     * Test method to verify the counting of forward-facing single cannons.
     * This test creates two single cannons with different orientations:
     * one facing forward (default) and one rotated horizontally.
     * Creates 2 single cannons, one forward-facing and one rotated horizontally.
     * The getNumberForwardSingleCannons() method should return 1.
     */
    @Test
    void testGetNumberForwardSingleCannons(){
        // Create first single cannon (default forward orientation)
        Cannon cannon1 = new Cannon(false, -1, universalConnectorList);
        shipBoard.pickComponent(cannon1);
        shipBoard.assembleComponent(2,2);

        // Create second single cannon and rotate it to horizontal orientation
        Cannon cannon2 = new Cannon(false, -1, universalConnectorList);
        shipBoard.pickComponent(cannon2);
        shipBoard.rotatePickedComponent(); // Rotate to horizontal
        shipBoard.assembleComponent(2,4);

        // Verify that only 1 forward-facing single cannon is counted
        assertEquals(1, shipBoard.getNumberForwardSingleCannons());
    }

    /**
     * Test method to verify the calculation of cannon strength in a specific direction.
     * This test sets up a ship with one forward-facing double cannon and one
     * horizontally-oriented single cannon, then checks the cannon strength in direction 0.
     * Creates 1 forward double cannon and 1 horizontal single cannon.
     * The getCannonStrength(0) method should return 0.5.
     */
    @Test
    void testGetCannonStrength(){
        // Create forward-facing double cannon
        Cannon dCannon = new Cannon(true, -1, universalConnectorList);
        shipBoard.pickComponent(dCannon);
        shipBoard.assembleComponent(2,2);

        // Create single cannon and rotate it horizontally
        Cannon cannon = new Cannon(false, -1, universalConnectorList);
        shipBoard.pickComponent(cannon);
        shipBoard.rotatePickedComponent(); // Rotate to horizontal
        shipBoard.assembleComponent(2,4);

        // Verify cannon strength in direction 0 (likely forward direction)
        assertEquals(0.5, shipBoard.getCannonStrength(0));
    }

    /**
     * Test method to verify the calculation of engine strength in a specific direction.
     * This test sets up a ship with one double engine and one single engine,
     * then checks the total engine strength in direction 0.
     * Creates 1 double engine and 1 single engine.
     * The getEngineStrength(0) method should return 1.
     */
    @Test
    void testGetEngineStrength(){
        // Create double engine
        Engine dEngine = new Engine(true, -1, universalConnectorList);
        shipBoard.pickComponent(dEngine);
        shipBoard.assembleComponent(2,2);

        // Create single engine
        Engine engine = new Engine(false, -1, universalConnectorList);
        shipBoard.pickComponent(engine);
        shipBoard.assembleComponent(2,4);

        // Verify total engine strength in direction 0
        assertEquals(1, shipBoard.getEngineStrength(0));
    }

    /**
     * Test method to verify the calculation of total goods price on the ship.
     * This test creates cargo holds, loads them with different colored goods,
     * and verifies that the total price calculation is correct.
     * Creates cargo holds with 1 blue, 2 yellow, and 1 red good.
     * The getGoodsPrice() method should return 11 (total value of all goods).
     */
    @Test
    void testGetGoodsPrice(){
        // Create regular cargo hold
        CargoHold cargoHold = new CargoHold(false, -1, universalConnectorList);
        shipBoard.pickComponent(cargoHold);
        shipBoard.assembleComponent(2,2);

        // Create special cargo hold
        CargoSpecial cargoSpecial = new CargoSpecial(true, -1, universalConnectorList);
        shipBoard.pickComponent(cargoSpecial);
        shipBoard.assembleComponent(2,1);

        // Load goods into cargo holds
        shipBoard.substituteGoods(2,2,Color.BLUE,0);    // Blue good in slot 0
        shipBoard.substituteGoods(2,2,Color.YELLOW,1);  // Yellow good in slot 1
        shipBoard.substituteGoods(2,2,Color.YELLOW,2);  // Yellow good in slot 2
        shipBoard.substituteGoods(2,1,Color.RED,0);     // Red good in slot 0

        // Verify total goods price (1 blue + 2 yellow + 1 red = 11)
        assertEquals(11, shipBoard.getGoodsPrice());
    }

    /**
     * Test method to verify the loading of goods into cargo holds from planet sources.
     * This test creates multiple cargo holds at different positions and loads them
     * with goods from a planet's available goods list.
     * Creates 4 cargo holds and loads goods from a planet's goods list.
     * Goods should be loaded in order: YELLOW, GREEN, BLUE, and one hold remains empty.
     */
    @Test
    void testLoadGoods(){
        // Create cargo hold components with universal connectors
        Component cargo1 = new CargoHold(false, 408, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component cargo2 = new CargoHold(false, 408, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component cargo3 = new CargoHold(false, 408, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component cargo4 = new CargoHold(false, 408, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));

        // Assemble cargo holds at specific positions
        shipBoard.assembleComponent(cargo1, 2, 2);
        shipBoard.assembleComponent(cargo2, 2, 4);
        shipBoard.assembleComponent(cargo3, 1, 3);
        shipBoard.assembleComponent(cargo4, 3, 3);

        // Define coordinates for cargo holds
        List<Integer> x = new ArrayList<>(Arrays.asList(2,2,1,0)); // X coordinates
        List<Integer> y = new ArrayList<>(Arrays.asList(2,4,3,0)); // Y coordinates

        // Define available goods from planet
        List<Color> planetOneGoods = new ArrayList<>();
        planetOneGoods.add(Color.YELLOW);
        planetOneGoods.add(Color.GREEN);
        planetOneGoods.add(Color.BLUE);
        planetOneGoods.add(Color.BLUE);

        // Load goods from planet into cargo holds
        shipBoard.loadGoods(x,y, planetOneGoods);

        // Verify that goods were loaded correctly
        assertEquals(Color.YELLOW, shipBoard.getAssembledComponent(2,2).getGoods().get(0));
        assertEquals(Color.GREEN, shipBoard.getAssembledComponent(2,4).getGoods().get(0));
        assertEquals(Color.BLUE, shipBoard.getAssembledComponent(1,3).getGoods().get(0));
        assertEquals(0, shipBoard.getAssembledComponent(3,3).getGoods().size()); // Fourth cargo hold remains empty
    }
}