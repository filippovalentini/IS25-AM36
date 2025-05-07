package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Battery;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Cannon;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoCrewException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.shotClasses.CannonShot;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PiratesTest {
    private Pirates pirates;
    private GameState gameState;
    String nickname;
    String nickname2;
    private VirtualViewRMI cl1;
    private VirtualViewRMI cl2;
    List<CannonShot> cannonShots;



    @BeforeEach
    void init(){
        gameState = new GameState(false, 2);
        nickname = "player1";
        nickname2 = "player2";
        try{
            cl1 = new ClientRMI(nickname, Color.RED);
            cl2 = new ClientRMI(nickname2, Color.BLUE);
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState.addPlayer(cl1, nickname, Color.RED);
        gameState.addPlayer(cl2, nickname2, Color.BLUE);
        Component battery = new Battery(false, 1000, new ArrayList<>(Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        Component cannon = new Cannon(true, 2000, new ArrayList<>(Arrays.asList(Connector.SMOOTH, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL)));
        gameState.assembleComponent(nickname, battery, 2, 4);
        gameState.assembleComponent(nickname, cannon, 1, 3);
        gameState.setPosition(nickname, 0);
        gameState.setPosition(nickname2, 1);
        gameState.addBatteries(nickname, 2, 4);
        gameState.addCrew(nickname, 2, 3);
        gameState.addCrew(nickname2, 2, 3);

        CannonShot cShotLarge = new CannonShot(true, Orientation.NORTH);
        CannonShot cShotNotLarge1 = new CannonShot(false, Orientation.NORTH);
        CannonShot cShotNotLarge2 = new CannonShot(false, Orientation.NORTH);
        cannonShots = new ArrayList<>();
        cannonShots.add(cShotLarge);
        cannonShots.add(cShotNotLarge1);
        cannonShots.add(cShotNotLarge2);
        pirates = new Pirates(4, 1, cannonShots, 1, 0);
        gameState.setGameState(State.CARD_SOLVING);
    }

    @Test
    void testDefeat() {
        int usedBatteries = 0;
        boolean looseDays = true;
        assertDoesNotThrow(() -> pirates.defeat(gameState, nickname, usedBatteries, looseDays));
    }

    @Test
    void testShouldNotDefeatNotEnoughStrength() {
        Pirates strongPirates = new Pirates(4, 1000, cannonShots, 1, 0);
        int usedBatteries = 0;
        boolean looseDays = true;
        assertDoesNotThrow(() -> strongPirates.defeat(gameState, nickname, usedBatteries, looseDays));
    }

    @Test
    void testHitShip() {
        int diceResult = 7;
        int usedBatteries = 0;
        boolean looseDays = true;
        boolean activateShield = false;
        boolean activateCannon = false;
        pirates.defeat(gameState, nickname, usedBatteries, looseDays);
        pirates.hitShip(gameState, nickname, diceResult, activateShield, activateCannon);
        assertEquals(0, gameState.getPlayersPlay().get(nickname).getShipBoard().getAssembledComponent(1, 3).getImageID());
        assertDoesNotThrow(() -> pirates.hitShip(gameState, nickname, diceResult, activateShield, activateCannon));
    }

    @Test
    void testDefeatedPirates(){
        int usedBatteries = 1;
        boolean loseDays = true;
        pirates.defeat(gameState, nickname, usedBatteries, loseDays);
        assertThrows(InvalidActionException.class, () -> pirates.hitShip(gameState, nickname, 3, false, false));
        assertEquals(23, gameState.getPlayersPos().get(nickname).getCell());
    }

    @Test
    void testHitShipQuitCond() {
        int diceResult = 4;
        int usedBatteries = 0;
        boolean looseDays = true;
        boolean activateShield = false;
        boolean activateCannon = false;
        List<Integer> y= new ArrayList<>();
        List<Integer> x= new ArrayList<>();
        List<Integer> e= new ArrayList<>();
        x.add(2);
        y.add(3);
        e.add(2);
        pirates.defeat(gameState, nickname, usedBatteries, looseDays);
        gameState.removeCrewMembers(nickname,x,y,e,2);
        pirates.hitShip(gameState, nickname, diceResult, activateShield, activateCannon);
        pirates.hitShip(gameState, nickname, diceResult, activateShield, activateCannon);
        assertThrows(NoCrewException.class, () -> pirates.hitShip(gameState, nickname, diceResult, activateShield, activateCannon));
        assertFalse(gameState.getPlayersPos().containsKey(nickname));
        assertEquals(State.CARD_PICKING, gameState.getGameState());
    }
}
