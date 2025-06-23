package it.polimi.ingsw.galaxytrucker.model.enumerations;

/**
 * this class is used to represent the colors associated to the different players and to the different type of goods
 * sored in "CargoHold" components
 */
public enum Color {
    GREEN, BLUE, RED, YELLOW;
    /**
     * this method is used to convert the color into a string
     * @return the string representation of the color
     */
    @Override
    public String toString() {
        return switch (this) {
            case RED -> "RED";
            case GREEN -> "GREEN";
            case BLUE -> "BLUE";
            case YELLOW -> "YELLOW";
        };
    }

    /**
     * this method is used to convert a string into a color
     * @param colorString the string representation of the color
     * @return the color associated to the string, or null if the string is not valid
     */
    public static Color convertToColor(String colorString) {
        return switch (colorString) {
            case "RED" -> Color.RED;
            case "GREEN" -> Color.GREEN;
            case "BLUE" -> Color.BLUE;
            case "YELLOW" -> Color.YELLOW;
            default -> null;
        };
    }

   /**
     * this method is used to convert a color into an emoji representation
     * @param color the color to convert
     * @return the emoji representation of the color
     */
    public static String convertColorIntoEmoji(Color color) {
        return switch (color) {
            case Color.RED -> "🟥";
            case Color.GREEN -> "🟩";
            case Color.BLUE -> "🟦";
            case Color.YELLOW -> "🟨";
        };
    }

    /**
     * this method is used to convert a color into a single letter representation
     * @param color the color to convert
     * @return the single letter representation of the color
     */
    public static String convertColorIntoLetter(Color color) {
        return switch (color) {
            case Color.RED -> "r";
            case Color.GREEN -> "g";
            case Color.BLUE -> "b";
            case Color.YELLOW -> "y";
        };
    }

    /**
     * this method is used to convert a color into a CSS style string
     * @param color the color to convert
     * @return the CSS style string representation of the color
     */
    public static String convertColorIntoStyle(Color color) {
        String colorString = "";
        switch (color) {
            case Color.RED -> colorString ="red";
            case Color.GREEN -> colorString ="green";
            case Color.BLUE -> colorString ="blue";
            case Color.YELLOW -> colorString ="yellow";
        }
        return "-fx-text-fill: " + colorString + ";";
    }
}
