package view;
import player.Player;

import java.util.*;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * 
 */
public class PlayerBoardView {
    private Player player;
    private static int CELL_LENGTH=3;
    private static int CELL_HEIGHT=3;
    private static int COLS=6;
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
        boolean firstRow = TRUE;
        boolean firstCol = TRUE;

        for (Player player : player.getPlayerBoard().getDamageTrack()) {
            for (int i=0; i<COLS-1; i++ ){
                printHorBordCell(TOP_LEFT_SEPARATOR, NEW_COL_SEPARATOR1);
            }
            printHorBordCell(TOP_LEFT_SEPARATOR, TOP_RIGHT_SEPARATOR);
        }
    }

    public void printHorBordCell(char left, char right){
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

}