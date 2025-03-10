package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

//EVENT CARD
//this class is used to generalize the concept of adventure/event card used during the game; each subclass of "EventCard"
//represents a specific type of card
public abstract class EventCard {
    protected final String imagePath;       //path for the image associated to the adventure card

    public EventCard(String imagePath) {        //constructor
        this.imagePath = imagePath;
    }
    public String getImagePath() {
        return imagePath;
    }

    //applies the effect of the card to the players involved in the game; this abstract method is defined in different
    //ways by the different adventure card subclasses, according to their specific characteristics and to the user interactions
    //that they require
    public abstract void solve(GameState gameState);
}












