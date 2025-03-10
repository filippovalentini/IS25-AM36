package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.List;

public class LifeSupport extends Component {
    private final boolean isPurple;     //life supports for aliens can be purple or brown

    public LifeSupport(boolean isPurple, String imagePath, List<Connector> sides) {     //constructor
        super(imagePath, sides);
        this.isPurple = isPurple;
    }
    public boolean isPurple() {
        return isPurple;
    }
}
