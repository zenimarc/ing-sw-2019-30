package view;
import board.Board;

import java.util.*;

/**
 * 
 */
public class BoardView {
    private Board board;
    private int N = 28;
    private int M = 7;

    /**
     * Default constructor
     */
    public BoardView() {
        //TODO implement here
    }

    public void drawCLI() {/*
        for (int HighBoard = 0; HighBoard < N-2; HighBoard++) {
            System.out.print(" _ _");
        }

        for (int column = 1; column < N+1; column++) {
            System.out.print("\n");
            if ((column % M == 0)) {
                for (int line = 0; line < N; line++) {
                    if (line % M == 0 && line != N - 1)
                        System.out.print("|_");
                    else {
                        if (line == N - 1)
                            System.out.print(" _ _ |");
                        else System.out.print(" _ _");
                    }
                }
            }
            else{
                for (int line = 0; line < N; line++) {
                    if (line % M == 0 && line != N - 1)
                        System.out.print("| ");
                    else {
                        if (line == N - 1)
                            System.out.print("     |");
                        else System.out.print("    ");
                    }
                }
            }
        }
         */
        printHighBoard();
        for(int i = 0; i < 4; i++){
            for(int column = 0; column < M; column++){
                printThings();}
            printLowBoard();
        }
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

    /**
     * This functions draws the middle of the map and indicates some details of every cell
     */
    public void printThings(){

        for (int line = 0; line < N; line++) {
            if (line % M == 0 && line != N - 1)
                System.out.print("| ");
            else {
                if (line == N - 1)
                    System.out.print("     |");
                else System.out.print("    ");
            }
        }
        System.out.print("\n");
    }

    public void printColor(int column, int line){

    }

    public void drawGUI() {
        // TODO implement here

    }
}