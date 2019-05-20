package constants;

/**
 * Color is an enumeration used to distinguish rooms in the board and the color of the bullets
 */
public enum Color {
    RED("RED"),
    YELLOW("YLL"),
    BLUE("BLU"),
    GREEN("GRN"),
    PURPLE("PRP"),
    WHITE("WHT");

    private String abbreviation;

    Color(String abbreviation){this.abbreviation = abbreviation;}

    public String getAbbreviation() {
        return abbreviation;
    }
}