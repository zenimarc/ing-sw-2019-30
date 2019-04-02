package board;

import player.Player;

import java.util.*;

/**
 * 
 */
public class Board {

    private int numSkulls;
    private HashMap<Player, Integer> playerSkulls;
    private List<Door> doors;
    private Cell[][] billboard;
    private boolean isFinalFrenzy;


    /**
     * Default constructor
     */
    public Board() {
        billboard = new Cell[4][3];
    }


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