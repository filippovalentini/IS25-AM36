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
    public void init(){ // Initialize the EventCard and GameState objects
        eventCard = new EventCard(1);
        gameState = new GameState (false,2);
        player1 = "truck3r";
        player2 = "4lien";
        try{
            cl1 = null;
            cl2 = null;
        }
        catch (Exception e){ // Handle exceptions for client initialization
            System.exit(-1); // Exit if client initialization fails
        }
        gameState.addPlayer(cl1, player1, Color.RED); // Add first player to the game state
        gameState.addPlayer(cl2, player2, Color.BLUE); // Add second player to the game state
        gameState.setPosition(player1, 6); // Set the position of the first player
        gameState.setPosition(player2, 3); // Set the position of the second player
        gameState.addCrew(player1, 2, 3); // Add crew to the first player
        gameState.addCrew(player2, 2, 3); // Add crew to the second player


    }

    //test for getImageID
    @Test
    public void testGetImageID() {
        assertEquals(1, eventCard.getImageID());
    } //test for getCardType
    //test manageGameQuit
    @Test
    public void testManageGameQuit() { // Test the manageGameQuit method
        eventCard.manageGameQuit(gameState, player1); // Call the method with the first player
        gameState.setTurnPlayer(player1); // Set the turn player to the first player
        assertEquals(CARD_PICKING, gameState.getGameState()); // Assert that the game state is now CARD_PICKING
    }
    //test planetLanding
    @Test
    public void testPlanetLanding() {
        assertThrows(InvalidActionException.class, () -> eventCard.planetLanding(gameState, player1, numberPlanets)); // Test the planetLanding method and expect an InvalidActionException
    }
    //test HitShip
    @Test
    public void testHitShip() { // Test the hitShip method
        assertThrows(InvalidActionException.class, () -> eventCard.hitShip(gameState, player1, 2, true, true)); // Expect an InvalidActionException when calling hitShip
    }
    //test Landing()
    @Test
    public void testLanding() {
        assertThrows(InvalidActionException.class, () -> eventCard.landing(gameState, player1, null, null, null)); // Test the landing method and expect an InvalidActionException
    }
    //test specialEffect()
    @Test
    public void testSpecialEffect() {
        assertDoesNotThrow(() -> eventCard.specialEffect(gameState));
    } // Test the specialEffect method and expect no exceptions
    //test defeat()
    @Test
    public void testDefeat() {
        assertThrows(InvalidActionException.class, () -> eventCard.defeat(gameState,player1,2,true)); // Test the defeat method and expect an InvalidActionException
    }
    //test fly()
    @Test
    public void testFly() {
        assertThrows(InvalidActionException.class, () -> eventCard.fly(gameState, player1, 2));
    } // Test the fly method and expect an InvalidActionException
    //test useBatteries
    @Test
    public void testUseBatteries() {
        assertThrows(InvalidActionException.class, () -> eventCard.useBatteries(gameState, player1, 2));
    } // Test the useBatteries method and expect an InvalidActionException
    //test skip()
    @Test
    public void testSkip() {
        assertThrows(InvalidActionException.class, () -> eventCard.skip(gameState, player1));
    } // Test the skip method and expect an InvalidActionException
    //test loadGoods()
    @Test
    public void TestLoadGoods(){
        assertThrows(InvalidActionException.class, () -> eventCard.loadGoods(gameState, player1, null, null));
        
    } // Test the loadGoods method and expect an InvalidActionException


}
