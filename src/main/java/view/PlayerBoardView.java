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
        //HEADER
        System.out.println(player.getName()+"'s damage track:");
        //TOP BORDER
        printBordCell(TOP_LEFT_SEPARATOR, NEW_COL_SEPARATOR_TOP);
        for (int i=0; i<COLS-2; i++ ){
            printBordCell(H_SEPARATOR, NEW_COL_SEPARATOR_TOP);
        }
        printBordCell(H_SEPARATOR, TOP_RIGHT_SEPARATOR);

        //CELL BODY
        System.out.print(System.getProperty("line.separator"));
        //First element
        if(!player.getPlayerBoard().getDamageTrack().isEmpty())
            printBodyCell(V_SEPARATOR, V_SEPARATOR, 1+" "+player.getPlayerBoard().getDamageTrack().get(0).getName());
        else
            printBodyCell(' ', V_SEPARATOR, "X");
        //other elements
        for (int i=1; i<COLS-1; i++)
            if(player.getPlayerBoard().getDamageTrack().size() > i)
                printBodyCell(' ', V_SEPARATOR, (i+1)+" "+player.getPlayerBoard().getDamageTrack().get(i).getName());
            else
                printBodyCell(' ', V_SEPARATOR, "X");
        //last element
        if(player.getPlayerBoard().getDamageTrack().size() > COLS-1)
            printBodyCell(' ', V_SEPARATOR, COLS+" "+player.getPlayerBoard().getDamageTrack().get(COLS-1).getName());
        else
            printBodyCell(' ', V_SEPARATOR, "X");

        //BOTTOM BORDER
        System.out.print(System.getProperty("line.separator"));
        printBordCell(BOTTOM_LEFT_SEPARATOR, NEW_COL_SEPARATOR_BOTTOM);
        for (int i=0; i<COLS-2; i++ ){
            printBordCell(H_SEPARATOR, NEW_COL_SEPARATOR_BOTTOM);
        }
        printBordCell(H_SEPARATOR, BOTTOM_RIGHT_SEPARATOR);




    }
    public void printBodyCell(char leftChar, char rightChar, String text){
        System.out.print(leftChar);
        if (text == null || text.equals(""))
            for(int i=0; i<CELL_LENGTH-1; i++)
                System.out.print(' ');
        else {
            System.out.print(text.substring(0, Math.min(CELL_LENGTH - 1, text.length())));
            for(int i=0; i<CELL_LENGTH-Math.min(CELL_LENGTH - 1, text.length());i++)
                System.out.print(' ');
        }
        System.out.print(rightChar);
    }

    public void printBordCell(char leftChar, char rightChar){
        System.out.print(leftChar);
        for(int i=0; i<CELL_LENGTH; i++)
            System.out.print(H_SEPARATOR);
        System.out.print(rightChar);
    }

    public void drawGUI() {
        // TODO implement here

    }
}