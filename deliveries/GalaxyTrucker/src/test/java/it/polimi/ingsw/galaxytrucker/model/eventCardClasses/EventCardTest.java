package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;
import it.polimi.ingsw.galaxytrucker.network.rmi.server.VirtualViewRMI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.galaxytrucker.model.enumerations.State.CARD_PICKING;
import static org.junit.jupiter.api.Assertions.*;

public class EventCardTest {
    private EventCard eventCard;
    private GameState gameState;
    private String player1;
    private String player2;
    private VirtualViewRMI cl1;
    private VirtualViewRMI cl2;
    private int numberPlanets=3;

    @BeforeEach
    public void init(){
        eventCard = new EventCard(1);
        gameState = new GameState (false,2);
        player1 = "truck3r";
        player2 = "4lien";
        try{
            cl1 = null;
            cl2 = null;
        }
        catch (Exception e){
            System.exit(-1);
        }
        gameState.addPlayer(cl1, player1, Color.RED);
        gameState.addPlayer(cl2, player2, Color.BLUE);
        gameState.setPosition(player1, 6);
        gameState.setPosition(player2, 3);
        gameState.addCrew(player1, 2, 3);
        gameState.addCrew(player2, 2, 3);


    }

    //test for getImageID
    @Test
    public void testGetImageID() {
        assertEquals(1, eventCard.getImageID());
    }
    //test manageGameQuit
    @Test
    public void testManageGameQuit() {
        eventCard.manageGameQuit(gameState, player1);
        gameState.setTurnPlayer(player1);
        assertEquals(CARD_PICKING, gameState.getGameState());
    }
    //test planetLanding
    @Test
    public void testPlanetLanding() {
        assertThrows(InvalidActionException.class, () -> eventCard.planetLanding(gameState, player1, numberPlanets));
    }
    //test HitShip
    @Test
    public void testHitShip() {
        assertThrows(InvalidActionException.class, () -> eventCard.hitShip(gameState, player1, 2, true, true));
    }
    //test Landing()
    @Test
    public void testLanding() {
        assertThrows(InvalidActionException.class, () -> eventCard.landing(gameState, player1, null, null, null));
    }
    //test specialEffect()
    @Test
    public void testSpecialEffect() {
        assertDoesNotThrow(() -> eventCard.specialEffect(gameState));
    }
    //test defeat()
    @Test
    public void testDefeat() {
        assertThrows(InvalidActionException.class, () -> eventCard.defeat(gameState,player1,2,true));
    }
    //test fly()
    @Test
    public void testFly() {
        assertThrows(InvalidActionException.class, () -> eventCard.fly(gameState, player1, 2));
    }
    //test useBatteries
    @Test
    public void testUseBatteries() {
        assertThrows(InvalidActionException.class, () -> eventCard.useBatteries(gameState, player1, 2));
    }
    //test skip()
    @Test
    public void testSkip() {
        assertThrows(InvalidActionException.class, () -> eventCard.skip(gameState, player1));
    }
    //test loadGoods()
    @Test
    public void TestLoadGoods(){
        assertThrows(InvalidActionException.class, () -> eventCard.loadGoods(gameState, player1, null, null));
        
    }
    //test switchGoods()
    @Test
    public void testSwitchGoods() {
        assertThrows(InvalidActionException.class, () -> eventCard.switchGoods(gameState, player1, 0, 0, Color.RED, 1));
    }

}
