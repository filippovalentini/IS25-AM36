package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.eventCardClasses.AbandonedShip;
import it.polimi.ingsw.galaxytrucker.model.eventCardClasses.AbandonedStation;
import it.polimi.ingsw.galaxytrucker.model.eventCardClasses.EventCard;
import it.polimi.ingsw.galaxytrucker.model.exceptions.EmptyDeckException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.PickedDeckException;
import it.polimi.ingsw.galaxytrucker.network.socket.client.SocketServerHandler;
import it.polimi.ingsw.galaxytrucker.network.socket.server.SocketClientHandler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    /**
     * Test to verify that the getCards() method returns a copy of the card list
     * and that modifications to the copy do not affect the original deck (defensive copying).
     * Also verifies that the size and content are correct.
     */
    @Test
    void testGetCards(){
        List<EventCard> listOfEventCards = new ArrayList<>();
        EventCard ec = new AbandonedShip(2, 3, 1, 1001);
        listOfEventCards.add(ec);
        Deck d = new Deck(listOfEventCards);
        assertEquals(1, d.getCards().size());
        assertEquals(d.getCards().getFirst(), ec);
        List<EventCard> copyListOfEventCards = d.getCards();
        copyListOfEventCards.removeLast(); //removing from copied list shouldn't have effects on Deck cards
        assertEquals(1, d.getCards().size());
    }

    /**
     * Test to verify the behavior of the getCards() method when the deck
     * is initialized with null. Must return null without throwing exceptions.
     */
    @Test
    void testGetCardsNull(){
        Deck d = new Deck(null);
        assertNull(d.getCards());
    }

    /**
     * Test to verify that the drawCard() method correctly throws
     * EmptyDeckException when attempting to draw from an empty deck.
     */
    @Test
    void testDrawCardFromEmptyDeck() {
        List<EventCard> listOfEventCards = new ArrayList<>();
        Deck deck = new Deck(listOfEventCards);
        assertThrows(EmptyDeckException.class, deck::drawCard);
    }

    /**
     * Test to verify that the drawCard() method works correctly
     * with a non-empty deck, returning a card of the correct type.
     */
    @Test
    void testDrawCardFromNonEmptyDeck() {
        List<EventCard> listOfEventCards = new ArrayList<>();
        listOfEventCards.add(new AbandonedShip(2, 3, 1, 1001));
        Deck deck = new Deck(listOfEventCards);
        assertEquals(AbandonedShip.class, deck.drawCard().getClass());
    }

    /**
     * Test to verify that the drawn card is exactly the same instance
     * that was inserted into the deck (object identity test).
     */
    @Test
    void testDrawCardAndSameCard() {
        List<EventCard> listOfEventCards = new ArrayList<>();
        EventCard ec = new AbandonedShip(2, 3, 1, 1001);
        listOfEventCards.add(ec);
        Deck deck = new Deck(listOfEventCards);
        assertEquals(ec, deck.drawCard());
    }

    /**
     * Test to verify that getCards() returns the correct content of the deck
     * by comparing the returned list with the original one.
     */
    @Test
    void testGetActualCardsFromDeck() {
        List<EventCard> listOfEventCards = new ArrayList<>();
        listOfEventCards.add(new AbandonedShip(2, 3, 1, 1001));
        listOfEventCards.add(new AbandonedStation(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN)), 5, 1, 2001));
        Deck deck = new Deck(listOfEventCards);
        assertEquals(listOfEventCards, deck.getCards());
    }

    /**
     * Test to verify that the shuffle() method does not cause card loss.
     * The number of cards must remain unchanged after the shuffle operation.
     */
    @Test
    void testShuffleHasNoLeaks() {
        List<EventCard> listOfEventCards = new ArrayList<>();
        listOfEventCards.add(new AbandonedShip(2, 3, 1, 1001));
        listOfEventCards.add(new AbandonedStation(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN)), 5, 1, 2001));
        Deck deck = new Deck(listOfEventCards);
        int initialSize = deck.getNumberCards();
        deck.shuffle();
        assertEquals(initialSize, deck.getNumberCards());
    }

    /**
     * Test to verify that after a shuffle it is still possible to draw cards
     * and that the card count decreases correctly after the draw.
     */
    @Test
    void testDrawAfterShuffle(){
        List<EventCard> listOfEventCards = new ArrayList<>();
        listOfEventCards.add(new AbandonedShip(2, 3, 1, 1001));
        listOfEventCards.add(new AbandonedStation(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN)), 5, 1, 2001));
        Deck deck = new Deck(listOfEventCards);
        int initialSize = deck.getNumberCards();
        deck.shuffle();
        deck.drawCard();
        assertEquals(initialSize-1, deck.getNumberCards());
    }

    /**
     * Test to verify that it is not possible to mark as "picked" a deck
     * that has already been picked by another player. Must throw PickedDeckException.
     */
    @Test
    void testShouldNotPickPickedDeck(){
        List<EventCard> listOfEventCards = new ArrayList<>();
        listOfEventCards.add(new AbandonedShip(2, 3, 1, 1001));
        Deck deck = new Deck(listOfEventCards);
        deck.setPicked();
        assertThrows(PickedDeckException.class, deck::setPicked);
    }

    /**
     * Test to verify the complete picked/not-picked cycle.
     * A deck can be marked as picked and subsequently as not-picked.
     */
    @Test
    void testSetNotPickedDeck(){
        Deck deck = new Deck(null);
        deck.setPicked();
        assertTrue(deck.isPicked());
        deck.setNotPicked();
        assertFalse(deck.isPicked());
    }

    /**
     * Test to verify that it is not possible to mark as "not picked" a deck
     * that is already not-picked. Must throw PickedDeckException to maintain
     * state consistency.
     */
    @Test
    void testShouldNotSetNotPickedDeckAlreadyNotPicked(){
        Deck deck = new Deck(null);
        assertFalse(deck.isPicked());
        assertThrows(PickedDeckException.class, deck::setNotPicked);
    }
}