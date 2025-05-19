package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Battery;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.NoBatteriesException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SmugglersTest {
    private Smugglers smugglers;
    private GameState gameState;
    String nickname;
    String nickname2;
    private VirtualViewRMI cl1;
    private VirtualViewRMI cl2;
    List<Color> prizeGoods;

    @BeforeEach
    void init(){
        prizeGoods = new ArrayList<>();
        prizeGoods.add(Color.YELLOW);
        prizeGoods.add(Color.GREEN);
        prizeGoods.add(Color.BLUE);
        List<Connector> sides = new ArrayList<>();
        sides.add(Connector.SMOOTH);
        sides.add(Connector.UNIVERSAL);
        sides.add(Connector.SINGLE);
        sides.add(Connector.DOUBLE);
        smugglers = new Smugglers(prizeGoods, 2,4, 1, 0);
        gameState = new GameState(false, 2);
        nickname = "a";
        nickname2 = "b";
        try{
            cl1 = new ClientRMI(nickname, Color.BLUE);
            cl2 = new ClientRMI(nickname2, Color.RED);
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState.addPlayer(cl1, nickname, Color.BLUE);
        gameState.addPlayer(cl2, nickname2, Color.RED);
        gameState.setPosition(nickname, 0);
        gameState.setPosition(nickname2, 1);
        Battery battery = new Battery(true,4, sides);

        gameState.assembleComponent(nickname, battery, 1,3);
        gameState.setGameState(State.CARD_SOLVING);
    }

    @Test
    void testShouldNotAttackIfDefeated() {
        int usedBatteries = 4;
        boolean looseDays = true;
        smugglers.setDefeated();
        assertThrows(InvalidActionException.class, () -> smugglers.defeat(gameState, nickname, usedBatteries, looseDays));
    }

    @Test
    void testShouldNotAttackIfNotEnoughBatteries() {
        int usedBatteries = 5;
        gameState.getNumberBatteries(nickname);
        boolean looseDays = true;
        assertThrows(NoBatteriesException.class, () -> smugglers.defeat(gameState, nickname, usedBatteries, looseDays));
    }
}