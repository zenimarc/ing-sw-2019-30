package board;

import player.Player;

import java.util.*;

/**
 * 
 */
public class Board {

    private int numSkulls;
    private HashMap<Player, Integer> playerSkulls;
    private Billboard billboard;
    private boolean isFinalFrenzy;


    /**
     * Default constructor
     */
    public Board() {
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
        if(this.numSkulls == 0)
            return true;
        return false;
    }

}