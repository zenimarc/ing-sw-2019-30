package view;
import board.*;
import weapon.WeaponCard;

import java.util.*;

/**
 * TODO sistemare gli spazi e le |, in particolare guardare perchè alla fine non printa le |
 */
public class BoardView {
    private Board board;
    private int N = 24;
    private int M = 8;
    private int L = 5;

    /**
     * Default constructor
     */
    public BoardView() {
        //TODO implement here
    }

    public void setBoard(Board board){this.board = board;}

    /**
     * This functions draws a specific map
     */

    public void drawCLI() {
    }
    public void drawCLI2() {

    }
    public void drawCLI3() {

    }

    /**
     * This function draws the high board of the map
     * @param column set to 0 if the map has the first cell, else 8
     */
    public void printHighBoard(int column){
        for (int Board = 0; Board < column; Board++) {
            System.out.print("    ");
        }
        for (int Board = column; Board < N-2; Board++) {
            System.out.print("____");
        }
        System.out.print("\n");
    }

    /**
     * This function draws the low board of a cell
     * @param column set to 0 if the map has the last cell, else 8
     */
    public void printLowBoard(int column){
        for (int Board =0; Board < N-column-2; Board++) {
            if(Board%8 == 0)
                System.out.print("│___");
            else System.out.print("____");
        }
        if(column == 0)
            System.out.print("│");
        System.out.print("\n");
    }

    public void printThings(int line) {
        for (int column = 0; column < N; column++) {
            if (column % M == 0 && column != N - 1) {
                if (column == 0) {
                    System.out.print("│ ");
                    printName(line, column / M);
                    column = column + L;
                } else {
                    if (column % 8 == 0 && line % 8 == 0) {
                        printColor(line, (column / M) - 1);
                        column = column + L;
                    }

                    else System.out.print("      │");
                }
            } else {
                if (column == N - 1 && line % 8 == 0) {
                    if(line % 8 == 0)
                        printColor(line, column / M);
                    else System.out.print("           │");
                } else System.out.print("     ");
            }
        }
        System.out.print("\n");
    }

    public void printCards(int line, int column) {
        if (board.getBillboard().getCellFromPosition(new Position(line / M, column)).getClass() == NormalCell.class)
            printAmmo((NormalCell) board.getBillboard().getCellFromPosition(new Position(line / M, column)));
        else printWeapons((RegenerationCell) board.getBillboard().getCellFromPosition(new Position(line / M, column)));
    }

    public void printColor(int line, int column){
        for(int i = board.getBillboard().getCellFromPosition(new Position(line/M, column)).getColor().name().length(); i< 10; i++)
            System.out.print(" ");
        System.out.print(board.getBillboard().getCellFromPosition(new Position(line/M, column)).getColor() + "|");
    }


    /**
     * This function prints the name of a player
     * @param line line at which the player is
     * @param column column of the position of the player
     */
    public void printName(int line, int column){
        if (line + 1 <= board.getBillboard().getCellFromPosition(new Position(line/M, column)).getPawns().size()){
            System.out.print(board.getBillboard().getCellFromPosition(new Position(line/M, column)).getPawns().get(line).getPlayer().getName());
            for (int f = board.getBillboard().getCellFromPosition(new Position(line/M, column)).getPawns().get(column).getPlayer().getName().length(); f < 10; f++)
                System.out.print(" ");}
        else  for (int f = 0; f < 10; f++)
            System.out.print(" ");
    }

    public void printAmmo(Cell cell){
        if (cell.getCard(0) != null)
            System.out.print("│Ammo ");
        else System.out.print("│     ");
        for(int j = L; j < N; j++)
            System.out.print(" ");
        System.out.print("│");
    }
    public void printWeapons(Cell cell){
        for(int i = 0; i < 3; i++) {
            if (cell.getCard(i) != null){
                printWeaponName((WeaponCard) cell.getCard(i));
                if(i != 2)
                    System.out.print(" ");}
            else for(int j = 0; j < 10; j++)
                System.out.print(" ");
        }
        System.out.print("│");
    }

    public void printWeaponName(WeaponCard weapon){
        System.out.print(weapon.getName() + " ");
        for(int j = 0; j < weapon.getName().length(); j++)
            System.out.print(" ");
    }

    public void drawGUI() {
        // TODO implement here

    }
}


/*
    ┌──────────────────────────────────────────────────────────────────────────┐
    │ Table Heading                                                            │
    ├──────────────────┬──────────────────┬──────────────────┬─────────────────┤
    │ first row (col1) │ with some        │ and more         │ even more       │
    │                  │ information      │ information      │                 │
    ├──────────────────┼──────────────────┼──────────────────┼─────────────────┤
    │ second row       │ with some        │ and more         │ even more       │
    │ (col1)           │ information      │ information      │                 │
    │                  │ (col2)           │ (col3)           │                 │
    └──────────────────┴──────────────────┴──────────────────┴─────────────────┘
 */