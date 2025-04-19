package it.polimi.ingsw.galaxytrucker.model.enumerations;
//this class is used to represent the colors associated to the different players and to the different type of goods
//sored in "CargoHold" components
public enum Color {
    GREEN, BLUE, RED, YELLOW;

    @Override
    public String toString() {
        return switch (this) {
            case RED -> "RED";
            case GREEN -> "GREEN";
            case BLUE -> "BLUE";
            case YELLOW -> "YELLOW";
        };
    }

    //converts a string in the respective Color object
    public static Color convertToColor(String colorString) {
        return switch (colorString) {
            case "RED" -> Color.RED;
            case "GREEN" -> Color.GREEN;
            case "BLUE" -> Color.BLUE;
            case "YELLOW" -> Color.YELLOW;
            default -> null;
        };
    }
}
