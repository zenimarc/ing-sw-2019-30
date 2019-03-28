package player;

import java.util.*;

/**
 * 
 */
public class PlayerBoard {

    /**
     * Default constructor
     */
    public PlayerBoard() {
    }

    /**
     * 
     */
    private Player[] damageTrack;

    /**
     * HashMap<Player, int>
     * tiene per ogni giocatore i marker che ha inflitto
     */
    private HashMap<Player, Integer> marks;

    /**
     * 
     */
    private int numDeaths;

    /**
     * HashMap<Player, int>
     * Si occuperà del conteggio effettivo dei punti da assegnare ad ogni player (guardando il numDeath ecc), verrà divisa in sottofunzioni.
     * @return
     */
    public HashMap<Player, Integer> getPoints() {
        // TODO implement here
        return null;
    }

}