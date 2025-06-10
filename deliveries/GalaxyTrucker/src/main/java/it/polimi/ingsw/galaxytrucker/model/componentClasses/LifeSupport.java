package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.ArrayList;
import java.util.List;

public class LifeSupport extends Component {
    private final boolean isPurple;     //life supports for aliens can be purple or brown

    public LifeSupport(boolean isPurple, int imageID, List<Connector> sides) {     //constructor
        super(imageID, sides);
        this.isPurple = isPurple;
    }
    public boolean isPurple() {
        return isPurple;    //returns true if the life support is purple, false otherwise
    }

    @Override
    public Component clone() {//return a copy of the component
        LifeSupport retComponent = new LifeSupport(isPurple, this.imageID, new ArrayList<>(this.sides));
        retComponent.orientation = this.orientation;
        return retComponent;
    }
    @Override
    public boolean supportsAlien(boolean purpleAlien) {
        return this.isPurple == purpleAlien;
    }
}
