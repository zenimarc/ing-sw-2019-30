package view;
import player.Player;

/**
 * 
 */
public class PlayerBoardView {
    private Player player;
    private static int CELL_LENGTH=8;
    private static int CELL_HEIGHT=8;
    private static int COLS=12;
    private static int ROWS=2;
    private static char H_SEPARATOR = '─';
    private static char V_SEPARATOR = '│';
    private static char TOP_LEFT_SEPARATOR = '┌';
    private static char TOP_RIGHT_SEPARATOR = '┐';
    private static char BOTTOM_LEFT_SEPARATOR = '└';
    private static char BOTTOM_RIGHT_SEPARATOR = '┘';
    private static char NEW_ROW_LEFT_SEPARATOR = '├';
    private static char NEW_ROW_RIGHT_SEPARATOR = '┤';
    private static char NEW_COL_SEPARATOR_TOP = '┬';
    private static char NEW_COL_SEPARATOR_MIDDLE = '┼';
    private static char NEW_COL_SEPARATOR_BOTTOM = '┴';




    /**
     * Default constructor
     */
    public PlayerBoardView(Player player) {
        this.player = player;
    }

    public void drawDamageTrack() {
        StringBuilder stringBuilder = new StringBuilder();
        //NAME
        stringBuilder.append(player.getName()+"'s damage track:"+System.getProperty("line.separator"));
        //TOP BORDER
        stringBuilder.append(printBordCell(TOP_LEFT_SEPARATOR, H_SEPARATOR));
        for (int i=0; i<COLS-2; i++ ){
            stringBuilder.append(printBordCell(H_SEPARATOR, H_SEPARATOR));
        }
        stringBuilder.append(printBordCell(H_SEPARATOR, TOP_RIGHT_SEPARATOR));

        //HEADER
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(printBodyCell(V_SEPARATOR, '\u0000', ""));
        for (int i=1; i<COLS-1; i++)
            switch(i) {
                case 2:
                    stringBuilder.append(printBodyCell(V_SEPARATOR, ' ', "GRAB+>"));
                    break;
                case 5:
                    stringBuilder.append(printBodyCell(V_SEPARATOR, ' ', "SHOOT+>"));
                    break;
                default:
                    stringBuilder.append(printBodyCell(' ', ' ', ""));
            }
        stringBuilder.append(printBodyCell(' ', ' ', ""));
        stringBuilder.append(V_SEPARATOR);

        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(printBordCell(TOP_LEFT_SEPARATOR, NEW_COL_SEPARATOR_TOP));
        for (int i=0; i<COLS-2; i++ ){
            stringBuilder.append(printBordCell(H_SEPARATOR, NEW_COL_SEPARATOR_TOP));
        }
        stringBuilder.append(printBordCell(H_SEPARATOR, TOP_RIGHT_SEPARATOR));

        //CELL BODY
        stringBuilder.append(System.getProperty("line.separator"));
        //First element
        if(!player.getPlayerBoard().getDamageTrack().isEmpty())
            stringBuilder.append(printBodyCell(V_SEPARATOR, V_SEPARATOR, 1+" "+player.getPlayerBoard().getDamageTrack().get(0).getName()));
        else
            stringBuilder.append(printBodyCell(' ', V_SEPARATOR, "X"));
        //other elements
        for (int i=1; i<COLS-1; i++)
            if(player.getPlayerBoard().getDamageTrack().size() > i)
                stringBuilder.append(printBodyCell(' ', V_SEPARATOR, (i+1)+" "+player.getPlayerBoard().getDamageTrack().get(i).getName()));
            else
                stringBuilder.append(printBodyCell(' ', V_SEPARATOR, "X"));
        //last element
        if(player.getPlayerBoard().getDamageTrack().size() > COLS-1)
            stringBuilder.append(printBodyCell(' ', V_SEPARATOR, COLS+" "+player.getPlayerBoard().getDamageTrack().get(COLS-1).getName()));
        else
            stringBuilder.append(printBodyCell(' ', V_SEPARATOR, "X"));

        //BOTTOM BORDER
        stringBuilder.append(System.getProperty("line.separator"));
        stringBuilder.append(printBordCell(BOTTOM_LEFT_SEPARATOR, NEW_COL_SEPARATOR_BOTTOM));
        for (int i=0; i<COLS-2; i++ ){
            stringBuilder.append(printBordCell(H_SEPARATOR, NEW_COL_SEPARATOR_BOTTOM));
        }
        stringBuilder.append(printBordCell(H_SEPARATOR, BOTTOM_RIGHT_SEPARATOR));

        System.out.print(stringBuilder.toString());




    }
    public String printBodyCell(char leftChar, char rightChar, String text){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(leftChar);
        if (text == null || text.equals(""))
            for(int i=0; i<CELL_LENGTH; i++)
                stringBuilder.append(' ');
        else {
            stringBuilder.append(text.substring(0, Math.min(CELL_LENGTH - 1, text.length())));
            for(int i=0; i<CELL_LENGTH-Math.min(CELL_LENGTH - 1, text.length());i++)
                stringBuilder.append(' ');
        }
        stringBuilder.append(rightChar);
        return stringBuilder.toString();
    }

    public String printBordCell(char leftChar, char rightChar){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(leftChar);
        for(int i=0; i<CELL_LENGTH; i++)
            stringBuilder.append(H_SEPARATOR);
        stringBuilder.append(rightChar);
        return stringBuilder.toString();
    }


    public void drawGUI() {
        // TODO implement here

    }
}