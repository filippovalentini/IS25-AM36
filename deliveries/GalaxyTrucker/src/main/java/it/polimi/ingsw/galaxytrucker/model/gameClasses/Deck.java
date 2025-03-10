package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.eventCardClasses.EventCard;
import it.polimi.ingsw.galaxytrucker.model.exceptions.EmptyDeckException;

import java.util.Collections;
import java.util.List;

public class Deck {
    private List<EventCard> cards;
    private int numberCards;
    public boolean picked;

    public Deck(List<EventCard> cards) {
        this.cards = cards;
        this.numberCards = cards.size();
        this.picked = false;
    }
    public List<EventCard> getCards() {
        return cards;
    }
    public int getNumberCards() {
        return numberCards;
    }
    public boolean isPicked() {
        return picked;
    }
    public void setPicked() {
        this.picked = true;
    }
    public EventCard drawCard() throws EmptyDeckException {
        if (numberCards == 0) {
            throw new EmptyDeckException("Deck is empty");
        }
        else {
            return cards.removeLast();
        }

    }
    //randomize deck's cards
    public void shuffle(){
        Collections.shuffle(cards);
    }

}

