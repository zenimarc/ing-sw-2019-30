package player;

import board.Cell;

import java.util.*;

/**
 * Pawn is an association between player and position
 */
public class Pawn {

    private Player player;
    private Cell position;

    /**
     * Constructors
     */
    public Pawn(){
        this(null, null);
    }

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
     * Thisfunction modifies the position of the pawn
     * @param cell of destination
     */

    public void setCell(Cell cell){ //TODO: la chiamerà il controller quando il giocatore decide dove iniziare, è public? -Marco
        this.position = cell;
    }

    /**
     * This function return the position of the pawn
     * @return the position of the pawn
     */
    public Cell getCell (){return this.position;}

}