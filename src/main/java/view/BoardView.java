package view;
import board.*;
import weapon.WeaponCard;

import java.util.*;

/**
 * TODO porte, armi e regen
 */
public class BoardView {
    private Board board;
    private int N = 24;
    private int M = 8;
    private int L = 4;
    private int Z = 3;

    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * This function picks the cell with coordinates x and y
     * @param x coordinate
     * @param y coordinate
     * @return cell with x and y coordinate
     */
    public Cell getCell(int x, int y) {
        return board.getBillboard().getCellFromPosition(new Position(x, y));
    }

    /**
     * This functions draws the map
     */

    public void drawCLI() {
        printHighBoard();
        for(int i = 0; i < L; i++){
            for(int x = i*M; x < i*M+M; x ++)
                printThings(x);
            printCards(i);
            if(i < L-1)
                printMiddleBoard(i);
            else printLowBoard();}
    }

    /**
     * This function draws the high board of the map
     */
    public void printHighBoard() {
        for (int Board = 0; Board < N/M; Board++) {
            if(getCell(0, Board).getColor() == null){
                if(Board == 0)
                    System.out.print(" ");
                else if (getCell(N/M, Board-1).getColor() != null)
                    System.out.print("┐");
                for(int i = 0; i < (M+Z)*Z; i++)
                    System.out.print(" ");
            }
                else{
                    if (Board == 0)
                        System.out.print("┌");
                    else if(getCell(0, Board-1).getColor() != null)
                        System.out.print("┬");
            for(int i = 0; i < (M+Z)*Z; i++)
                System.out.print("─");
                }
        }
        if(getCell(0, N/M-1).getColor() != null)
            System.out.print("┐");
        System.out.print("\n");
    }

    /**
     * This function draws the low board of the cells in the middle of the map
     * @param x the number of the board to be printed
     */
    public void printMiddleBoard(int x) {
        for (int Board = 0; Board < N/M; Board++) {
            if (getCell(x, Board).getColor() == null && getCell(x+1, Board).getColor() == null) //caso sopra e sotto son nulle
                for (int i = 0; i < (M+Z)*Z + 1; i++)
                    System.out.print(" ");

            else {
                if(Board == 0) {
                    if (getCell(x, Board).getColor() != null && getCell(x + 1, Board).getColor() == null)//caso solo sotto sia nulla
                        System.out.print("┌");
                    else System.out.print("├");
                }
                for (int i = 0; i < (M+Z)*Z; i++) {
                    if (i >= M+Z && i <= (M+Z)*2) {
                        if (board.getBillboard().hasDoor(getCell(x, Board), getCell(x+1, Board)) || board.getBillboard().hasSameColor(getCell(x, Board), getCell(x+1, Board)))
                            System.out.print(" ");
                        else
                            System.out.print("─");
                    }
                    else System.out.print("─");
                }

                if (Board == N/M-1) {
                    if (getCell(x+1, Board).getColor() == null)
                        System.out.print("┘\n");
                    else System.out.print("┤\n");
                }
                else {
                    if(getCell(x+1, Board).getColor() != null)
                        System.out.print("┼");
                    else System.out.print("┬");}
            }
        }
    }

    /**
     * This function draws the low board of the map
     *
     */
    public void printLowBoard() {
        for (int Board =0; Board < N/M; Board++) {
            if(getCell(N/M, Board).getColor() == null){
                if(Board == 0)
                    System.out.print(" ");
                else if (getCell(N/M, Board-1).getColor() != null)
                    System.out.print("┘");
                for(int i = 0; i < (M+Z)*Z; i++)
                    System.out.print(" ");
            }
            else{
                if (Board == 0)
                    System.out.print("└");
                else if(getCell(N/M, Board-1).getColor() != null)
                    System.out.print("┴");
                for(int i = 0; i < (M+Z)*Z; i++)
                    System.out.print("─");
            }

        }
        if(getCell(N/M, N/M-1).getColor() != null)
            System.out.print("┘");
        System.out.print("\n");
    }

    /**
     * This function draws all the important things needed to understand better the game
     * @param x
     */
    public void printThings(int x) {
        for(int square = 0; square < N/M; square++)
            if(getCell(x/M, square).getColor() == null)
                for(int i = 0; i < N; i++)
                    System.out.print(" ");
            else{
                if(square == 0)
                    System.out.print("│");
                printName(x, square);
                for(int i = 0; i < M+Z; i++)
                    System.out.print(" ");
                if(x%M == 0)
                    printColor(x, square);
                else if(square != N/M -1)
                        printDoors(x, square);
                    else {
                        if(x % M == 1 && getCell(x/M, N/M-1).getClass() == RegenerationCell.class){
                            for (int i = 0; i < M-2; i++)
                                System.out.print(" ");
                            System.out.print("Regen│");
                }
                        for (int i = 0; i < M+Z; i++)
                        System.out.print(" ");}
                if(getCell(x/M, square+1) == null)
                    if(!(x % M == 1 && getCell(x/M, N/M-1).getClass() == RegenerationCell.class))
                        System.out.print("│");
            }
        System.out.print("\n");
    }

    /**
     * This function prints the name of a player
     * @param x coordinate
     * @param y coordinate
     */
    public void printName(int x, int y){
        if(getCell(x/M, y).getPawns().size() > x%M){
            System.out.print(getCell(x/M, y).getPawns().get(x%M).getPlayer().getName());
            for (int f = getCell(x/M, y).getPawns().get(x%M).getPlayer().getName().length(); f < M+Z; f++)
                System.out.print(" ");
        }
        else for (int f = 0; f < M+Z; f++)
            System.out.print(" ");
    }

    /**
     * This function prints the nacolor of the cell
     * @param x coordinate
     * @param y coordinate
     */
    public void printColor(int x, int y){
        if(getCell(x/M, y).getColor() == null){
            for(int i = 0; i< M+Z; i++)
                System.out.print(" ");
        }
        else {
            for(int i = getCell(x/M, y).getColor().name().length(); i< M+Z; i++)
                System.out.print(" ");
            System.out.print(getCell(x/M, y).getColor());
            if(y != N/M - 1)
                System.out.print("│");
        }
    }

    /**
     * This function prints the doors between two adjancent cells with the same x coordinate
     * @param x coordinate
     * @param square y coordinate
     */
    private void printDoors(int x, int square) {
        if (x % M == 1 && getCell(x/M, square).getClass() == RegenerationCell.class){
            for (int i = 0; i < M-2; i++)
                System.out.print(" ");
            System.out.print("Regen│");}
        else if (x%M > L / 2 && x%M < M - L / 2) {
            if (board.getBillboard().hasDoor(getCell(x/M , square), getCell(x/M , square+1))|| board.getBillboard().hasSameColor(getCell(x/M , square), getCell(x/M , square+1)))
                for (int i = 0; i < M+Z+1; i++)
                    System.out.print(" ");
            else {
                for (int i = 0; i < M+Z; i++)
                    System.out.print(" ");
                System.out.print("│");
            }
        }
        else {
            for (int i = 0; i < M+Z; i++)
                System.out.print(" ");
            System.out.print("│");
        }
    }

    /**
     * This function is used to decide what to print based on the type of the cell
     * @param x coordinate
     */
    public void printCards(int x) {
        for (int y = 0; y < N/M; y++) {
            if (getCell(x, y).getColor() == null){
                if(y == 0)
                    System.out.print(" ");
                for(int i = 0; i < (Z+M)*Z; i++)
                    System.out.print(" ");
            if(y != N/M - 1)
                if(getCell(x, y+1).getColor() != null)
                    System.out.print("│");
            }
            else {
                if (y == 0)
                    System.out.print("│");
                if (getCell(x, y).getClass() == NormalCell.class)
                    printAmmo(getCell(x, y));
                else printWeapons(getCell(x, y));
            }
        }
        System.out.print("\n");
    }

    /**
     * This function prints Ammo if the cell has an ammo, else nothing
     * @param cell with needed information
     */
    public void printAmmo(Cell cell){
        if (cell.getCard(0) != null){
            System.out.print("Ammo");
            for (int i = 0; i < Z*2 - 4; i++)
                System.out.print(" ");
        }
        else
            for (int i = 0; i < Z*2; i++)
                System.out.print(" ");

        for(int j = Z*2; j < (M+Z)*Z; j++)
            System.out.print(" ");
        System.out.print("│");
    }

    /**
     * This function verifies if a Regeneration cell has weapons
     * @param cell with needed information
     */
    public void printWeapons(Cell cell){
        for(int i = 0; i < Z; i++) {
            if (cell.getCard(i) != null)
                printWeaponName((WeaponCard) cell.getCard(i));
            else for(int j = 0; j < M+Z; j++)
                System.out.print(" ");
        }
        System.out.print("│");
    }

    /**
     * This function prints the name of the weapon
     * @param weapon name to be printed
     */
    public void printWeaponName(WeaponCard weapon){
        System.out.print(weapon.getName().substring(0, Math.min(weapon.getName().length(), M+2)) + " ");
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