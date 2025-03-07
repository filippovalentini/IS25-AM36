package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.List;

public class LifeSupport extends Component {
    private final boolean isPurple;

    public LifeSupport(boolean isPurple, String imagePath, List<Connector> sides) {
        super(imagePath, sides);
        this.isPurple = isPurple;
    }
    public boolean isPurple() {
        return isPurple;
    }
}
