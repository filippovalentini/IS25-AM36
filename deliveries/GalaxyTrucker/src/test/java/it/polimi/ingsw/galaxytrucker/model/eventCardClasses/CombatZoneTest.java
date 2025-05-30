package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.*;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoBatteriesException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoCrewException;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        player1 = "a";
        player2 = "b";
        try{
            cl1 = null;
            cl2 = null;
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState.addPlayer(cl1, player1, Color.RED);
        gameState.addPlayer(cl2, player2, Color.YELLOW);
        CargoHold cargoP1 = new CargoHold(false,2334, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        cargoP1.addGood(Color.YELLOW);
        cargoP1.addGood(Color.YELLOW);
        cargoP1.addGood(Color.YELLOW);
        Cabin cabinP1 = new Cabin(203, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Battery batteryP2 = new Battery(false, 201, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Cannon cannonP1 = new Cannon(true,12,new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Battery batteryP1 = new Battery(false, 201, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Engine engineP2 = new Engine(true, 726, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.SMOOTH, Connector.UNIVERSAL))); //double engine
        gameState.assembleComponent(player2, batteryP2,2, 2); //left to the init cabin
        gameState.assembleComponent(player2, engineP2, 2,4);//right to the init cabin
        gameState.assembleComponent(player1, cannonP1,2, 2);//left to the init cabin
        gameState.assembleComponent(player1, batteryP1,3, 3);//up to the init cabin
        gameState.assembleComponent(player1, cargoP1,1, 3);
        gameState.assembleComponent(player1, cabinP1, 2, 4);

        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 1);
        gameState.addCrew(player1, 2, 3);
        gameState.addCrew(player2, 2, 3);
        gameState.addCrew(player1, 2, 4);
        gameState.addBatteries(player2, 2, 2);
        gameState.addBatteries(player1, 3, 3);

        combatZoneLV1 = new CombatZone(true, 0);
        combatZoneLV2 = new CombatZone(false, 0);
    }


    @Test
    void combatZoneLV1completeTest(){
        //  PHASE 1
        gameState.pickGivenCard(combatZoneLV1);
        assertEquals(22, gameState.getPlayersPos().get(player2).getCell());
        assertEquals(0, gameState.getPlayersPos().get(player1).getCell());
        assertEquals(player1, gameState.getTurnPlayer());
        assertEquals(2, combatZoneLV1.getPhase());
        // PHASE 2
        gameState.useBatteries(player1, 0);
        gameState.useBatteries(player2, 1);
        assertEquals(player1, gameState.getTurnPlayer());
        gameState.landing(player1, new ArrayList<>(Arrays.asList(2)), new ArrayList<>(Arrays.asList(4)), new ArrayList<>(Arrays.asList(2)));
        assertEquals(player1, gameState.getTurnPlayer());
        assertEquals(3, combatZoneLV1.getPhase());
        //PHASE 3
        gameState.useBatteries(player1, 1);
        gameState.useBatteries(player2, 0);
        assertEquals(player2, gameState.getTurnPlayer());
        gameState.hit(player2, 6, false, false);
        gameState.hit(player2, 9, false, false);
        assertEquals(1, gameState.getPlayersPlay().get(player2).getLostComponents());
        assertEquals(State.CARD_PICKING, gameState.getGameState());
        assertEquals(player1, gameState.getTurnPlayer());
    }

    @Test
    void combatZoneLV2completeTest(){
        //  PHASE 0
        gameState.pickGivenCard(combatZoneLV2);
        assertEquals(1, gameState.getPlayersPos().get(player2).getCell());
        assertEquals(0, gameState.getPlayersPos().get(player1).getCell());
        assertEquals(player2, gameState.getTurnPlayer());
        assertEquals(1, combatZoneLV1.getPhase());
        // PHASE 1
        gameState.useBatteries(player2, 0);
        gameState.useBatteries(player1, 1);
        assertEquals(21, gameState.getPlayersPos().get(player2).getCell());
        assertEquals(0, gameState.getPlayersPos().get(player1).getCell());
        assertEquals(2, gameState.getNumberBatteries(player1));
        assertEquals(player1, gameState.getTurnPlayer());
        assertEquals(2, combatZoneLV2.getPhase());
        // PHASE 2
        assertEquals(3, gameState.getNumberGoods(player1));
        gameState.useBatteries(player1, 0);
        gameState.useBatteries(player2, 1);
        assertEquals(0, gameState.getNumberGoods(player1));
        assertEquals(player1, combatZoneLV2.getWorstEnginePlayer());
        assertEquals(player2, gameState.getTurnPlayer());
        assertEquals(3, combatZoneLV2.getPhase());
        //PHASE 3
        gameState.hit(player2, 9, false, false);
        gameState.hit(player2, 7, false, false);
        gameState.hit(player2, 7, false, false);
        gameState.hit(player2, 9, false, false);
        assertEquals(State.CARD_PICKING, gameState.getGameState());
        assertEquals(player1, gameState.getTurnPlayer());
        assertEquals(2, gameState.getPlayersPlay().get(player2).getLostComponents());
    }


}