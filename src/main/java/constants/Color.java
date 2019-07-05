package constants;

/**
 * Color is an enumeration used to distinguish rooms in the board and the color of the bullets
 */
public enum Color {
    RED("RED", "R"),
    YELLOW("YLL", "Y"),
    BLUE("BLU", "B"),
    GREEN("GRN", "G"),
    PURPLE("PRP", "P"),
    WHITE("WHT", "W");

    private String abbreviation;
    private String firstLetter;

    Color(String abbreviation, String firstLetter){
        this.abbreviation = abbreviation;
        this.firstLetter = firstLetter;}

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public static Color convertToColor(int i){
        if(i == 0)
            return RED;
        else if(i == 1)
            return YELLOW;
        else if(i==2)
            return BLUE;
        else
            return null;
    }
}