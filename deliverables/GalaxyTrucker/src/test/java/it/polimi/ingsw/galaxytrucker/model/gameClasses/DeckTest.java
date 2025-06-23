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

    @Test
    void testGetCardsNull(){
        Deck d = new Deck(null);
        assertNull(d.getCards());
    }

    @Test
    void testDrawCardFromEmptyDeck() {
        List<EventCard> listOfEventCards = new ArrayList<>();
        Deck deck = new Deck(listOfEventCards);
        assertThrows(EmptyDeckException.class, deck::drawCard);
    }

    @Test
    void testDrawCardFromNonEmptyDeck() {
        List<EventCard> listOfEventCards = new ArrayList<>();
        listOfEventCards.add(new AbandonedShip(2, 3, 1, 1001));
        Deck deck = new Deck(listOfEventCards);
        assertEquals(AbandonedShip.class, deck.drawCard().getClass());
    }

    @Test
    void testDrawCardAndSameCard() {
        List<EventCard> listOfEventCards = new ArrayList<>();
        EventCard ec = new AbandonedShip(2, 3, 1, 1001);
        listOfEventCards.add(ec);
        Deck deck = new Deck(listOfEventCards);
        assertEquals(ec, deck.drawCard());
    }

    @Test
    void testGetActualCardsFromDeck() {
        List<EventCard> listOfEventCards = new ArrayList<>();
        listOfEventCards.add(new AbandonedShip(2, 3, 1, 1001));
        listOfEventCards.add(new AbandonedStation(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN)), 5, 1, 2001));
        Deck deck = new Deck(listOfEventCards);
        assertEquals(listOfEventCards, deck.getCards());
    }

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

    @Test
    void testShouldNotPickPickedDeck(){
        List<EventCard> listOfEventCards = new ArrayList<>();
        listOfEventCards.add(new AbandonedShip(2, 3, 1, 1001));
        Deck deck = new Deck(listOfEventCards);
        deck.setPicked();
        assertThrows(PickedDeckException.class, deck::setPicked);
    }

    @Test
    void testSetNotPickedDeck(){
        Deck deck = new Deck(null);
        deck.setPicked();
        assertTrue(deck.isPicked());
        deck.setNotPicked();
        assertFalse(deck.isPicked());
    }

    @Test
    void testShouldNotSetNotPickedDeckAlreadyNotPicked(){
        Deck deck = new Deck(null);
        assertFalse(deck.isPicked());
        assertThrows(PickedDeckException.class, deck::setNotPicked);
    }
}