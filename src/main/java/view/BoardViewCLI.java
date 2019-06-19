package view;
import board.*;
import board.Cell;
import board.NormalCell;
import board.RegenerationCell;
import deck.AmmoCard;
import weapon.WeaponCard;

import java.util.Observable;
import java.util.Observer;

import static constants.Constants.MAX_WEAPON_REGENERATIONCELL;

/**
 * BoardViewCLI is used to draw the map of the game. It can draw Cli or GUI version
 */
public class BoardViewCLI implements Observer {
    private Board board;
    private int horizontalCells = 4; //Number of horizontal cells
    private int verticalCells = 3;  //Number of vertical cells
    private int numLength = 6; //Size of vertical board and at least it must be > doorSize+2
    private int doorSize = 3; //variable used for drawing doors
    private int length = 3; //variable used to print in a proportional way

    public BoardViewCLI(Board board){
        this.board = board;
    }

    /**
     * This function picks the cell with coordinates x and y
     * @param x coordinate
     * @param y coordinate
     * @return cell with x and y coordinate
     */
    private Cell getCell(int x, int y) {
        return board.getBillboard().getCellFromPosition(new Position(x, y));
    }

    /**
     * This functions draws the map
     */

    public void drawCLI() {
        printHighBoard();
        for(int i = 0; i < verticalCells; i++){
            for(int x = i* numLength; x < i* numLength + numLength -1; x++)
                printThings(x);
            printCards(i);
            if(i < verticalCells -1)
                printMiddleBoard(i);
            else printLowBoard();}
    }

    /**
     * This function draws the high board of the map
     */
    private void printHighBoard() {
        StringBuilder stream = new StringBuilder();
        for (int Board = 0; Board < horizontalCells; Board++) {
            if(getCell(0, Board) == null){
                if(Board == 0)
                    stream.append(" ");
                else if(getCell(0, Board-1) == null)
                    stream.append(" ");
                for(int i = 0; i < (numLength + length)* length; i++)
                    stream.append(" ");
            }
            else{
                if (Board == 0)
                    stream.append("┌");
                else if(getCell(0, Board-1) == null)
                    stream.append(" ");
                for(int i = 0; i < (numLength + length)* length; i++)
                    stream.append("─");
                if(getCell(0, Board+1) == null)
                    stream.append("┐");
                else stream.append("┬");
            }
        }
        stream.append("\n");
        System.out.print(stream);
    }

    /**
     * This function draws the low board of the cells in the middle of the map
     * @param x the number of the board to be printed
     */
    private void printMiddleBoard(int x) {
        StringBuilder stream = new StringBuilder();

        if (getCell(x, 0) == null) //Cell is null
            if(getCell(x+1, 0) == null)
                stream.append(" ");
            else stream.append("┌");
        else //cell is not null
            if(getCell(x+1, 0) == null)
                stream.append("└");
            else stream.append("├");

        for (int Board = 0; Board < horizontalCells; Board++) {

            for (int i = 0; i < (numLength + length) * length; i++) {
                if (getCell(x + 1, Board) == null && getCell(x, Board) == null)
                    stream.append(" ");
                else if (getCell(x + 1, Board) == null || getCell(x, Board) == null)
                        stream.append("─");
                     else {
                        if (i >= numLength + length && i <= (numLength + length) * 2) {
                            if (board.getBillboard().hasDoor(getCell(x, Board), getCell(x + 1, Board)) || board.getBillboard().hasSameColor(getCell(x, Board), getCell(x + 1, Board)))
                                stream.append(" ");
                            else
                                stream.append("─");
                         }
                        else stream.append("─");
                    }
                }

            if(Board != horizontalCells -1){
                if(getCell(x, Board) == null){//cella in cui si è è nulla
                    if(getCell(x,Board+1) != null && getCell(x+1,Board) != null) //cella in diagonale nulla
                        stream.append("┼");
                    else if(getCell(x+1,Board+1) != null){
                        if(getCell(x+1,Board) == null){
                            if(getCell(x,Board+1) == null)
                                    stream.append("┌");
                                else stream.append("┬");
                            }
                            else stream.append("├");

                        }
                         else if(getCell(x+1,Board) != null)
                             stream.append("┐");
                             else if(getCell(x,Board+1) != null)
                                stream.append("┘");
                                    else stream.append(" ");
                }

                else{
                    if(getCell(x+1,Board+1) != null ||(getCell(x+1,Board) != null && getCell(x,Board+1) != null))
                        stream.append("┼");
                    else{
                        if(getCell(x,Board+1) != null)
                            stream.append("┴");
                        else if (getCell(x+1,Board) != null)
                            stream.append("┤");
                        else
                            stream.append("┌");
                    }
                }
            }
        }

        if(getCell(x, horizontalCells -1) != null){
            if(getCell(x+1, horizontalCells -1) != null)
                stream.append("┤");
            else  stream.append("┘");}
        else {
            if (getCell(x + 1, horizontalCells -1) != null)
                stream.append("┐");
            else stream.append(" ");
        }

        stream.append("\n");
        System.out.print(stream);
    }

    /**
     * This function draws the low board of the map
     */
    private void printLowBoard() {
        StringBuilder stream = new StringBuilder();
        for (int Board = 0; Board < horizontalCells; Board++) {
        if(getCell(verticalCells -1, Board) == null){
            if(Board == 0)
                stream.append(" ");
            else if(getCell(verticalCells -1, Board-1) == null)
                stream.append(" ");
            for(int i = 0; i < (numLength + length)* length; i++)
                stream.append(" ");
        }
        else{
            if (Board == 0)
                stream.append("└");
            else if(getCell(verticalCells -1, Board-1) == null)
                stream.append("└");

            for(int i = 0; i < (numLength + length)* length; i++)
                stream.append("─");
            if(getCell(verticalCells -1, Board+1) == null)
                stream.append("┘");
            else stream.append("┴");
        }
    }
        stream.append("\n");
        System.out.print(stream);
}

    /**
     * This function draws all the important things needed to understand better the game
     * @param x number of line to be printed
     */
    private void printThings(int x) {
        StringBuilder stream = new StringBuilder();
        for(int square = 0; square < horizontalCells; square++)
            if(getCell(x/ numLength, square) == null){
                for(int i = 0; i < (numLength + length)* length +1; i++)
                    stream.append(" ");
                if (getCell(x/ numLength, square+1) != null)
                    stream.append("│");
                else stream.append(" ");
            }
            else{
                if(square == 0)
                    stream.append("│");
                stream.append(printName(x, square));
                for(int i = 0; i < numLength + length; i++)
                    stream.append(" ");
                if(x% numLength == 0)
                    stream.append(printColor(x, square));
                else
                    stream.append(printDoors(x, square));
            }
        stream.append("\n");
        System.out.print(stream);
    }

    /**
     * This function prints the name of a player
     * @param x coordinate
     * @param y coordinate
     * @return a stream with infos
     */
    private StringBuilder printName(int x, int y){
        StringBuilder stream = new StringBuilder();
        if(getCell(x/ numLength, y).getPawns().size() > x% numLength){
            String name = (getCell(x/ numLength, y).getPawns().get(x% numLength).getPlayer().getName().substring(0, Math.min(getCell(x/ numLength, y).getPawns().get(x% numLength).getPlayer().getName().length(), numLength + length -1)));
            stream.append(name);
            for (int f = name.length(); f < numLength + length; f++)
                stream.append(" ");
        }
        else for (int f = 0; f < numLength + length; f++)
            stream.append(" ");
        return stream;
    }

    /**
     * This function prints the color of the cell
     * @param x coordinate
     * @param y coordinate
     * @return a stream with infos
     */
    private StringBuilder printColor(int x, int y){
        StringBuilder stream = new StringBuilder();
        if(getCell(x/ numLength, y) == null){
            for(int i = 0; i< numLength + length; i++)
                stream.append(" ");
        }
        else {
            for(int i = getCell(x/ numLength, y).getColor().name().length(); i< numLength + length; i++)
                stream.append(" ");
            stream.append(getCell(x/ numLength, y).getColor());

                stream.append("│");
        }
        return stream;
    }

    /**
     * This function prints the doors between two adjancent cells with the same x coordinate
     * @param x coordinate
     * @param square y coordinate
     * @return a stream with infos
     */
    private StringBuilder printDoors(int x, int square) {
        StringBuilder stream = new StringBuilder();
        if (x % numLength == 1){
            for(int i = 0; i < numLength + length -board.getBillboard().getCellPosition(getCell(x/ numLength, square)).toString().length(); i++)
                stream.append(" ");
            stream.append(printCoordinates(x, square));
            stream.append("│");
            }
        else if (x% numLength > doorSize / 2 && x% numLength < numLength - doorSize / 2) {
            if(x % numLength == 2 && getCell(x/ numLength, square).getClass() == RegenerationCell.class){
                for (int i = 0; i < numLength -2; i++)
                    stream.append(" ");
                stream.append("Regen");}
            else for (int i = 0; i < numLength + length; i++)
                stream.append(" ");
            if(square == horizontalCells || getCell(x/ numLength, square+1) == null)
                stream.append("│");
            else if (board.getBillboard().hasDoor(getCell(x/ numLength, square), getCell(x/ numLength, square+1))|| board.getBillboard().hasSameColor(getCell(x/ numLength, square), getCell(x/ numLength, square+1)))
                stream.append(" ");
            else
                stream.append("│");
        }
        else {
            for (int i = 0; i < numLength + length; i++)
                stream.append(" ");
            stream.append("│");
        }
        return stream;
    }

    /**
     * This function prints the coordinates of the cell
     * @param x coordinate
     * @param y coordinate
     * @return a stream
     */
    private StringBuilder printCoordinates(int x, int y){
        StringBuilder stream = new StringBuilder();
        stream.append(board.getBillboard().getCellPosition(getCell(x/ numLength, y)).toString());
        return stream;
    }

    /**
     * This function is used to decide what to print based on the type of the cell
     * @param x coordinate
     */
    private void printCards(int x) {
        StringBuilder stream = new StringBuilder();
        for(int i = MAX_WEAPON_REGENERATIONCELL.getValue() -1; i > -1; i--){
            for (int y = 0; y < horizontalCells; y++) {
                if (getCell(x, y) == null){
                    if(y == 0)
                       stream.append(" ");
                    for(int j = 0; j < (length + numLength)* length; j++)
                        stream.append(" ");
                    if(y != horizontalCells - 1 && getCell(x, y+1) != null)
                        stream.append("│");
                }
                else {
                    if (y == 0)
                        stream.append("│");
                    if (getCell(x, y).getClass() == NormalCell.class)
                        stream.append(printAmmo(getCell(x, y), i));
                 else  stream.append(printWeapons(getCell(x, y), i));
                }
        }
            stream.append("\n");}

        System.out.print(stream);
    }

    /**
     * This function prints Ammo if the cell has an ammo, else nothing
     * @param cell with needed information
     * @return a stream with infos
     */
    private StringBuilder printAmmo(Cell cell, int pos){
        StringBuilder stream = new StringBuilder();
        if (cell.getCard(0) != null && pos == 0){
            stream.append(printAmmoThings((AmmoCard)cell.getCard(0)));
        }
        else
            for (int i = 0; i < cell.getCard(0).toString().length(); i++)
                stream.append(" ");

        for(int j = cell.getCard(0).toString().length(); j < (numLength + length)* length; j++)
            stream.append(" ");
        stream.append("│");
        return stream;
    }

    /**
     * This function prints the information of the AmmoCard:
     * It will print in order the number of red, yellow and blu cubes it gives
     * it prints "+Power" if it gives a power up, else null spaces
     * @param ammo with needed information
     * @return a stream with infos
     */
    private StringBuilder printAmmoThings(AmmoCard ammo){
        StringBuilder stream = new StringBuilder();
        stream.append(ammo.toString());
        return stream;
    }

    /**
     * This function verifies if a Regeneration cell has weapons
     * @param cell with needed information
     * @return a stream with infos
     */
    private StringBuilder printWeapons(Cell cell, int pos){
        StringBuilder stream = new StringBuilder();
            if (cell.getCard(pos) != null)
                stream.append(printWeaponName((WeaponCard) cell.getCard(pos)));
            else for(int j = 0; j < (numLength + length)* length; j++)
                stream.append(" ");
        stream.append("│");
        return stream;
    }

    /**
     * This function prints the name of the weapon
     * @param weapon name to be printed
     * @return a stream with infos
     */
    private StringBuilder printWeaponName(WeaponCard weapon){
        StringBuilder stream = new StringBuilder();
        String name = weapon.getName().substring(0, Math.min(weapon.getName().length(), numLength + length -1));
        stream.append(name);
        for(int i = name.length(); i < numLength + length; i++)
            stream.append(" ");
        stream.append(weapon.getWeaponCostCLI());
        for(int i = stream.length(); i < (numLength + length)* length; i++)
            stream.append(" ");
        return stream;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o.getClass()== Board.class){
            this.board = (Board) o;
            drawCLI();
        }
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
