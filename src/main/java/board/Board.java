package board;

import player.Player;

import java.util.*;

/**
 * 
 */
public class Board {

    /**
     * Default constructor
     */
    public Board() {
    }

    /**
     * 
     */
    private int numSkulls;

    /**
     * HashMap<Player, int>
     * teschi di ogni player
     */
    private HashMap<Player, Integer> playerSkulls;

    /**
     * 
     */
    private List<Door> doors;

    /**
     * in realtà il tipo è Cell[4][3]
     */
    private Cell[] billboard;

    /**
     * 
     */
    private boolean isFinalFrenzy;


    /**
     * @return
     */
    public Board cloneBoard() {
        // TODO implement here
        return null;
    }

    /**
     * @return
     */
    public boolean isFinalFrenzy() {
        // TODO implement here
        return false;
    }

}