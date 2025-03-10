package it.polimi.ingsw.galaxytrucker.model.eventCardClasses;

import it.polimi.ingsw.galaxytrucker.model.gameClasses.GameState;

//EVENT CARD
public abstract class EventCard {
    protected final String imagePath;

    public EventCard(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getImagePath() {
        return imagePath;
    }
    public abstract void solve(GameState gameState);       //applies the effect of the card to the players
                                        //involved in the game
}












