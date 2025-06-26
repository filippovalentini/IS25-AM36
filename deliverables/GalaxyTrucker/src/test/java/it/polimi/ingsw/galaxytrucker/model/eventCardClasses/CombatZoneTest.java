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
    void init() { // 2 players
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
        gameState.addPlayer(cl1, player1, Color.RED); //player1
        gameState.addPlayer(cl2, player2, Color.YELLOW); //player2
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
        gameState.assembleComponent(player1, cargoP1,1, 3); //down to the init cabin
        gameState.assembleComponent(player1, cabinP1, 2, 4); //right to the init cabin

        gameState.setPosition(player1, 0); // player1 starts at 0
        gameState.setPosition(player2, 1); // player2 starts at 1
        gameState.addCrew(player1, 2, 3); // player1 has 1 crew
        gameState.addCrew(player2, 2, 3); // player2 has 1 crew
        gameState.addCrew(player1, 2, 4); // player1 has 1 more crew
        gameState.addBatteries(player2, 2, 2); // player2 has 2 batteries
        gameState.addBatteries(player1, 3, 3); // player1 has 3 batteries

        combatZoneLV1 = new CombatZone(true, 0); // combatZoneLV1 is the level 1 combat zone
        combatZoneLV2 = new CombatZone(false, 0); // combatZoneLV2 is the level 2 combat zone
    }


    @Test
    void combatZoneLV1completeTest(){ // 2 players
        //  PHASE 1
        gameState.pickGivenCard(combatZoneLV1); // player2 picks the combat zone level 1 card
        assertEquals(21, gameState.getPlayersPos().get(player2).getCell()); // player2 is at cell 21
        assertEquals(0, gameState.getPlayersPos().get(player1).getCell()); // player1 is at cell 0
        assertEquals(player1, gameState.getTurnPlayer()); // player1 starts the turn
        assertEquals(2, combatZoneLV1.getPhase()); // combatZoneLV1 is in phase 2
        // PHASE 2
        gameState.useBatteries(player1, 0); // player1 uses 0 batteries
        gameState.useBatteries(player2, 1); // player2 uses 1 battery
        assertEquals(player1, gameState.getTurnPlayer()); // player1 is the turn player
        gameState.landing(player1, new ArrayList<>(Arrays.asList(2)), new ArrayList<>(Arrays.asList(4)), new ArrayList<>(Arrays.asList(2)));
        assertEquals(player1, gameState.getTurnPlayer()); // player1 is still the turn player
        assertEquals(3, combatZoneLV1.getPhase()); // combatZoneLV1 is in phase 3
        //PHASE 3
        gameState.useBatteries(player1, 1); // player1 uses 1 battery
        gameState.useBatteries(player2, 0); // player2 uses 0 batteries
        assertEquals(player2, gameState.getTurnPlayer()); // player2 is the turn player
        gameState.hit(player2, 6, false, false); // player2 hits the ship with a dice result of 6
        gameState.hit(player2, 9, false, false); // player2 hits the ship with a dice result of 9
        assertEquals(1, gameState.getPlayersPlay().get(player2).getLostComponents()); // player2 loses 1 component
        assertEquals(State.CARD_PICKING, gameState.getGameState()); // game state is now in card picking
        assertEquals(player1, gameState.getTurnPlayer()); // player1 is the turn player
    }

    @Test
    void combatZoneLV2completeTest(){ // 2 players
        //  PHASE 0
        gameState.pickGivenCard(combatZoneLV2); // player2 picks the combat zone level 2 card
        assertEquals(1, gameState.getPlayersPos().get(player2).getCell()); // player2 is at cell 1
        assertEquals(0, gameState.getPlayersPos().get(player1).getCell()); // player1 is at cell 0
        assertEquals(player2, gameState.getTurnPlayer()); // player2 starts the turn
        assertEquals(1, combatZoneLV1.getPhase()); // combatZoneLV2 is in phase 1
        // PHASE 1
        gameState.useBatteries(player2, 0); // player2 uses 0 batteries
        gameState.useBatteries(player1, 1); // player1 uses 1 battery
        assertEquals(20, gameState.getPlayersPos().get(player2).getCell()); // player2 is at cell 20
        assertEquals(0, gameState.getPlayersPos().get(player1).getCell()); // player1 is at cell 0
        assertEquals(2, gameState.getNumberBatteries(player1)); // player1 has 2 batteries
        assertEquals(player1, gameState.getTurnPlayer()); // player1 is the turn player
        assertEquals(2, combatZoneLV2.getPhase()); // combatZoneLV2 is in phase 2
        // PHASE 2
        assertEquals(3, gameState.getNumberGoods(player1)); // player1 has 3 goods
        gameState.useBatteries(player1, 0); // player1 uses 0 batteries
        gameState.useBatteries(player2, 1); // player2 uses 1 battery
        assertEquals(0, gameState.getNumberGoods(player1)); // player1 has 0 goods
        assertEquals(player1, combatZoneLV2.getWorstEnginePlayer()); // player1 has the worst engine
        assertEquals(player2, gameState.getTurnPlayer()); // player2 is the turn player
        assertEquals(3, combatZoneLV2.getPhase()); // combatZoneLV2 is in phase 3
        //PHASE 3
        gameState.hit(player2, 9, false, false); // player2 hits the ship with a dice result of 9
        gameState.hit(player2, 7, false, false); // player2 hits the ship with a dice result of 7
        gameState.hit(player2, 7, false, false); // player2 hits the ship with a dice result of 7
        gameState.hit(player2, 9, false, false); // player2 hits the ship with a dice result of 9
        assertEquals(State.CARD_PICKING, gameState.getGameState()); // game state is now in card picking
        assertEquals(player1, gameState.getTurnPlayer()); // player1 is the turn player
        assertEquals(2, gameState.getPlayersPlay().get(player2).getLostComponents()); // player2 loses 2 components
    }


}