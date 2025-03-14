package it.polimi.ingsw.galaxytrucker.model.componentClasses;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Connector;

import java.util.List;

public class ConfigurableComponent extends Component {      //classes of components which can have 2 configurations
    protected boolean isDouble;     //true if the component is "double" (if false, the component can be single or triple depending on its type)

    public ConfigurableComponent(boolean isDouble, int imageID, List<Connector> sides) {       //constructor
        super(imageID, sides);
        this.isDouble = isDouble;
    }
    public boolean getIsDouble() {
        return isDouble;
    }       //determines if the component configuration is double or not
}
