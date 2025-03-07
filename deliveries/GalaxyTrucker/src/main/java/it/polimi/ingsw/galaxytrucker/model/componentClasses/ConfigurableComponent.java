package it.polimi.ingsw.galaxytrucker.model.componentClasses;

public class ConfigurableComponent extends Component {
    protected boolean isDouble;

    public ConfigurableComponent(boolean isDouble) {
        super();
        this.isDouble = isDouble;
    }
    public boolean isDouble() {
        return isDouble;
    }
}
