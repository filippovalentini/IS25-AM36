package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Battery;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Cannon;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.CargoHold;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Engine;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoBatteriesException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CombatZoneTest {
    private CombatZone combatZoneLV1;
    private CombatZone combatZoneLV2;
    private GameState gameState;
    private String player1;
    private String player2;
    private VirtualViewRMI cl1;
    private VirtualViewRMI cl2;

    @BeforeEach
    void init() {
        gameState = new GameState(false, 2);
        CargoHold cargoP1 = new CargoHold(false,2334, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.SINGLE, Connector.DOUBLE, Connector.SMOOTH)));
       cargoP1.addGood(Color.YELLOW);
       cargoP1.addGood(Color.GREEN);
       cargoP1.addGood(Color.BLUE);
        player1 = "a";
        player2 = "b";
        try{
            cl1 = new ClientRMI(player1, Color.RED);
            cl2 = new ClientRMI(player2, Color.YELLOW);
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState.addPlayer(cl1, player1, Color.RED);
        gameState.addPlayer(cl2, player2, Color.YELLOW);
        Battery batteryP2 = new Battery(true, 201, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.SINGLE, Connector.DOUBLE, Connector.SMOOTH)));
        Cannon cannonP1 = new Cannon(true,12,new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.SINGLE, Connector.DOUBLE, Connector.SMOOTH)));
        Battery batteryP1 = new Battery(true, 201, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.SINGLE, Connector.DOUBLE, Connector.SMOOTH)));

        Engine engineP2 = new Engine(true, 726, new ArrayList<>(Arrays.asList(Connector.SINGLE, Connector.SINGLE, Connector.UNIVERSAL, Connector.SINGLE))); //double engine
        gameState.assembleComponent(player2, batteryP2,2, 2); //left to the init cabin
        gameState.assembleComponent(player2, engineP2, 2,4);//right to the init cabin
        gameState.assembleComponent(player1, cannonP1,2, 2);//left to the init cabin
        gameState.assembleComponent(player1, batteryP1,3, 3);//up to the init cabin
        gameState.assembleComponent(player1, cargoP1,1, 3);

        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 3);
        gameState.addCrew(player1, 2, 3);
        gameState.addCrew(player2, 2, 3);
        gameState.addBatteries(player2, 2, 2);
        gameState.addBatteries(player1, 3, 3);
        gameState.setGameState(State.CARD_SOLVING);
        combatZoneLV1 = new CombatZone(true, 0);
        combatZoneLV2 = new CombatZone(false, 0);
    }

    @Test
    void testSpecialEffectLV1() {
        combatZoneLV1.specialEffect(gameState);
        assertEquals(0, gameState.getPlayersPos().get(player2).getCell()); //player2 should have lost 3 position
    }
    @Test
    void testSpecialEffectLV1WrongPhase() {
        combatZoneLV1.specialEffect(gameState);
        assertThrows(InvalidActionException.class, () -> combatZoneLV1.specialEffect(gameState));
    }


    @Test
    void testUseBatteriesAvailable(){
        combatZoneLV1.specialEffect(gameState); //applies the effect so it will be in second phase
        combatZoneLV1.useBatteries(gameState, player2, 2);
        combatZoneLV1.useBatteries(gameState, player1, 0); //player1 should be the next in turn ready to apply the penalty
        List<Integer> x = new ArrayList<>();
        x.add(2);
        List<Integer> y = new ArrayList<>();
        y.add(3);
        List<Integer> crewMemToRemove = new ArrayList<>();
        crewMemToRemove.add(2);
        combatZoneLV1.landing(gameState, player1, x, y, crewMemToRemove); //player1 removes 2 crew member
        assertEquals(0, gameState.getPlayersPlay().get(player1).getNumberCrew()); //player1 must have 0 crew members at this point
    }

    @Test
    void testShouldNotUseBatteriesNotAvailableL1(){
        combatZoneLV1.specialEffect(gameState); //applies the effect so it will be in second phase
        assertThrows(NoBatteriesException.class, () -> {combatZoneLV1.useBatteries(gameState, player2, 1000);});
    }

    @Test
    void testHitShipEmptyColumnL1(){
        combatZoneLV1.specialEffect(gameState); //applies the effect so it will be in second phase
        //the player should not lose any component
        combatZoneLV1.hitShip(gameState, player1, 1+4, false, false);
        assertEquals(0, gameState.getPlayersPlay().get(player1).getShipBoard().getLostComponents());
    }

    @Test
    void testHitShipNotEmptyColumnL1(){
        combatZoneLV1.specialEffect(gameState); //applies the effect so it will be in second phase
        //the player2 should lose the component (battery)
        assertEquals(0, gameState.getPlayersPlay().get(player2).getShipBoard().getLostComponents());
        combatZoneLV1.hitShip(gameState, player2, 2+4, false, false);
        assertEquals(1, gameState.getPlayersPlay().get(player2).getShipBoard().getLostComponents());
    }

    //---------------------------------------------------------------------------------------------------
    @Test
    void testSpecialEffectLV2WrongAction() {
        assertThrows(InvalidActionException.class, () -> combatZoneLV2.specialEffect(gameState));
    }



    @Test
    void testUseBatteriesAvailableLV2(){
        combatZoneLV2.useBatteries(gameState, player2, 0); // usa 2 batterie
        combatZoneLV2.useBatteries(gameState, player1, 2); // passa turno
        assertEquals(23, gameState.getPlayersPos().get(player2).getCell()); //player2 should have lost 3 position

    }

    @Test
    void testShouldNotUseBatteriesNotAvailableL2(){
        assertThrows(NoBatteriesException.class, () -> {
            combatZoneLV2.useBatteries(gameState, player2, 1000);
        });
    }

    @Test
    void testHitShipEmptyColumnL2(){
        combatZoneLV2.useBatteries(gameState,player1,0);
        combatZoneLV2.useBatteries(gameState, player1, 2);
        List<Integer> x = new ArrayList<>();
        x.add(1);
        List<Integer> y = new ArrayList<>();
        y.add(3);
        List<Integer> goodsToRemove = new ArrayList<>();
        goodsToRemove.add(3);
        combatZoneLV2.landing(gameState, player1, x, y, goodsToRemove);
        assertEquals(0, gameState.getNumberGoods(player1));
    }

    @Test
    void testHitShipNotEmptyColumnL2(){
        combatZoneLV2.useBatteries(gameState,player1,0);
        combatZoneLV2.useBatteries(gameState, player1, 2);
        List<Integer> x = new ArrayList<>();
        x.add(1);
        List<Integer> y = new ArrayList<>();
        y.add(3);
        List<Integer> goodsToRemove = new ArrayList<>();
        goodsToRemove.add(3);
        combatZoneLV2.landing(gameState, player1, x, y, goodsToRemove);
         // entra nella terza fase
        assertEquals(0, gameState.getPlayersPlay().get(player2).getShipBoard().getLostComponents());
        combatZoneLV2.hitShip(gameState, player2, 2 + 4, false, false); // colpisce componente (batteria)
        assertEquals(1, gameState.getPlayersPlay().get(player2).getShipBoard().getLostComponents());
    }
}