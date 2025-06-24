package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.eventCardClasses.EventCard;
import it.polimi.ingsw.galaxytrucker.model.exceptions.EmptyDeckException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.PickedDeckException;

import java.util.*;

/**
 * This class is used to represent both the three decks used in the assembling phase (only for standard game) and the main deck of the game
 */
public class Deck {
    private List<EventCard> cards;      //list of cards contained in the deck
    public boolean picked;      //true if the deck has been picked by a player

    /**
     * Constructor for the Deck class
     * @param cards
     */
    public Deck(List<EventCard> cards) {        //constructor
        this.cards = cards;
        this.picked = false;
    }

    /**
     * Returns a copy of the list of cards in the deck
     * @return a copy of the list of cards in the deck
     */
    public List<EventCard> getCards() { //return a copy of eventCards
        if(this.cards == null) {
            return null;
        }
        return new ArrayList<>(this.cards);
    }

    /**
     * Returns the number of cards in the deck
     * @return the number of cards in the deck
     */
    public int getNumberCards() {
        return cards.size();
    }
    /**
     * Returns true if the deck has been picked by a player, false otherwise
     * @return true if the deck has been picked by a player, false otherwise
     */
    public boolean isPicked() {
        return picked;
    }

    /**
     * Sets the deck as picked by a player
     * @throws PickedDeckException
     */
    public void setPicked() throws PickedDeckException{       //invoked when a player picks a deck
        if(picked){
            throw new PickedDeckException("The deck has been picked by another player");
        }
        this.picked = true;
    }

    /**
     * Sets the deck as not picked by a player
     * @throws PickedDeckException
     */
    public void setNotPicked() throws PickedDeckException{       //invoked when a player releases a deck
        if(!picked){
            throw new PickedDeckException("The deck has been picked by another player");
        }
        this.picked = false;
    }

    /**
     * Draws a card from the deck, removing it from the deck
     * @return the drawn EventCard
     * @throws EmptyDeckException
     */
    public EventCard drawCard() throws EmptyDeckException {     //removes and returns the last card of the deck
        if (cards.isEmpty()) {
            throw new EmptyDeckException("Deck is empty");
        }
        else {
            return cards.removeLast();
        }

    }

    /**
     * Shuffles the cards in the deck
     */
    public void shuffle(){      //shuffles the cards in the deck
        Collections.shuffle(cards);
    }

}

