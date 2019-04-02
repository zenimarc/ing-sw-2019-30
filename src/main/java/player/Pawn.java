package player;

import board.Cell;

import java.util.*;

/**
 * associazione tra player e posizione
 */
public class Pawn {

    private Player player;
    private Cell position;

    /**
     * Default constructor
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

    public void setPosition(Cell cell){ //TODO: la chiamerà il controller quando il giocatore decide dove iniziare, è public? -Marco
        this.position = cell;
    }



}