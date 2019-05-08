package view;
import board.Board;
import board.Position;

import java.util.*;

/**
 * TODO ancora da finire la CLI completa
 */
public class BoardView {
    private Board board;
    private int N = 32;
    private int M = 8;

    /**
     * Default constructor
     */
    public BoardView() {
        //TODO implement here
    }

    public void setBoard(Board board){this.board = board;}

    public void drawCLI() {
        printHighBoard();
        for(int i = 0; i < 3; i++){
            for(int column = 0; column < M; column++){
                printThings(column);}
            printLowBoard();
        }
    }

    public void drawCLI2() {
        printHighBoard2();
        for(int column = 0; column < M; column++){
            printThings2();}
        printLowBoard2();
        for(int i = 0; i < 2; i++){
            for(int column = 0; column < M; column++){
                printThings(column);}
            printLowBoard();
        }
    }

    public void drawCLI3() {
        printHighBoard2();
        for(int column = 0; column < M; column++){
            printThings2();}
        printLowBoard2();

        for(int column = 0; column < M; column++)
            printThings(column);
         printLowBoard();
        for(int column = 0; column < M; column++)
            printThings3();
        printLowBoard3();
    }

        /*
        System.out.println
               colore, il tipo di cella, la lista di giocatori, has ammo card, armi
*/

    /**
     * This function draws the high board of the map
     */
    public void printHighBoard(){
        for (int HighBoard = 0; HighBoard < N-2; HighBoard++) {
            System.out.print(" _ _");
        }
        System.out.print("\n");
    }


    public void printHighBoard2(){

        for (int HighBoard = 0; HighBoard < N-M-2; HighBoard++) {
            System.out.print(" _ _");
        }
        System.out.print("\n");
    }

    /**
     * This function draws the lower board of a line of cells
     */
    public void printLowBoard(){
        for (int line = 0; line < N; line++) {
            if (line % M == 0 && line != N - 1)
                System.out.print("|_");
            else {
                if (line == N - 1)
                    System.out.print(" _ _ |");
                else System.out.print(" _ _");
            }
        }
        System.out.print("\n");
    }

    public void printLowBoard2(){
        for (int line = 0; line < N; line++) {
            if (line % M == 0 && line != N - 1)
                System.out.print("|_");
            else {
                if (line == N-1 && line != N-1)
                    System.out.print(" _ _ |");
                else System.out.print(" _ _");
            }
        }
        System.out.print("\n");
    }

    public void printLowBoard3() {
        for(int line = 0; line < M-1; line++){
            System.out.print("    ");
        }
        System.out.print("  ");
        for (int line = M; line < N; line++) {
            if (line % M == 0 && line != N - 1)
                System.out.print("|_");
            else {
                if (line == N-1)
                    System.out.print(" _ _ |");
                else System.out.print(" _ _");
            }
        }
        System.out.print("\n");
    }


    /**
     * This functions draws the middle of the map and indicates some details of every cell
     */
    public void printThings(int column){

        for (int line = 0; line < N; line++) {
            if (line % M == 0 && line != N - 1) {
                if(line == 0)
                    System.out.print("| ");
                else {if(column%8 == 0)
                    printColor(line/M, column/M);}
                printName(line/M, column/M);
            }
            else {
                if (line == N - 1)
                    printColor(line/M, column);
                else System.out.print("    ");
            }
        }
        System.out.print("\n");
    }

    public void printThings2(){

        for (int line = 0; line < N-M; line++) {
            if (line % M == 0 && line != N - 1)
                System.out.print("| ");
            else {
                if (line == N - M - 1)
                    System.out.print("    |");
                else System.out.print("    ");
            }
        }
        System.out.print("\n");
    }

    public void printThings3() {

        for(int line = 0; line < M-1; line++) {
            System.out.print("    ");
        }
        System.out.print("  ");
        for (int line = M; line < N; line++) {
        if (line % M == 0 && line != N - 1)
            System.out.print("| ");
        else {
            if (line == N-1)
                System.out.print("     |");
            else System.out.print("    ");
        }
    }
        System.out.print("\n");
    }

    public void printColor(int i, int j){
        System.out.print(board.getBillboard().getCellFromPosition(new Position(i, j)).getColor() + "|");
    }


    /**
     * This function prints the name of a player
     * @param i line at which the player is
     * @param j column of the position of the player
     */
    public void printName(int i, int j){
        if(board.getBillboard().getCellFromPosition(new Position(i, j)).getPawns().size() <= j+1)
            System.out.print(board.getBillboard().getCellFromPosition(new Position(i, j)).getPawns().get(j).getPlayer().getName());
    }

    public void drawGUI() {
        // TODO implement here

    }
}