package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.eventCardClasses.AbandonedShip;
import it.polimi.ingsw.galaxytrucker.model.eventCardClasses.AbandonedStation;
import it.polimi.ingsw.galaxytrucker.model.eventCardClasses.EventCard;
import it.polimi.ingsw.galaxytrucker.model.exceptions.EmptyDeckException;
import jdk.jfr.Event;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void testDrawCardFromEmptyDeck() {
        List<EventCard> listOfEventCards = new ArrayList<>();
        Deck deck = new Deck(listOfEventCards);
        deck.drawCard();
        assertThrows(EmptyDeckException.class, () -> deck.drawCard());
    }

    @Test
    void testDrawCardFromNonEmptyDeck() {
        List<EventCard> listOfEventCards = new ArrayList<>();
        listOfEventCards.add(new AbandonedShip(2, 3, 1, "abandonedshipL1_1.jpg"));
        Deck deck = new Deck(listOfEventCards);
        assertEquals(EventCard.class, deck.drawCard().getClass());
    }

    @Test
    void testDrawCardAndSameCard() {
        List<EventCard> listOfEventCards = new ArrayList<>();
        EventCard ec = new AbandonedShip(2, 3, 1, "abandonedshipL1_1.jpg");
        listOfEventCards.add(ec);
        Deck deck = new Deck(listOfEventCards);
        assertEquals(ec, deck.drawCard());
    }

    @Test
    void testGetCardsFromDeck() {
        List<EventCard> listOfEventCards = new ArrayList<>();
        listOfEventCards.add(new AbandonedShip(2, 3, 1, "abandonedshipL1_1.jpg"));
        listOfEventCards.add(new AbandonedStation(new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN)), 5, 1, "abandonedstationL1_1.jpg"));
        Deck deck = new Deck(listOfEventCards);
        assertEquals(listOfEventCards, deck.getCards());
    }
}