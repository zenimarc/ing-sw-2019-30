package player;

import constants.Constants;

import java.util.*;

/**
 * 
 */
public class PlayerBoard {

    //tiene la lista dei giocatori che hanno colpito
    private Player[] damageTrack;
    /**
     * tiene per ogni giocatore i marker che ha inflitto
     */
    private HashMap<Player, Integer> marks;
    private int numDeaths;

    /**
     * Default constructor
     */
    public PlayerBoard() {
        this.damageTrack = new Player[Constants.MAX_DAMAGE.getValue()];
        this.marks = new HashMap<Player, Integer>();
        this.numDeaths = 0;
    }

    /**
     *
     * Si occuperà del conteggio effettivo dei punti da assegnare ad ogni player (guardando il numDeath ecc), verrà divisa in sottofunzioni.
     */
    public HashMap<Player, Integer> getPoints() {
        return null;
    }

}