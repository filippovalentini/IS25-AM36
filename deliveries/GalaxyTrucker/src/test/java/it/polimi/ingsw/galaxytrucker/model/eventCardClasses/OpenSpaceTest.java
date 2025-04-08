package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Battery;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Engine;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.LevelTwoPosition;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.Position;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OpenSpaceTest {
    private OpenSpace os;
    private GameState gameState;
    private String player1;
    private String player2;
    private VirtualViewRMI cl1;
    private VirtualViewRMI cl2;
    private Engine engine2;
    private List<Connector> sides;
    private Position position;
    @BeforeEach
    void init() {
        os = new OpenSpace(2);
        gameState = new GameState(false, 2);
        player1="filippo";
        player2="thomas";
        try{
            cl1 = new ClientRMI();
            cl2 = new ClientRMI();
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState.addPlayer(cl1, player1, Color.RED);
        gameState.addPlayer(cl2, player2, Color.GREEN);
        gameState.setPosition(player1, 6);
        gameState.setPosition(player2, 3);
        gameState.updateTurns();
        sides = new ArrayList<>();
        sides.add(Connector.UNIVERSAL);
        sides.add(Connector.UNIVERSAL);
        sides.add(Connector.SMOOTH);
        sides.add(Connector.UNIVERSAL);
        engine2= new Engine(false,24244, sides);
        gameState.assembleComponent(player1, engine2,1,3);
        position= new LevelTwoPosition(gameState.getPlayersPos().get(player1).getCell());
    }

    @Test
    void testFly(){
        os.fly(gameState,player1,0);
        assertEquals(player2, gameState.getTurnPlayer(), "The leader should be player2 (thomas)");
        assertEquals(position.getCell()+1, gameState.getPlayersPos().get(player1).getCell(), "The leader should be the lap");
    }
}