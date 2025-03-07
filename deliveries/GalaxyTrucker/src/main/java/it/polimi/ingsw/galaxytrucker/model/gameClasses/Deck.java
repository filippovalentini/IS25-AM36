package it.polimi.ingsw.galaxytrucker.model.gameClasses;

import it.polimi.ingsw.galaxytrucker.model.eventCardClasses.EventCard;

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
    public EventCard drawCard(){
        return cards.removeLast();
    }
    public void shuffle(){}
}
