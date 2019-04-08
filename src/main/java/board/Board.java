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


    /**
     * Default constructor
     */
    public Board(){
        this(8);
    }
    public Board(int numskull) {
        this.numSkulls = numskull;
        this.playerSkulls = new HashMap<>();
        this.billboard = new Billboard();
    }


    /**
     * @return
     */
    public Board cloneBoard() {
        // TODO implement here
        return null;
    }

    public void decrementSkull(){
        this.numSkulls--;
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