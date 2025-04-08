package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.componentClasses.Cabin;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.CargoHold;
import it.polimi.ingsw.galaxytrucker.model.componentClasses.Component;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;
import it.polimi.ingsw.galaxytrucker.model.enumerations.SpecialEventType;
import it.polimi.ingsw.galaxytrucker.model.enumerations.State;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.network.rmi.client.ClientRMI;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpecialEventTest {
    private SpecialEvent stardustEvent;
    SpecialEvent epidemicEvent;
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
            cl1 = new ClientRMI();
            cl2 = new ClientRMI();
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState.addPlayer(cl1, player1, Color.RED);
        gameState.addPlayer(cl2, player2, Color.YELLOW);
        gameState.setPosition(player1, 0);
        gameState.setPosition(player2, 6);
        List<Component> shownComponents = gameState.getShownComponent();
        for(int i=0; i<shownComponents.size(); i++) { //assemble the cabin at the left to the initial cabin
            if (shownComponents.get(i).getClass() == Cabin.class && shownComponents.get(i).getEastSide()!= Connector.UNIVERSAL) {
                gameState.pickShown(player1, i);
                gameState.assembleComponent(player1, 2, 2);
                i=100000;
            }
        }
        gameState.setGameState(State.CARD_SOLVING);
        stardustEvent = new SpecialEvent(SpecialEventType.STARDUST, 0);
        epidemicEvent = new SpecialEvent(SpecialEventType.EPIDEMIC, 0);
    }

    @Test
    void testSpecialEventStardust() {
        stardustEvent.specialEffect(gameState);
        assertEquals(2, gameState.getPlayersPos().get(player2).getCell());
    }

    @Test
    void testSpecialEventEpidemic(){
        epidemicEvent.specialEffect(gameState);
        //each player should lose one crew member
        assertEquals(2,gameState.getPlayersPlay().get(player1).getNumberCrew());
    }
}