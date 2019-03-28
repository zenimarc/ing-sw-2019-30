package player;

import deck.Bullet;
import deck.PowerCard;
import deck.WeaponCard;

import java.util.*;

/**
 * 
 */
public class Player {

    /**
     * Default constructor
     */
    public Player() {
    }

    /**
     * 
     */
    private String nickname;

    /**
     * 
     */
    private Pawn pawn;

    /**
     * 
     */
    private int points;

    /**
     * 
     */
    private PlayerBoard playerBoard;

    /**
     * 
     */
    private WeaponCard[] weapons;

    /**
     * 
     */
    private PowerCard[] powerups;

    /**
     * 
     */
    private ArrayList<Bullet> ammo;



}