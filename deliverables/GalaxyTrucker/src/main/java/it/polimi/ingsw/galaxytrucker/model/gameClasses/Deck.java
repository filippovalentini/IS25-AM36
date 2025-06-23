package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.eventCardClasses.EventCard;
import it.polimi.ingsw.galaxytrucker.model.exceptions.EmptyDeckException;
import it.polimi.ingsw.galaxytrucker.model.exceptions.PickedDeckException;

import java.util.*;
//this class is used to represent both the three decks used in the assembling phase (only for standard game)
// and the main deck of the game
public class Deck {
    private List<EventCard> cards;      //list of cards contained in the deck
    public boolean picked;      //true if the deck has been picked by a player

    public Deck(List<EventCard> cards) {        //constructor
        this.cards = cards;
        this.picked = false;
    }
    public List<EventCard> getCards() { //return a copy of eventCards
        if(this.cards == null) {
            return null;
        }
        return new ArrayList<>(this.cards);
    }
    public int getNumberCards() {
        return cards.size();
    }
    public boolean isPicked() {
        return picked;
    }

    public void setPicked() throws PickedDeckException{       //invoked when a player picks a deck
        if(picked){
            throw new PickedDeckException("The deck has been picked by another player");
        }
        this.picked = true;
    }
    public void setNotPicked() throws PickedDeckException{       //invoked when a player releases a deck
        if(!picked){
            throw new PickedDeckException("The deck has been picked by another player");
        }
        this.picked = false;
    }

    public EventCard drawCard() throws EmptyDeckException {     //removes and returns the last card of the deck
        if (cards.isEmpty()) {
            throw new EmptyDeckException("Deck is empty");
        }
        else {
            return cards.removeLast();
        }

    }
    public void shuffle(){      //shuffles the cards in the deck
        Collections.shuffle(cards);
    }

}

