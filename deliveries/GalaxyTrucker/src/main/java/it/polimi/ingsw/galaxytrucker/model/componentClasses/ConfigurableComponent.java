package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.List;

public class ConfigurableComponent extends Component {
    protected boolean isDouble;

    public ConfigurableComponent(boolean isDouble, String imagePath, List<Connector> sides) {
        super(imagePath, sides);
        this.isDouble = isDouble;
    }
    public boolean isDouble() {
        return isDouble;
    }
}
