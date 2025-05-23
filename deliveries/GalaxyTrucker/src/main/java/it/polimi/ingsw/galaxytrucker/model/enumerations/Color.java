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

    //converts a color in an emoji
    public static String convertColorIntoEmoji(Color color) {
        return switch (color) {
            case Color.RED -> "🟥";
            case Color.GREEN -> "🟩";
            case Color.BLUE -> "🟦";
            case Color.YELLOW -> "🟨";
        };
    }

    //converts a color in an emoji
    public static Color convertEmojiIntoColor(String color) {
        return switch (color) {
            case "🟥" -> Color.RED;
            case "🟩" -> Color.GREEN;
            case "🟦" -> Color.BLUE;
            case "🟨" -> Color.YELLOW;
            default -> null;
        };
    }

    //converts a color into a letter
    public static String convertColorIntoLetter(Color color) {
        return switch (color) {
            case Color.RED -> "r";
            case Color.GREEN -> "g";
            case Color.BLUE -> "b";
            case Color.YELLOW -> "y";
        };
    }
}
