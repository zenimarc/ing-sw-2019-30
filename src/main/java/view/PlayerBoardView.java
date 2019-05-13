package view;
import player.Player;

import static java.lang.Boolean.TRUE;

/**
 * 
 */
public class PlayerBoardView {
    private Player player;
    private static int CELL_LENGTH=12;
    private static int CELL_HEIGHT=6;
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
    private static char NEW_COL_SEPARATOR1 = '┬';
    private static char NEW_COL_SEPARATOR2 = '┼';




    /**
     * Default constructor
     */
    public PlayerBoardView(Player player) {
        this.player = player;
    }

    public void drawDamageTrack() {
        printBordCell(TOP_LEFT_SEPARATOR, NEW_COL_SEPARATOR1);
        for (int i=0; i<COLS-2; i++ ){
            printBordCell(H_SEPARATOR, NEW_COL_SEPARATOR1);
        }
        printBordCell(H_SEPARATOR, TOP_RIGHT_SEPARATOR);
        System.out.print(System.getProperty("line.separator"));
        printBodyCell(V_SEPARATOR, V_SEPARATOR, 1+"test");
        for (int i=0; i<COLS-2; i++)
            printBodyCell(' ', V_SEPARATOR, (i+2)+"test");
        printBodyCell(' ', V_SEPARATOR, COLS+1+"test");




    }
    public void printBodyCell(char left, char right, String text){
        System.out.print(left);
        if (text == null || text.equals(""))
            for(int i=0; i<CELL_LENGTH-2; i++)
                System.out.print(' ');
        else {
            System.out.print(text.substring(0, Math.min(CELL_LENGTH - 2, text.length())));
            for(int i=0; i<CELL_LENGTH-text.length();i++)
                System.out.print(' ');
        }
        System.out.print(right);
    }

    public void printBordCell(char left, char right){
        System.out.print(left);
        for(int i=0; i<CELL_LENGTH; i++)
            System.out.print(H_SEPARATOR);
        System.out.print(right);
    }

    public void drawCLI() {
        System.out.println(" _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ _ \n" +
                "| \t\t|\t       \t |\t\t  |\t\t  |\n" +
                "|\t\t|\t\t |\t\t  |\t\t  |\n" +
                "|\t\t|\t\t |\t\t  |\t\t  |\n" +
                "|\t\t|\t\t |\t\t  |\t\t  |\n" +
                "|\t\t|\t\t |\t\t  |\t\t  |\n" +
                "|\t\t|\t\t |\t\t  |\t\t  |\n" +
                "|_ _ _ _ _ _ _ _|_ _ _ _ _ _ _ _ | _ _ _ _ _ _ _ _|_ _ _ _ _ _ _ _|\n" +
                "|\t\t|\t\t |\t\t  |\t\t  |\n" +
                "|\t\t|\t\t |\t\t  |\t\t  |\n" +
                "|\t\t|\t\t |\t\t  |\t\t  |\n" +
                "|\t\t|\t\t |\t\t  |\t\t  |\n" +
                "|\t\t|\t\t |\t\t  |\t\t  |\n" +
                "|\t\t|\t\t |\t\t  |\t\t  |\n" +
                "|_ _ _ _ _ _ _ _| _ _ _ _ _ _ _ _|_ _ _ _ _ _ _ _ |_ _ _ _ _ _ _ _|\n" +
                "|\t\t|\t\t |\t\t  |\t\t  |\n" +
                "|\t\t|\t\t |\t\t  |\t\t  |\n" +
                "|\t\t|\t\t |\t\t  |\t\t  |\n" +
                "|\t\t|\t\t |\t\t  |\t\t  |\n" +
                "|\t\t|\t\t |\t\t  |\t\t  |\n" +
                "|_ _ _ _ _ _ _ _|_ _ _ _ _ _ _ _ |_ _ _ _ _ _ _ _ |_ _ _ _ _ _ _ _|");
//TODO implement here
    }


    public void drawGUI() {
        // TODO implement here

    }

    public static void main (String[] args){
        PlayerBoardView playerBoardView = new PlayerBoardView(new Player("Marco"));
        playerBoardView.drawDamageTrack();
    }
}