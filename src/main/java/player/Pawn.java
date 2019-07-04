package player;

import board.Cell;

import java.io.Serializable;

/**
 * Pawn is an association between player and position
 */
public class Pawn implements Serializable {

    private Player player;
    private Cell position;

    /**
     * Constructors
     */

    public Pawn(Player player){
        this(player, null);
    }

    public Pawn(Player player, Cell position){
        this.player = player;
        this.position = position;
    }

    /**
     * This function returns the player associated with the pawn
     * @return the player associated
     */

    public Player getPlayer(){return this.player;}

    /**
     * This function modifies the position of the pawn
     * @param cell of destination
     */

    public void setCell(Cell cell){
        this.position = cell;
    }

    /**
     * This function return the position of the pawn
     * @return the position of the pawn
     */
    public Cell getCell(){return this.position;}

}